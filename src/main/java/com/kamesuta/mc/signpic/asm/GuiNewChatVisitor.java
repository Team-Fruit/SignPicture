package com.kamesuta.mc.signpic.asm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.kamesuta.mc.signpic.asm.lib.DescHelper;
import com.kamesuta.mc.signpic.asm.lib.MethodMatcher;
import com.kamesuta.mc.signpic.asm.lib.RefName;
import com.kamesuta.mc.signpic.asm.lib.VisitorHelper;

public class GuiNewChatVisitor extends ClassVisitor {
	private static class InitHookMethodVisitor extends MethodVisitor {
		public InitHookMethodVisitor(final @Nullable MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
		}

		@Override
		public void visitInsn(final int opcode) {
			if (opcode==Opcodes.RETURN) {
				/*
				 * 42  aload_0 [this]
				 * 43  new com.kamesuta.mc.signpic.render.CustomChatRender$PicChatHook [54]
				 * 46  dup
				 * 47  aload_0 [this]
				 * 48  getfield net.minecraft.client.gui.GuiNewChat.field_146253_i : java.util.List [50]
				 * 51  invokespecial com.kamesuta.mc.signpic.render.CustomChatRender$PicChatHook(java.util.List) [56]
				 * 54  putfield net.minecraft.client.gui.GuiNewChat.hook : com.kamesuta.mc.signpic.render.CustomChatRender.PicChatHook [59]
				 */
				super.visitVarInsn(Opcodes.ALOAD, 0);
				super.visitTypeInsn(Opcodes.NEW, "com/kamesuta/mc/signpic/render/CustomChatRender$PicChatHook");
				super.visitInsn(Opcodes.DUP);
				super.visitVarInsn(Opcodes.ALOAD, 0);
				super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiNewChat", ASMDeobfNames.GuiNewChatDrawnChatLines.name(), DescHelper.toDesc("java.util.List"));
				super.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/kamesuta/mc/signpic/render/CustomChatRender$PicChatHook", "<init>", DescHelper.toDesc(void.class, "java.util.List"), false);
				super.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/client/gui/GuiNewChat", "hook", DescHelper.toDesc("com.kamesuta.mc.signpic.render.CustomChatRender$PicChatHook"));
			}
			super.visitInsn(opcode);
		}
	}

	private static class DrawChatHookMethodVisitor extends MethodVisitor {
		private final MethodMatcher matcher;

		public DrawChatHookMethodVisitor(final @Nullable MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
			this.matcher = new MethodMatcher(VisitorHelper.getMappedName("net/minecraft/client/gui/FontRenderer"), DescHelper.toDesc(int.class, "java.lang.String", int.class, int.class, int.class), ASMDeobfNames.FontRendererDrawStringWithShadow);
		}

		@Override
		public void visitCode() {
			/*
			 * 106  aload_0 [this]
			 * 107  getfield net.minecraft.client.gui.GuiNewChat.hook : com.kamesuta.mc.signpic.gui.PicChatHook [57]
			 * 110  invokevirtual com.kamesuta.mc.signpic.gui.PicChatHook.updateLines() : void [128]
			 */
			super.visitVarInsn(Opcodes.ALOAD, 0);
			super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiNewChat", "hook", DescHelper.toDesc("com.kamesuta.mc.signpic.render.CustomChatRender$PicChatHook"));
			super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/kamesuta/mc/signpic/render/CustomChatRender$PicChatHook", "updateLines", DescHelper.toDesc(void.class, new Object[0]), false);
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
				super.visitVarInsn(Opcodes.ALOAD, 0);
				super.visitVarInsn(Opcodes.ALOAD, 12);
				super.visitVarInsn(Opcodes.ILOAD, 16);
				super.visitVarInsn(Opcodes.ILOAD, 11);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/kamesuta/mc/signpic/render/CustomChatRender", "hookDrawStringWithShadow",
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

	private static class GetChatComponentHookMethodVisitor extends MethodVisitor {

		public GetChatComponentHookMethodVisitor(final @Nullable MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
		}

		@Override
		public void visitTypeInsn(final int opcode, final @Nullable String type) {
			super.visitTypeInsn(opcode, type);
			if (opcode==Opcodes.CHECKCAST&&VisitorHelper.getMappedName("net/minecraft/client/gui/ChatLine").equals(type)) {
				/*
				 * 203  checkcast net.minecraft.client.gui.ChatLine [137]
				 * 206  astore 10 [chatline]
				 * 208  aload 10 [chatline]
				 * 210  instanceof com.kamesuta.mc.signpic.gui.PicChatLine [445]
				 * 213  ifeq 244
				 * 216  aload 10 [chatline]
				 * 218  checkcast com.kamesuta.mc.signpic.gui.PicChatLine [445]
				 * 221  aload_0 [this]
				 * 222  aload_0 [this]
				 * 223  getfield net.minecraft.client.gui.GuiNewChat.mc : net.minecraft.client.Minecraft [59]
				 * 226  getfield net.minecraft.client.Minecraft.fontRenderer : net.minecraft.client.gui.FontRenderer [168]
				 * 229  iload 6 [l]
				 * 231  invokevirtual com.kamesuta.mc.signpic.gui.PicChatLine.onClicked(net.minecraft.client.gui.GuiNewChat, net.minecraft.client.gui.FontRenderer, int) : net.minecraft.util.IChatComponent [447]
				 * 234  astore 11 [c]
				 * 236  aload 11 [c]
				 * 238  ifnull 244
				 * 241  aload 11 [c]
				 * 243  areturn
				 * 244  iconst_0
				 */
				super.visitVarInsn(Opcodes.ASTORE, 10);
				super.visitVarInsn(Opcodes.ALOAD, 10);
				super.visitTypeInsn(Opcodes.INSTANCEOF, "com/kamesuta/mc/signpic/render/CustomChatRender$PicChatLine");
				final Label l = new Label();
				super.visitJumpInsn(Opcodes.IFEQ, l);
				super.visitVarInsn(Opcodes.ALOAD, 10);
				super.visitTypeInsn(Opcodes.CHECKCAST, "com/kamesuta/mc/signpic/render/CustomChatRender$PicChatLine");
				super.visitVarInsn(Opcodes.ALOAD, 0);
				super.visitVarInsn(Opcodes.ILOAD, 6);
				super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/kamesuta/mc/signpic/render/CustomChatRender$PicChatLine", "onClicked", DescHelper.toDesc("net.minecraft.util.IChatComponent", "net.minecraft.client.gui.GuiNewChat", int.class), false);
				super.visitVarInsn(Opcodes.ASTORE, 11);
				super.visitVarInsn(Opcodes.ALOAD, 11);
				super.visitJumpInsn(Opcodes.IFNULL, l);
				super.visitVarInsn(Opcodes.ALOAD, 11);
				super.visitInsn(Opcodes.ARETURN);
				super.visitLabel(l);
				super.visitVarInsn(Opcodes.ALOAD, 10);
			}
		}
	}

	private final MethodMatcher initmatcher;
	private final MethodMatcher drawchatmatcher;
	private final MethodMatcher getchatcomponentmatcher;

	public GuiNewChatVisitor(final @Nonnull String obfClassName, final @Nonnull ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		visitField(Opcodes.ACC_PUBLIC|Opcodes.ACC_FINAL, "hook", DescHelper.toDesc("com.kamesuta.mc.signpic.render.CustomChatRender$PicChatHook"), null, null);
		this.initmatcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, new Object[0]), RefName.name("<init>"));
		this.drawchatmatcher = new MethodMatcher(obfClassName, DescHelper.toDesc(void.class, int.class), ASMDeobfNames.GuiNewChatDrawChat);
		this.getchatcomponentmatcher = new MethodMatcher(obfClassName, DescHelper.toDesc("net.minecraft.util.IChatComponent", int.class, int.class), ASMDeobfNames.GuiNewChatGetChatComponent);
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
		if (this.getchatcomponentmatcher.match(name, desc))
			return new GetChatComponentHookMethodVisitor(parent);
		return parent;
	}
}