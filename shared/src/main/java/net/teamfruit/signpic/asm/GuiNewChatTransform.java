package net.teamfruit.signpic.asm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.teamfruit.signpic.asm.lib.*;
import net.teamfruit.signpic.compat.CompatBaseVersion;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.teamfruit.signpic.compat.CompatVersion;

public class GuiNewChatTransform implements INodeCoreTransformer {
	@Override public ClassName getClassName() {
		return ClassName.of("net.minecraft.client.gui.GuiNewChat");
	}

	@Nonnull @Override public ClassVisitor createVisitor(@Nonnull String name, @Nonnull ClassVisitor cv) throws StopTransforming {
		return new GuiNewChatVisitor(name, cv);
	}

	public static class GuiNewChatVisitor extends ClassVisitor {
		private static class InitHookMethodVisitor extends MethodVisitor {
			public InitHookMethodVisitor(final @Nullable MethodVisitor mv) {
				super(Opcodes.ASM5, mv);
			}

			@Override
			public void visitInsn(final int opcode) {
				if (opcode==Opcodes.RETURN) {
					/*
					 * 42  aload_0 [this]
					 * 43  new net.teamfruit.signpic.render.CustomChatRender$PicChatHook [54]
					 * 46  dup
					 * 47  aload_0 [this]
					 * 48  getfield net.minecraft.client.gui.GuiNewChat.field_146253_i : java.util.List [50]
					 * 51  invokespecial net.teamfruit.signpic.render.CustomChatRender$PicChatHook(java.util.List) [56]
					 * 54  putfield net.minecraft.client.gui.GuiNewChat.hook : net.teamfruit.signpic.render.CustomChatRender.PicChatHook [59]
					 */
					super.visitVarInsn(Opcodes.ALOAD, 0);
					super.visitTypeInsn(Opcodes.NEW, ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatHook").getBytecodeName());
					super.visitInsn(Opcodes.DUP);
					super.visitVarInsn(Opcodes.ALOAD, 0);
					super.visitFieldInsn(Opcodes.GETFIELD, ClassName.of("net.minecraft.client.gui.GuiNewChat").getBytecodeName(), ASMDeobfNames.GuiNewChatDrawnChatLines.name(), DescHelper.toDesc(ClassName.of("java.util.List")));
					super.visitMethodInsn(Opcodes.INVOKESPECIAL, ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatHook").getBytecodeName(), "<init>", DescHelper.toDescMethod(void.class, ClassName.of("java.util.List")), false);
					super.visitFieldInsn(Opcodes.PUTFIELD, ClassName.of("net.minecraft.client.gui.GuiNewChat").getBytecodeName(), "hook", DescHelper.toDesc(ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatHook")));
				}
				super.visitInsn(opcode);
			}
		}

		private static class DrawChatHookMethodVisitor extends MethodVisitor {
			private final MethodMatcher matcher;

			public DrawChatHookMethodVisitor(final @Nullable MethodVisitor mv) {
				super(Opcodes.ASM5, mv);
				switch (CompatVersion.version()) {
					case V7:
						this.matcher = new MethodMatcher(ClassName.of("net.minecraft.client.gui.FontRenderer"), DescHelper.toDescMethod(int.class, ClassName.of("java.lang.String"), int.class, int.class, int.class), ASMDeobfNames.FontRendererDrawStringWithShadow);
						break;
					default:
						this.matcher = new MethodMatcher(ClassName.of("net.minecraft.client.gui.FontRenderer"), DescHelper.toDescMethod(int.class, ClassName.of("java.lang.String"), float.class, float.class, int.class), ASMDeobfNames.FontRendererDrawStringWithShadow);
						break;
				}
			}

			@Override
			public void visitCode() {
				/*
				 * 106  aload_0 [this]
				 * 107  getfield net.minecraft.client.gui.GuiNewChat.hook : net.teamfruit.signpic.gui.PicChatHook [57]
				 * 110  invokevirtual net.teamfruit.signpic.gui.PicChatHook.updateLines() : void [128]
				 */
				super.visitVarInsn(Opcodes.ALOAD, 0);
				super.visitFieldInsn(Opcodes.GETFIELD, ClassName.of("net.minecraft.client.gui.GuiNewChat").getBytecodeName(), "hook", DescHelper.toDesc(ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatHook")));
				super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatHook").getBytecodeName(), "updateLines", DescHelper.toDescMethod(void.class), false);
				super.visitCode();
			}

			@Override
			public void visitMethodInsn(final int opcode, @Nullable final String owner, @Nullable final String name, @Nullable final String desc, final boolean itf) {
				if (name!=null&&desc!=null&&this.matcher.match(name, desc)) {
					switch (CompatVersion.version()) {
						case V7:
							/*
							 * 328  aload_0 [this]
							 * 329  aload 12 [chatline]
							 * 331  iload 16 [j2]
							 * 333  iload 11 [i2]
							 * 335  invokestatic net.teamfruit.signpic.gui.PicChatLine.hookDrawStringWithShadow(net.minecraft.client.gui.FontRenderer, java.lang.String, int, int, int, net.minecraft.client.gui.GuiNewChat, net.minecraft.client.gui.ChatLine, int, int) : int [170]
							 */
							super.visitVarInsn(Opcodes.ISTORE, 32);
							super.visitVarInsn(Opcodes.ISTORE, 33);
							super.visitInsn(Opcodes.I2F);
							super.visitVarInsn(Opcodes.ILOAD, 33);
							super.visitInsn(Opcodes.I2F);
							super.visitVarInsn(Opcodes.ILOAD, 32);

							super.visitVarInsn(Opcodes.ALOAD, 0);
							super.visitVarInsn(Opcodes.ALOAD, 12);
							super.visitVarInsn(Opcodes.ILOAD, 16);
							super.visitVarInsn(Opcodes.ILOAD, 11);

							super.visitInsn(Opcodes.I2F);
							super.visitLdcInsn(255f);
							super.visitInsn(Opcodes.FDIV);

							break;
						case V8:
							super.visitVarInsn(Opcodes.ALOAD, 0);
							super.visitVarInsn(Opcodes.ALOAD, 10);
							super.visitVarInsn(Opcodes.ILOAD, 16);
							super.visitVarInsn(Opcodes.ILOAD, 14);

							super.visitInsn(Opcodes.I2F);
							super.visitLdcInsn(255f);
							super.visitInsn(Opcodes.FDIV);

							break;
						default:
							super.visitVarInsn(Opcodes.ALOAD, 0);
							super.visitVarInsn(Opcodes.ALOAD, 10);
							super.visitVarInsn(Opcodes.ILOAD, 16);
							super.visitVarInsn(Opcodes.FLOAD, 6);

							break;
					}

					super.visitMethodInsn(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.signpic.render.CustomChatRender").getBytecodeName(), "hookDrawStringWithShadow",
							DescHelper.toDescMethod(int.class,
									ClassName.of("net.minecraft.client.gui.FontRenderer"),
									ClassName.of("java.lang.String"), float.class, float.class, int.class,
									ClassName.of("net.minecraft.client.gui.GuiNewChat"),
									ClassName.of("net.minecraft.client.gui.ChatLine"), int.class, float.class),
							false);
				} else
					super.visitMethodInsn(opcode, owner, name, desc, itf);
			}
		}

		private static class GetChatComponentHookMethodVisitor extends MethodVisitor {
			private final ClassName classmatcher;

			public GetChatComponentHookMethodVisitor(final @Nullable MethodVisitor mv) {
				super(Opcodes.ASM5, mv);
				this.classmatcher = ClassName.of("net.minecraft.client.gui.ChatLine");
			}

			@Override
			public void visitTypeInsn(final int opcode, final @Nullable String type) {
				super.visitTypeInsn(opcode, type);
				if (opcode==Opcodes.CHECKCAST&&type!=null&&this.classmatcher.equals(ClassName.of(type))) {
					/*
					 * 203  checkcast net.minecraft.client.gui.ChatLine [137]
					 * 206  astore 10 [chatline]
					 * 208  aload 10 [chatline]
					 * 210  instanceof net.teamfruit.signpic.gui.PicChatLine [445]
					 * 213  ifeq 244
					 * 216  aload 10 [chatline]
					 * 218  checkcast net.teamfruit.signpic.gui.PicChatLine [445]
					 * 221  aload_0 [this]
					 * 222  aload_0 [this]
					 * 223  getfield net.minecraft.client.gui.GuiNewChat.mc : net.minecraft.client.Minecraft [59]
					 * 226  getfield net.minecraft.client.Minecraft.fontRenderer : net.minecraft.client.gui.FontRenderer [168]
					 * 229  iload 6 [l]
					 * 231  invokevirtual net.teamfruit.signpic.gui.PicChatLine.onClicked(net.minecraft.client.gui.GuiNewChat, net.minecraft.client.gui.FontRenderer, int) : net.minecraft.util.IChatComponent [447]
					 * 234  astore 11 [c]
					 * 236  aload 11 [c]
					 * 238  ifnull 244
					 * 241  aload 11 [c]
					 * 243  areturn
					 * 244  iconst_0
					 */
					super.visitVarInsn(Opcodes.ASTORE, 10);
					super.visitVarInsn(Opcodes.ALOAD, 10);
					super.visitTypeInsn(Opcodes.INSTANCEOF, ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatLine").getBytecodeName());
					final Label l = new Label();
					super.visitJumpInsn(Opcodes.IFEQ, l);
					super.visitVarInsn(Opcodes.ALOAD, 10);
					super.visitTypeInsn(Opcodes.CHECKCAST, ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatLine").getBytecodeName());
					super.visitVarInsn(Opcodes.ALOAD, 0);
					super.visitVarInsn(Opcodes.ILOAD, 6);
					if (CompatVersion.version().older(CompatBaseVersion.V8))
						super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatLine").getBytecodeName(), "onClicked", DescHelper.toDescMethod(ClassName.of("net.minecraft.util.IChatComponent"), ClassName.of("net.minecraft.client.gui.GuiNewChat"), int.class), false);
					else
						super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatLine").getBytecodeName(), "onClicked", DescHelper.toDescMethod(ClassName.of("net.minecraft.util.text.ITextComponent"), ClassName.of("net.minecraft.client.gui.GuiNewChat"), int.class), false);
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
			visitField(Opcodes.ACC_PUBLIC|Opcodes.ACC_FINAL, "hook", DescHelper.toDesc(ClassName.of("net.teamfruit.signpic.render.CustomChatRender$PicChatHook")), null, null);
			this.initmatcher = new MethodMatcher(ClassName.of(obfClassName), DescHelper.toDescMethod(void.class, ClassName.of("net.minecraft.client.Minecraft")), RefName.name("<init>"));
			this.drawchatmatcher = new MethodMatcher(ClassName.of(obfClassName), DescHelper.toDescMethod(void.class, int.class), ASMDeobfNames.GuiNewChatDrawChat);
			this.getchatcomponentmatcher = new MethodMatcher(ClassName.of(obfClassName), DescHelper.toDescMethod(ClassName.of("net.minecraft.util.IChatComponent"), int.class, int.class), ASMDeobfNames.GuiNewChatGetChatComponent);
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
}
