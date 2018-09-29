package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

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

	private static <T> T invoke(final String afterFmlPath, final String name, final Class<?>[] types, final Object instance, final Object[] params) {
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
		if ($class==null)
			throw new RuntimeException("Could not find fml class");
		Method $method;
		try {
			$method = $class.getMethod(name, types);
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

	public static void loadVersion() {
		final Object loader = invoke("common.Loader", "instance", new Class[0], null, new Object[0]);
		final Object container = invoke("common.Loader", "activeModContainer", new Class[0], loader, new Object[0]);
		final Object[] data = invoke("relauncher.FMLInjectionData", "data", new Class[0], null, new Object[0]);
		final String mccversion0 = (String) data[4];
		final String mccversion = getVersion(mccversion0);

		if (mccversion==null)
			throw new RuntimeException(String.format("Version %s is not supported! Supported version is %s.", mccversion0, versions));

		final File minecraftDir = (File) data[6];
		final File modsDir = new File(minecraftDir, "mods");
		if (container!=null) {
			final File modFile = invoke("common.ModContainer", "getSource", new Class[0], container, new Object[0]);
			if (modFile!=null) {
				ZipFile file = null;
				InputStream stream = null;
				try {
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
					new RuntimeException("Could not load version-specific file.", e);
				} finally {
					IOUtils.closeQuietly(file);
					IOUtils.closeQuietly(stream);
				}
			}
		}
	}
}
