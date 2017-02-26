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

	private static class DrawChatHookMethodVisitor extends MethodVisitor {
		private final MethodMatcher matcher;

		public DrawChatHookMethodVisitor(final @Nullable MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
			this.matcher = new MethodMatcher("net/minecraft/client/gui/FontRenderer", DescHelper.toDesc(int.class, "java.lang.String", int.class, int.class, int.class), "drawStringWithShadow", "drawStringWithShadow");
		}

		@Override
		public void visitCode() {
			/*
			 * 106  aload_0 [this]
			 * 107  getfield net.minecraft.client.gui.GuiNewChat.hook : com.kamesuta.mc.signpic.gui.PicChatHook [57]
			 * 110  invokevirtual com.kamesuta.mc.signpic.gui.PicChatHook.updateLines() : void [128]
			 */
			visitVarInsn(Opcodes.ALOAD, 0);
			visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiNewChat", "hook", DescHelper.toDesc("com.kamesuta.mc.signpic.gui.PicChatHook"));
			visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/kamesuta/mc/signpic/gui/PicChatHook", "updateLines", DescHelper.toDesc(void.class, new Object[0]), false);
			super.visitCode();
		}

		@Override
		public void visitMethodInsn(final int opcode, @Nullable final String owner, @Nullable final String name, @Nullable final String desc, final boolean itf) {
			if (name!=null&&desc!=null&&this.matcher.match(name, desc)) {
				/*
				 * 328  aload_0 [this]
				 * 329  aload 12 [chatline]
				 * 331  iload 16 [j2]
				 * 333  iload 11 [i2]
				 * 335  invokestatic com.kamesuta.mc.signpic.gui.PicChatLine.hookDrawStringWithShadow(net.minecraft.client.gui.FontRenderer, java.lang.String, int, int, int, net.minecraft.client.gui.GuiNewChat, net.minecraft.client.gui.ChatLine, int, int) : int [170]
				 */
				visitVarInsn(Opcodes.ALOAD, 0);
				visitVarInsn(Opcodes.ALOAD, 12);
				visitVarInsn(Opcodes.ILOAD, 16);
				visitVarInsn(Opcodes.ILOAD, 11);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/kamesuta/mc/signpic/gui/PicChatLine", "hookDrawStringWithShadow",
						DescHelper.toDesc(int.class,
								"net.minecraft.client.gui.FontRenderer",
								"java.lang.String", int.class, int.class, int.class,
								"net.minecraft.client.gui.GuiNewChat",
								"net.minecraft.client.gui.ChatLine", int.class, int.class),
						false);
			} else
				super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

	private static class ApplyHookMethodVisitor extends MethodVisitor {
		private final MethodMatcher matcher;

		public ApplyHookMethodVisitor(final @Nullable MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
			this.matcher = new MethodMatcher("java/util/List", DescHelper.toDesc(boolean.class, "java.lang.Object"), "add", "add");
		}

		@Override
		public void visitMethodInsn(final int opcode, @Nullable final String owner, @Nullable final String name, @Nullable final String desc, final boolean itf) {
			if (name!=null&&desc!=null&&"".equals(owner)&&this.matcher.match(name, desc)) {
				/*
				 * 328  aload_0 [this]
				 * 329  aload 12 [chatline]
				 * 331  iload 16 [j2]
				 * 333  iload 11 [i2]
				 * 335  invokestatic com.kamesuta.mc.signpic.gui.PicChatLine.hookDrawStringWithShadow(net.minecraft.client.gui.FontRenderer, java.lang.String, int, int, int, net.minecraft.client.gui.GuiNewChat, net.minecraft.client.gui.ChatLine, int, int) : int [170]
				 */
				visitVarInsn(Opcodes.ALOAD, 0);
				visitVarInsn(Opcodes.ALOAD, 12);
				visitVarInsn(Opcodes.ILOAD, 16);
				visitVarInsn(Opcodes.ILOAD, 11);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/kamesuta/mc/signpic/gui/PicChatLine", "hookDrawStringWithShadow", DescHelper.toDesc(int.class, "net.minecraft.client.gui.FontRenderer", "java.lang.String", int.class, int.class, int.class, "net.minecraft.client.gui.GuiNewChat", "net.minecraft.client.gui.ChatLine", int.class, int.class), false);
			} else
				super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

	private final MethodMatcher initmatcher;
	private final MethodMatcher drawchatmatcher;
	private final MethodMatcher applymatcher;

	public GuiNewChatVisitor(final @Nonnull String obfClassName, final @Nonnull ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		visitField(Opcodes.ACC_PUBLIC|Opcodes.ACC_FINAL, "hook", DescHelper.toDesc("com.kamesuta.mc.signpic.gui.PicChatHook"), null, null);
		this.initmatcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, new Object[0]), "<init>", "<init>");
		this.drawchatmatcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, int.class), "drawChat", "drawChat");
		this.applymatcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, "net.minecraft.util.IChatComponent", int.class, int.class, boolean.class), "func_146237_a", "func_146237_a");
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
		if (this.drawchatmatcher.match(name, desc))
			return new DrawChatHookMethodVisitor(parent);
		if (this.applymatcher.match(name, desc))
			return new ApplyHookMethodVisitor(parent);

		return parent;
	}
}