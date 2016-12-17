package com.kamesuta.mc.signpic.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.asm.lib.VisitorHelper;
import com.kamesuta.mc.signpic.asm.lib.VisitorHelper.TransformProvider;

import net.minecraft.launchwrapper.IClassTransformer;

public class SignPictureClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
		if (bytes==null)
			return bytes;

		if (transformedName.equals("net.minecraft.block.BlockSign"))
			return VisitorHelper.apply(bytes, name, new TransformProvider(ClassWriter.COMPUTE_FRAMES) {
				@Override
				public ClassVisitor createVisitor(final String name, final ClassVisitor cv) {
					Log.info(String.format("Trying to patch BlockSign.getCollisionBoundingBox (class: %s)", name));
					return new BlockSignVisitor(name, cv);
				}
			});

		return bytes;
	}
}