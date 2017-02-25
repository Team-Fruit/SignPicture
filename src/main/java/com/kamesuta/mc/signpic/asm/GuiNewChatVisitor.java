package com.kamesuta.mc.signpic.asm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.kamesuta.mc.signpic.asm.lib.DescHelper;
import com.kamesuta.mc.signpic.asm.lib.FieldMatcher;
import com.kamesuta.mc.signpic.asm.lib.MethodMatcher;

public class GuiNewChatVisitor extends ClassVisitor {
	private static class InitHookMethodVisitor extends MethodVisitor {
		private final FieldMatcher matcher;

		public InitHookMethodVisitor(final @Nullable MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
			this.matcher = new FieldMatcher("net/minecraft/client/gui/GuiNewChat", DescHelper.toDesc("java.util.List"), "field_146253_i", "field_146253_i");
		}

		@Override
		public void visitFieldInsn(final int opcode, @Nullable final String owner, @Nullable final String name, @Nullable final String desc) {
			super.visitFieldInsn(opcode, owner, name, desc);
			if (name!=null&&desc!=null)
				if (opcode==Opcodes.PUTFIELD&&this.matcher.match(name, desc)) {
					/*
					 * 37  aload_0 [this]
					 * 38  new com.kamesuta.mc.signpic.gui.PicChatHook [52]
					 * 41  dup
					 * 42  aload_0 [this]
					 * 43  getfield net.minecraft.client.gui.GuiNewChat.field_146253_i : java.util.List [50]
					 * 46  invokespecial com.kamesuta.mc.signpic.gui.PicChatHook(java.util.List) [54]
					 * 49  putfield net.minecraft.client.gui.GuiNewChat.hook : com.kamesuta.mc.signpic.gui.PicChatHook [57]
					 */
					visitVarInsn(Opcodes.ALOAD, 0);
					visitTypeInsn(Opcodes.NEW, "com/kamesuta/mc/signpic/gui/PicChatHook");
					visitInsn(Opcodes.DUP);
					visitVarInsn(Opcodes.ALOAD, 0);
					visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiNewChat", "field_146253_i", DescHelper.toDesc("java.util.List"));
					visitMethodInsn(Opcodes.INVOKESPECIAL, "com/kamesuta/mc/signpic/gui/PicChatHook", "<init>", DescHelper.toDesc(void.class, "java/util/List"), false);
					visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/client/gui/GuiNewChat", "hook", DescHelper.toDesc("com.kamesuta.mc.signpic.gui.PicChatHook"));
				}
		}
	}

	private final MethodMatcher initmatcher;

	public GuiNewChatVisitor(final @Nonnull String obfClassName, final @Nonnull ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		visitField(Opcodes.ACC_PUBLIC|Opcodes.ACC_FINAL, "hook", DescHelper.toDesc("com.kamesuta.mc.signpic.gui.PicChatHook"), null, null);
		this.initmatcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, new Object[0]), "<init>", "<init>");
	}

	@Override
	public @Nullable FieldVisitor visitField(final int access, @Nullable final String name, @Nullable final String desc, @Nullable final String signature, @Nullable final Object value) {
		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public @Nullable MethodVisitor visitMethod(final int access, final @Nullable String name, final @Nullable String desc, final @Nullable String signature, final @Nullable String[] exceptions) {
		final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
		if (name==null||desc==null)
			return parent;
		if (this.initmatcher.match(name, desc))
			return new InitHookMethodVisitor(parent);

		return parent;
	}
}