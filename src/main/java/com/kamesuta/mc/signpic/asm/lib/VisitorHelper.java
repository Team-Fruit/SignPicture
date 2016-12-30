/*
 * This class is from the OpenModsLib.
 * https://github.com/OpenMods/OpenModsLib
 *
 * Code Copyright (c) 2013 Open Mods
 * Code released under the MIT license
 * https://github.com/OpenMods/OpenModsLib/blob/master/LICENSE
 */
package com.kamesuta.mc.signpic.asm.lib;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.Launch;

public class VisitorHelper {

	public abstract static class TransformProvider {
		private final int flags;

		public TransformProvider(final int flags) {
			this.flags = flags;
		}

		public abstract @Nonnull ClassVisitor createVisitor(@Nonnull String name, @Nonnull ClassVisitor cv);
	}

	public static byte[] apply(final @Nonnull byte[] bytes, final @Nonnull String name, final @Nonnull TransformProvider context) {
		Validate.notNull(bytes);
		final ClassReader cr = new ClassReader(bytes);
		final ClassWriter cw = new ClassWriter(cr, context.flags);
		final ClassVisitor mod = context.createVisitor(name, cw);

		try {
			cr.accept(mod, 0);
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
		return useSrgNames() ? FMLDeobfuscatingRemapper.INSTANCE.unmap(clsName) : clsName;
	}
}