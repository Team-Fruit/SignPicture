/*
 * This class is from the OpenModsLib.
 * https://github.com/OpenMods/OpenModsLib
 *
 * Code Copyright (c) 2013 Open Mods
 * Code released under the MIT license
 * https://github.com/OpenMods/OpenModsLib/blob/master/LICENSE
 */
package net.teamfruit.signpic.asm.lib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.Launch;
import net.teamfruit.signpic.compat.Compat.CompatFMLDeobfuscatingRemapper;

public class VisitorHelper {

	public abstract static class TransformProvider {
		private final int writerFlags;
		private final int readerFlags;

		public TransformProvider(final int writerFlags) {
			this.writerFlags = writerFlags;
			this.readerFlags = 0;
		}

		public TransformProvider(final int writerFlags, final int readerFlags) {
			this.writerFlags = writerFlags;
			this.readerFlags = readerFlags;
		}

		public abstract @Nonnull ClassVisitor createVisitor(@Nonnull String name, @Nonnull ClassVisitor cv);
	}

	public static byte[] apply(final @Nonnull byte[] bytes, final @Nonnull String name, final @Nonnull TransformProvider context) {
		Validate.notNull(bytes);
		final ClassReader cr = new ClassReader(bytes);
		// ASMライブラリのクラスでのgetClass().getClassLoader()はForgeのクラスを見つけることができない可能性があります。
		// インナークラスを作成してgetClass().getClassLoader()をLaunchClassLoaderにしましょう。
		final ClassWriter cw = new ClassWriter(cr, context.writerFlags) {
			@Override
			protected String getCommonSuperClass(final @Nullable String type1, final @Nullable String type2) {
				if (type1==null||type2==null)
					throw new NullPointerException();
				Class<?> c, d;
				final ClassLoader classLoader = ClassWriter.class.getClassLoader();
				try {
					try {
						c = Class.forName(type1.replace('/', '.'), false, classLoader);
						d = Class.forName(type2.replace('/', '.'), false, classLoader);
					} catch (final ClassNotFoundException e) {
						final ClassLoader launchClassLoader = getClass().getClassLoader();
						try {
							c = Class.forName(type1.replace('/', '.'), false, launchClassLoader);
							d = Class.forName(type2.replace('/', '.'), false, launchClassLoader);
						} catch (final ClassNotFoundException e1) {
							throw new RuntimeException(String.format("ClassLoader: %s, LaunchClassLoader: %s", e.toString(), e1.toString()));
						}
					}
				} catch (final Exception e) {
					throw new RuntimeException(e.toString());
				}
				if (c.isAssignableFrom(d))
					return type1;
				if (d.isAssignableFrom(c))
					return type2;
				if (c.isInterface()||d.isInterface())
					return "java/lang/Object";
				else {
					do
						c = c.getSuperclass();
					while (!c.isAssignableFrom(d));
					return c.getName().replace('.', '/');
				}
			}
		};
		final ClassVisitor mod = context.createVisitor(name, cw);

		try {
			cr.accept(mod, context.readerFlags);
			return cw.toByteArray();
		} catch (final StopTransforming e) {
			return bytes;
		}
	}

	public static boolean useSrgNames() {
		final Boolean deobfuscated = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		return deobfuscated==null||!deobfuscated;
	}

	public static @Nonnull String getMappedName(final @Nonnull String clsName) {
		return useSrgNames() ? CompatFMLDeobfuscatingRemapper.unmap(clsName) : clsName;
	}
}