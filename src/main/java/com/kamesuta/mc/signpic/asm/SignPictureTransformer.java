package com.kamesuta.mc.signpic.asm;

import javax.annotation.Nullable;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.asm.lib.VisitorHelper;
import com.kamesuta.mc.signpic.asm.lib.VisitorHelper.TransformProvider;

import net.minecraft.launchwrapper.IClassTransformer;

public class SignPictureTransformer implements IClassTransformer {
	@Override
	public @Nullable byte[] transform(final @Nullable String name, final @Nullable String transformedName, final @Nullable byte[] bytes) {
		if (bytes==null||name==null||transformedName==null)
			return bytes;

		if (transformedName.equals("net.minecraft.client.gui.GuiScreenBook"))
			return VisitorHelper.apply(bytes, name, new TransformProvider(ClassWriter.COMPUTE_FRAMES) {
				@Override
				public ClassVisitor createVisitor(final String name, final ClassVisitor cv) {
					Log.log.info(String.format("Patching GuiScreenBook.drawScreen (class: %s)", name));
					return new GuiScreenBookVisitor(name, cv);
				}
			});

		if (transformedName.equals("net.minecraft.client.gui.GuiNewChat"))
			return VisitorHelper.apply(bytes, name, new TransformProvider(ClassWriter.COMPUTE_FRAMES) {
				@Override
				public ClassVisitor createVisitor(final String name, final ClassVisitor cv) {
					Log.log.info(String.format("Patching GuiNewChat (class: %s)", name));
					return new GuiNewChatVisitor(name, cv);
				}
			});

		if (transformedName.equals("net.minecraft.client.gui.GuiScreen"))
			return VisitorHelper.apply(bytes, name, new TransformProvider(ClassWriter.COMPUTE_FRAMES) {
				@Override
				public ClassVisitor createVisitor(final String name, final ClassVisitor cv) {
					Log.log.info(String.format("Patching GuiScreen.handleInput (class: %s)", name));
					return new GuiScreenVisitor(name, cv);
				}
			});

		return bytes;
	}
}