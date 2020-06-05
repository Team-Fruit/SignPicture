package net.teamfruit.emojicord.asm;

import java.util.Optional;
import java.util.function.Supplier;

#if MC_12_LATER
import cpw.mods.modlauncher.Environment;
import cpw.mods.modlauncher.Launcher;
#endif
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.teamfruit.emojicord.asm.lib.ASMValidate;
import net.teamfruit.emojicord.asm.lib.ClassName;
import net.teamfruit.emojicord.asm.lib.DescHelper;
import net.teamfruit.emojicord.asm.lib.INodeTreeTransformer;
import net.teamfruit.emojicord.asm.lib.MethodMatcher;
import net.teamfruit.emojicord.asm.lib.VisitorHelper;
import net.teamfruit.emojicord.compat.CompatBaseVersion;
import net.teamfruit.emojicord.compat.CompatVersion;

public class FontRendererTransform implements INodeTreeTransformer {
	@Override
	public ClassName getClassName() {
		return ClassName.of("net.minecraft.client.gui.FontRenderer");
	}

	@Override
	public ClassNode apply(final ClassNode node) {
		final ASMValidate validator = ASMValidate.create(getSimpleName());
		validator.test("renderStringAtPos.updateEmojiContext");
		validator.test("renderStringAtPos.emoji");
		validator.test("renderGlyph.render", CompatVersion.version().newer(CompatBaseVersion.V13));
		validator.test("getStringWidth.updateEmojiContext");
		validator.test("getStringWidth.findGlyph", CompatVersion.version().newer(CompatBaseVersion.V13));
		validator.test("getCharWidth.width");
		validator.test("renderChar", !CompatVersion.version().newer(CompatBaseVersion.V13));

		{
			final MethodMatcher matcher = ((Supplier<MethodMatcher>) () -> {
				if (CompatVersion.version().newer(CompatBaseVersion.V15))
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(float.class, ClassName.of("java.lang.String"), float.class, float.class, int.class, boolean.class, ClassName.of("net.minecraft.client.renderer.Matrix4f"), ClassName.of("net.minecraft.client.renderer.IRenderTypeBuffer"), boolean.class, int.class, int.class), ASMDeobfNames.FontRendererRenderStringAtPos);
				else if (CompatVersion.version().newer(CompatBaseVersion.V13))
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(float.class, ClassName.of("java.lang.String"), float.class, float.class, int.class, boolean.class), ASMDeobfNames.FontRendererRenderStringAtPos);
				else
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(void.class, ClassName.of("java.lang.String"), boolean.class), ASMDeobfNames.FontRendererRenderStringAtPos);
			}).get();
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					/*
					 0  aload_1 [text]
					 1  invokestatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.updateEmojiContext(java.lang.String) : java.lang.String [61]
					 4  astore_1 [text]
					*/
					final InsnList insertion = new InsnList();
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 1));
					insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "updateEmojiContext", DescHelper.toDescMethod(ClassName.of("java.lang.String"), ClassName.of("java.lang.String")), false));
					insertion.add(new VarInsnNode(Opcodes.ASTORE, 1));
					method.instructions.insert(insertion);
					validator.check("renderStringAtPos.updateEmojiContext");
				}

				if (!CompatVersion.version().newer(CompatBaseVersion.V13)) {
					final MethodMatcher matcher0 = ((Supplier<MethodMatcher>) () -> {
						if (CompatVersion.version().older(CompatBaseVersion.V7))
							return new MethodMatcher(getClassName(), DescHelper.toDescMethod(float.class, int.class, char.class, boolean.class), ASMDeobfNames.FontRendererRenderCharAtPos);
						else
							return new MethodMatcher(getClassName(), DescHelper.toDescMethod(float.class, char.class, boolean.class), ASMDeobfNames.FontRendererRenderChar);
					}).get();
					VisitorHelper.stream(method.instructions).filter(matcher0.insnMatcher()).findFirst().ifPresent(marker -> {
						{
							/*
							 446  iload_3 [charIndex]
							 447  putstatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.index : int [181]
							 450  iload_2 [shadow]
							 451  putstatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.shadow : boolean [183]

							 466  iconst_0
							 467  putstatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.shadow : boolean [183]
							*/
							{
								final InsnList insertion = new InsnList();

								insertion.add(new VarInsnNode(Opcodes.ILOAD, 3));
								insertion.add(new FieldInsnNode(Opcodes.PUTSTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "index", DescHelper.toDesc(int.class)));
								insertion.add(new VarInsnNode(Opcodes.ILOAD, 2));
								insertion.add(new FieldInsnNode(Opcodes.PUTSTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "shadow", DescHelper.toDesc(boolean.class)));

								method.instructions.insertBefore(marker, insertion);
							}
							{
								final InsnList insertion = new InsnList();

								insertion.add(new InsnNode(Opcodes.ICONST_0));
								insertion.add(new FieldInsnNode(Opcodes.PUTSTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "shadow", DescHelper.toDesc(boolean.class)));

								method.instructions.insert(marker, insertion);
							}
							validator.check("renderStringAtPos.emoji");
						}
					});
				} else {
					/*
					 IGlyph iglyph = this.font.findGlyph(c0);
					 TexturedGlyph texturedglyph = flag && c0 != ' ' ? this.font.obfuscate(iglyph) : this.font.getGlyph(c0);
					+ EmojiFontRenderer.EmojiGlyph emojiGlyph = EmojiFontRenderer.getEmojiGlyph(c0, i);
					+ if (emojiGlyph != null) {
					+    iglyph = emojiGlyph;
					+    texturedglyph = emojiGlyph.getTexturedGlyph();
					+ }
					if (!(texturedglyph instanceof EmptyGlyph)) {
					   float f9 = flag1 ? iglyph.getBoldOffset() : 0.0F;
					 */
					int o = CompatVersion.version().newer(CompatBaseVersion.V15) ? 3 : 0;
					final MethodMatcher matcher0 = new MethodMatcher(ClassName.of("net.minecraft.client.gui.fonts.Font"), DescHelper.toDescMethod(ClassName.of("net.minecraft.client.gui.fonts.IGlyph"), char.class), ASMDeobfNames.FontFindGlyph);
					final MethodMatcher matcher0Optifine = new MethodMatcher(getClassName(), DescHelper.toDescMethod(ClassName.of("net.minecraft.client.gui.fonts.IGlyph"), char.class), ASMDeobfNames.OptifineFontRendererFindGlyph);
					final Optional<AbstractInsnNode> marker0 = VisitorHelper.stream(method.instructions).filter(matcher0.insnMatcher().or(matcher0Optifine.insnMatcher())).findFirst();
					final Optional<AbstractInsnNode> marker1 = VisitorHelper.stream(method.instructions).filter(e -> {
						return e instanceof VarInsnNode&&e.getOpcode()==Opcodes.ASTORE&&((VarInsnNode) e).var==26+o;
					}).findFirst();
					if (marker0.isPresent()&&marker1.isPresent()) {
						/*
						 367  iload 24 [character]
						 369  iload 23 [index]
						 371  invokestatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.getEmojiGlyph(char, int) : net.teamfruit.emojicord.emoji.EmojiFontRenderer$EmojiGlyph [208]
						 374  astore 27 [emojiGlyph]

						 376  aload 27 [emojiGlyph]
						 378  ifnull 392
						 381  aload 27 [emojiGlyph]
						 383  astore 25 [glyph]
						 385  aload 27 [emojiGlyph]
						 387  astore 26 [texturedglyph]
						 389  goto 438

						 392  aload_0 [this]
						 393  getfield net.minecraft.client.gui.FontRenderer.font : net.minecraft.client.gui.fonts.Font [45]
						 396  iload 24 [character]
						 398  invokevirtual net.minecraft.client.gui.fonts.Font.findGlyph(char) : net.minecraft.client.gui.fonts.IGlyph [214]
						 401  astore 25 [glyph]
						 403  iload 17 [obfuscated]
						 405  ifeq 427
						 408  iload 24 [character]
						 410  bipush 32
						 412  if_icmpeq 427
						 415  aload_0 [this]
						 416  getfield net.minecraft.client.gui.FontRenderer.font : net.minecraft.client.gui.fonts.Font [45]
						 419  aload 25 [glyph]
						 421  invokevirtual net.minecraft.client.gui.fonts.Font.obfuscate(net.minecraft.client.gui.fonts.IGlyph) : net.minecraft.client.gui.fonts.TexturedGlyph [218]
						 424  goto 436
						 427  aload_0 [this]
						 428  getfield net.minecraft.client.gui.FontRenderer.font : net.minecraft.client.gui.fonts.Font [45]
						 431  iload 24 [character]
						 433  invokevirtual net.minecraft.client.gui.fonts.Font.getGlyph(char) : net.minecraft.client.gui.fonts.TexturedGlyph [222]
						 436  astore 26 [texturedglyph]

						 438 -
						*/
						final LabelNode label0 = new LabelNode();
						{
							final AbstractInsnNode marker2 = marker0.get().getPrevious().getPrevious().getPrevious();

							final InsnList insertion = new InsnList();

							final int index = 40;

							insertion.add(new VarInsnNode(Opcodes.ILOAD, 24+o));
							insertion.add(new VarInsnNode(Opcodes.ILOAD, 23+o));
							insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "getEmojiGlyph", DescHelper.toDescMethod(ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer$EmojiGlyph"), char.class, int.class), false));
							insertion.add(new VarInsnNode(Opcodes.ASTORE, index));

							insertion.add(new VarInsnNode(Opcodes.ALOAD, index));
							final LabelNode label1 = new LabelNode();
							insertion.add(new JumpInsnNode(Opcodes.IFNULL, label1));
							insertion.add(new VarInsnNode(Opcodes.ALOAD, index));
							insertion.add(new VarInsnNode(Opcodes.ASTORE, 25+o));
							insertion.add(new VarInsnNode(Opcodes.ALOAD, index));
							insertion.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer$EmojiGlyph").getBytecodeName(), "getTexturedGlyph", DescHelper.toDescMethod(ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer$EmojiTexturedGlyph")), false));
							insertion.add(new VarInsnNode(Opcodes.ASTORE, 26+o));
							insertion.add(new JumpInsnNode(Opcodes.GOTO, label0));

							insertion.add(label1);

							method.instructions.insertBefore(marker2, insertion);
						}
						{
							final AbstractInsnNode marker3 = marker1.get();

							final InsnList insertion = new InsnList();

							insertion.add(label0);

							method.instructions.insert(marker3, insertion);
						}
						validator.check("renderStringAtPos.emoji");
					}
				}
			});
		}
		if (CompatVersion.version().newer(CompatBaseVersion.V13)) {
			final MethodMatcher matcher = ((Supplier<MethodMatcher>) () -> {
				if (CompatVersion.version().newer(CompatBaseVersion.V15))
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(void.class, ClassName.of("net.minecraft.client.gui.fonts.TexturedGlyph"), boolean.class, boolean.class, float.class, float.class, float.class, ClassName.of("net.minecraft.client.renderer.Matrix4f"), ClassName.of("com.mojang.blaze3d.vertex.IVertexBuilder"), float.class, float.class, float.class, float.class, int.class), ASMDeobfNames.FontRendererRenderGlyph);
				else
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(void.class, ClassName.of("net.minecraft.client.gui.fonts.TexturedGlyph"), boolean.class, boolean.class, float.class, float.class, float.class, ClassName.of("net.minecraft.client.renderer.BufferBuilder"), float.class, float.class, float.class, float.class), ASMDeobfNames.FontRendererRenderGlyph);
			}).get();
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					final MethodMatcher matcher0 = ((Supplier<MethodMatcher>) () -> {
						if (CompatVersion.version().newer(CompatBaseVersion.V15))
							return new MethodMatcher(ClassName.of("net.minecraft.client.gui.fonts.TexturedGlyph"), DescHelper.toDescMethod(void.class, boolean.class, float.class, float.class, ClassName.of("net.minecraft.client.renderer.Matrix4f"), ClassName.of("com.mojang.blaze3d.vertex.IVertexBuilder"), float.class, float.class, float.class, float.class, int.class), ASMDeobfNames.TexturedGlyphRender);
						else
							return new MethodMatcher(ClassName.of("net.minecraft.client.gui.fonts.TexturedGlyph"), DescHelper.toDescMethod(void.class, ClassName.of("net.minecraft.client.renderer.texture.TextureManager"), boolean.class, float.class, float.class, ClassName.of("net.minecraft.client.renderer.BufferBuilder"), float.class, float.class, float.class, float.class), ASMDeobfNames.TexturedGlyphRender);
					}).get();
					VisitorHelper.stream(method.instructions).filter(matcher0.insnMatcher()).skip(1).findFirst().ifPresent(marker -> {
						/*
						 27  iconst_1
						 28  putstatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.shadow : boolean [351]
						
						 57  iconst_0
						 58  putstatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.shadow : boolean [351]
						*/
						{
							final InsnList insertion = new InsnList();

							insertion.add(new InsnNode(Opcodes.ICONST_1));
							insertion.add(new FieldInsnNode(Opcodes.PUTSTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "shadow", DescHelper.toDesc(boolean.class)));

							method.instructions.insertBefore(marker, insertion);
						}
						{
							final InsnList insertion = new InsnList();

							insertion.add(new InsnNode(Opcodes.ICONST_0));
							insertion.add(new FieldInsnNode(Opcodes.PUTSTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "shadow", DescHelper.toDesc(boolean.class)));

							method.instructions.insert(marker, insertion);
						}
						validator.check("renderGlyph.render");
					});
				}
			});
		}
		{
			final MethodMatcher matcher = new MethodMatcher(getClassName(), DescHelper.toDescMethod(int.class, ClassName.of("java.lang.String")), ASMDeobfNames.FontRendererGetStringWidth);
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					/*
					 0  aload_1 [text]
					 1  invokestatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.updateEmojiContext(java.lang.String) : java.lang.String [61]
					 4  astore_1 [text]
					*/
					final InsnList insertion = new InsnList();
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 1));
					insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "updateEmojiContext", DescHelper.toDescMethod(ClassName.of("java.lang.String"), ClassName.of("java.lang.String")), false));
					insertion.add(new VarInsnNode(Opcodes.ASTORE, 1));
					method.instructions.insert(insertion);
					validator.check("getStringWidth.updateEmojiContext");
				}

				if (CompatVersion.version().newer(CompatBaseVersion.V13)) {
					final MethodMatcher matcher0 = new MethodMatcher(ClassName.of("net.minecraft.client.gui.fonts.Font"), DescHelper.toDescMethod(ClassName.of("net.minecraft.client.gui.fonts.IGlyph"), char.class), ASMDeobfNames.FontFindGlyph);
					VisitorHelper.stream(method.instructions).filter(matcher0.insnMatcher()).findFirst().ifPresent(marker0 -> {
						{
							/*
							 *88  fload_2 [width]
							
							  89  aload_0 [this]
							  90  getfield net.minecraft.client.gui.FontRenderer.font : net.minecraft.client.gui.fonts.Font [45]
							  93  iload 5 [character]
							  95  invokevirtual net.minecraft.client.gui.fonts.Font.findGlyph(char) : net.minecraft.client.gui.fonts.IGlyph [214]
							  98  iload_3 [bold]
							  99  invokeinterface net.minecraft.client.gui.fonts.IGlyph.getAdvance(boolean) : float [254] [nargs: 2]
							 104  fadd
							 105  fstore_2 [width]
							
							  ↓
							
							  88  iload 5 [character]
							  90  iload 4 [index]
							  92  invokestatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.getEmojiGlyph(char, int) : net.teamfruit.emojicord.emoji.EmojiFontRenderer$EmojiGlyph [208]
							  95  astore 6 [emojiGlyph]
							 *97  fload_2 [width]
							  98  aload 6 [emojiGlyph]
							 100  ifnull 108
							 103  aload 6 [emojiGlyph]
							 105  goto 117
							
							 108  aload_0 [this]
							 109  getfield net.minecraft.client.gui.FontRenderer.font : net.minecraft.client.gui.fonts.Font [45]
							 112  iload 5 [character]
							*114  invokevirtual net.minecraft.client.gui.fonts.Font.findGlyph(char) : net.minecraft.client.gui.fonts.IGlyph [214]
							 117  iload_3 [bold]
							 118  invokeinterface net.minecraft.client.gui.fonts.IGlyph.getAdvance(boolean) : float [254] [nargs: 2]
							 123  fadd
							 124  fstore_2 [width]
							*/
							final AbstractInsnNode marker1 = marker0.getPrevious().getPrevious().getPrevious().getPrevious();
							final LabelNode label0 = new LabelNode();
							final LabelNode label1 = new LabelNode();
							{
								final InsnList insertion = new InsnList();

								insertion.add(new VarInsnNode(Opcodes.ILOAD, 5));
								insertion.add(new VarInsnNode(Opcodes.ILOAD, 4));
								insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "getEmojiGlyph", DescHelper.toDescMethod(ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer$EmojiGlyph"), char.class, int.class), false));
								insertion.add(new VarInsnNode(Opcodes.ASTORE, 6));

								method.instructions.insertBefore(marker1, insertion);
							}
							{
								final InsnList insertion = new InsnList();

								insertion.add(new VarInsnNode(Opcodes.ALOAD, 6));
								insertion.add(new JumpInsnNode(Opcodes.IFNULL, label0));
								insertion.add(new VarInsnNode(Opcodes.ALOAD, 6));
								insertion.add(new JumpInsnNode(Opcodes.GOTO, label1));
								insertion.add(label0);

								method.instructions.insert(marker1, insertion);
							}
							{
								final InsnList insertion = new InsnList();

								insertion.add(label1);

								method.instructions.insert(marker0, insertion);
							}
							validator.check("getStringWidth.findGlyph");
						}
					});
				}
			});
		}
		{
			final MethodMatcher matcher = ((Supplier<MethodMatcher>) () -> {
				if (!CompatVersion.version().newer(CompatBaseVersion.V13))
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(int.class, char.class), ASMDeobfNames.FontRendererGetCharWidth);
				else
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(float.class, char.class), ASMDeobfNames.FontRendererGetCharWidth);
			}).get();
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					/*
					 0  iload_1 [character]
					 1  bipush 63
					 3  if_icmpne 9
					 6  bipush 10
					 8  ireturn
					 9  -
					*/
					final InsnList insertion = new InsnList();
					insertion.add(new VarInsnNode(Opcodes.ILOAD, 1));
					insertion.add(new IntInsnNode(Opcodes.BIPUSH, '\u0000'));
					final LabelNode label = new LabelNode();
					insertion.add(new JumpInsnNode(Opcodes.IF_ICMPNE, label));
					if (!CompatVersion.version().newer(CompatBaseVersion.V13)) {
						insertion.add(new IntInsnNode(Opcodes.BIPUSH, 10));
						insertion.add(new InsnNode(Opcodes.IRETURN));
					} else {
						insertion.add(new LdcInsnNode(10f));
						insertion.add(new InsnNode(Opcodes.FRETURN));
					}
					insertion.add(label);
					method.instructions.insert(insertion);
					validator.check("getCharWidth.width");
				}
			});
		}
		if (!CompatVersion.version().newer(CompatBaseVersion.V13)) {
			final MethodMatcher matcher = ((Supplier<MethodMatcher>) () -> {
				if (CompatVersion.version().older(CompatBaseVersion.V7))
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(float.class, int.class, char.class, boolean.class), ASMDeobfNames.FontRendererRenderCharAtPos);
				else
					return new MethodMatcher(getClassName(), DescHelper.toDescMethod(float.class, char.class, boolean.class), ASMDeobfNames.FontRendererRenderChar);
			}).get();
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					/*
					 0  iload_1 [c]
					 1  iload_2 [italic]
					 2  aload_0 [fontRenderer]
					 3  getfield net.minecraft.client.gui.FontRenderer.posX : float [49]
					 6  aload_0 [fontRenderer]
					 7  getfield net.minecraft.client.gui.FontRenderer.posY : float [55]
					10  aload_0 [fontRenderer]
					11  getfield net.minecraft.client.gui.FontRenderer.red : float [58]
					14  aload_0 [fontRenderer]
					15  getfield net.minecraft.client.gui.FontRenderer.green : float [61]
					18  aload_0 [fontRenderer]
					19  getfield net.minecraft.client.gui.FontRenderer.blue : float [64]
					22  aload_0 [fontRenderer]
					23  getfield net.minecraft.client.gui.FontRenderer.alpha : float [67]
					26  invokestatic net.teamfruit.emojicord.emoji.EmojiFontRenderer.renderEmojiChar(char, boolean, float, float, float, float, float, float) : boolean [70]
					 6  ifeq 12
					 9  ldc <Float 10.0> [21]
					11  freturn
					12  -
					*/
					final InsnList insertion = new InsnList();
					if (CompatVersion.version().older(CompatBaseVersion.V7)) {
						insertion.add(new VarInsnNode(Opcodes.ILOAD, 2));
						insertion.add(new VarInsnNode(Opcodes.ILOAD, 3));
					} else {
						insertion.add(new VarInsnNode(Opcodes.ILOAD, 1));
						insertion.add(new VarInsnNode(Opcodes.ILOAD, 2));
					}
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insertion.add(new FieldInsnNode(Opcodes.GETFIELD, getClassName().getBytecodeName(), ASMDeobfNames.FontRendererPosX.name(), DescHelper.toDesc(float.class)));
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insertion.add(new FieldInsnNode(Opcodes.GETFIELD, getClassName().getBytecodeName(), ASMDeobfNames.FontRendererPosY.name(), DescHelper.toDesc(float.class)));
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insertion.add(new FieldInsnNode(Opcodes.GETFIELD, getClassName().getBytecodeName(), ASMDeobfNames.FontRendererRed.name(), DescHelper.toDesc(float.class)));
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insertion.add(new FieldInsnNode(Opcodes.GETFIELD, getClassName().getBytecodeName(), ASMDeobfNames.FontRendererGreen.name(), DescHelper.toDesc(float.class)));
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insertion.add(new FieldInsnNode(Opcodes.GETFIELD, getClassName().getBytecodeName(), ASMDeobfNames.FontRendererBlue.name(), DescHelper.toDesc(float.class)));
					insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insertion.add(new FieldInsnNode(Opcodes.GETFIELD, getClassName().getBytecodeName(), ASMDeobfNames.FontRendererAlpha.name(), DescHelper.toDesc(float.class)));
					insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emojicord.emoji.EmojiFontRenderer").getBytecodeName(), "renderEmojiChar", DescHelper.toDescMethod(boolean.class, char.class, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class), false));
					final LabelNode label = new LabelNode();
					insertion.add(new JumpInsnNode(Opcodes.IFEQ, label));
					insertion.add(new LdcInsnNode(10.0F));
					insertion.add(new InsnNode(Opcodes.FRETURN));
					insertion.add(label);
					method.instructions.insert(insertion);
					validator.check("renderChar");
				}
			});
		}

		validator.validate();
		return node;
	}
}