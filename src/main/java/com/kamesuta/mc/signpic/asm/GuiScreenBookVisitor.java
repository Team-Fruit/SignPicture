package com.kamesuta.mc.signpic.asm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.kamesuta.mc.signpic.asm.lib.DescHelper;
import com.kamesuta.mc.signpic.asm.lib.MethodMatcher;
import com.kamesuta.mc.signpic.asm.lib.VisitorHelper;

public class GuiScreenBookVisitor extends ClassVisitor {
	private static class HookMethodVisitor extends MethodVisitor {
		private final @Nonnull MethodMatcher matcher;

		public HookMethodVisitor(final @Nullable MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
			this.matcher = new MethodMatcher(VisitorHelper.getMappedName("net/minecraft/client/gui/FontRenderer"), DescHelper.toDesc(void.class, "java.lang.String", int.class, int.class, int.class, int.class), ASMDeobfNames.FontRendererDrawSplitString);
		}

		@Override
		public void visitMethodInsn(final int opcode, final @Nullable String owner, final @Nullable String name, final @Nullable String desc, final boolean itf) {
			if (name!=null&&desc!=null&&this.matcher.match(name, desc))
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/kamesuta/mc/signpic/render/CustomBookRenderer", "hookDrawSplitString", desc, itf);
			else
				super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

	private final MethodMatcher matcher;

	public GuiScreenBookVisitor(final @Nonnull String obfClassName, final @Nonnull ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.matcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, int.class, int.class, float.class), ASMDeobfNames.GuiScreenBookDrawScreen);
	}

	@Override
	public @Nullable MethodVisitor visitMethod(final int access, final @Nullable String name, final @Nullable String desc, final @Nullable String signature, final @Nullable String[] exceptions) {
		final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
		if (name==null||desc==null)
			return parent;
		return this.matcher.match(name, desc) ? new HookMethodVisitor(parent) : parent;
	}
}