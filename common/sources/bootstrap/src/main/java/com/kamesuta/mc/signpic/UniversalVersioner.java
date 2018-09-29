package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;

public class UniversalVersioner {
	public static final ImmutableMap<String, String> versions = ImmutableMap.<String, String> builder()
			.put("1.7", "1.7.10")
			.put("1.8", "1.8.9")
			.put("1.9", "1.9.4")
			.put("1.10", "1.10.2")
			.put("1.11", "1.11.2")
			.put("1.12", "1.12.2")
			.build();

	public static String getVersion(final String mccversion) {
		for (final Entry<String, String> entry : UniversalVersioner.versions.entrySet()) {
			final String key = entry.getKey();
			if (StringUtils.startsWith(mccversion, key))
				return entry.getValue();
		}
		return null;
	}

	private static Class<?> getFMLClass(final String afterFmlPath) {
		Class<?> $class;
		try {
			$class = Class.forName("net.minecraftforge.fml."+afterFmlPath);
		} catch (final ClassNotFoundException e1) {
			try {
				$class = Class.forName("cpw.mods.fml."+afterFmlPath);
			} catch (final ClassNotFoundException e2) {
				throw new RuntimeException("Could not load fml class");
			}
		}
		return $class;
	}

	private static <T> T invokeFMLMethod(final Class<?> $class, final String name, final Class<?>[] types, final Object instance, final Object[] params, final boolean declared) {
		if ($class==null)
			throw new RuntimeException("Could not find fml class");
		Method $method;
		try {
			$method = declared ? $class.getDeclaredMethod(name, types) : $class.getMethod(name, types);
			$method.setAccessible(true);
		} catch (final Exception e) {
			throw new RuntimeException("Could not find or access fml method");
		}
		try {
			@SuppressWarnings("unchecked")
			final T res = (T) $method.invoke(instance, params);
			return res;
		} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
			throw new RuntimeException("Could not invoke fml method");
		}
	}

	private static <T> T getFMLField(final Class<?> $class, final String name, final Object instance, final boolean declared) {
		if ($class==null)
			throw new RuntimeException("Could not find fml class");
		Field $field;
		try {
			$field = declared ? $class.getDeclaredField(name) : $class.getField(name);
			$field.setAccessible(true);
		} catch (final Exception e) {
			throw new RuntimeException("Could not find or access fml field");
		}
		try {
			@SuppressWarnings("unchecked")
			final T res = (T) $field.get(instance);
			return res;
		} catch (IllegalAccessException|IllegalArgumentException e) {
			throw new RuntimeException("Could not get fml field");
		}
	}

	private static void loadVersionImpl(final File modFile) {
		if (modFile==null)
			throw new RuntimeException("Could not specify mod file.");

		ZipFile file = null;
		InputStream stream = null;
		try {
			final Object[] data = invokeFMLMethod(getFMLClass("relauncher.FMLInjectionData"), "data", new Class[0], null, new Object[0], false);
			final String mccversion0 = (String) data[4];
			final String mccversion = getVersion(mccversion0);

			if (mccversion==null)
				throw new RuntimeException(String.format("Version %s is not supported! Supported version is %s.", mccversion0, versions));

			final File minecraftDir = (File) data[6];
			final File modsDir = new File(minecraftDir, "mods");

			final File canonicalModsDir = modsDir.getCanonicalFile();
			final File versionSpecificModsDir = new File(canonicalModsDir, mccversion);
			final File modVersionSpecific = new File(versionSpecificModsDir, Reference.MODID);

			final String jarname = String.format("%s.jar", mccversion);
			final File destMod = new File(modVersionSpecific, jarname);

			file = new ZipFile(modFile);
			final ZipEntry entry = file.getEntry(jarname);
			if (entry==null)
				throw new RuntimeException("Could not find version-specific file: "+jarname);
			stream = file.getInputStream(entry);

			FileUtils.copyInputStreamToFile(stream, destMod);
			Launch.classLoader.addURL(destMod.toURI().toURL());
		} catch (final IOException e) {
			throw new RuntimeException("Could not load version-specific file.", e);
		} finally {
			IOUtils.closeQuietly(file);
			IOUtils.closeQuietly(stream);
		}
	}

	public static void loadVersionFromFMLMod() {
		final Object loader = invokeFMLMethod(getFMLClass("common.Loader"), "instance", new Class[0], null, new Object[0], false);
		final Object container = invokeFMLMethod(getFMLClass("common.Loader"), "activeModContainer", new Class[0], loader, new Object[0], false);
		final File modFile = invokeFMLMethod(getFMLClass("common.ModContainer"), "getSource", new Class[0], container, new Object[0], false);
		loadVersionImpl(modFile);
	}

	public static void loadVersionFromCoreMod(final Class<?> coreModClass) {
		File modFile = null;
		@SuppressWarnings("unchecked")
		final List<ITweaker> tweakers = (List<ITweaker>) Launch.blackboard.get("Tweaks");
		if (tweakers!=null) {
			final Class<?> fmlPlugin = getFMLClass("relauncher.CoreModManager$FMLPluginWrapper");
			for (final ITweaker tweaker : tweakers)
				if (fmlPlugin.isInstance(tweaker)) {
					final Object coreModInstance = getFMLField(fmlPlugin, "coreModInstance", tweaker, true);
					if (coreModClass.isInstance(coreModInstance))
						modFile = getFMLField(fmlPlugin, "location", tweaker, true);
				}
		}
		loadVersionImpl(modFile);
	}
}
