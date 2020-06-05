package net.teamfruit.emojicord.asm;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.teamfruit.emojicord.asm.lib.RefName;
import net.teamfruit.emojicord.compat.CompatBaseVersion;
import net.teamfruit.emojicord.compat.CompatVersion;

public class ASMDeobfNames {
	public static final @Nonnull RefName GuiScreenSendChatMessage = RefName.deobName("sendChatMessage", "func_175281_b");
	public static final @Nonnull RefName GuiScreenHandleInput = RefName.deobName("handleInput", "func_146269_k");
	public static final @Nonnull RefName GuiScreenHandleMouseInput = RefName.deobName("handleMouseInput", "func_146274_d");
	public static final @Nonnull RefName GuiScreenHandleKeyboardInput = RefName.deobName("handleKeyboardInput", "func_146282_l");
	public static final @Nonnull RefName GuiChatSendChatMessage = RefName.deobName("func_146403_a", "func_146403_a");
	public static final @Nonnull RefName GuiTextFieldFontRenderer = ((Supplier<RefName>) () -> {
		if (CompatVersion.version().older(CompatBaseVersion.V7))
			return RefName.deobName("field_146211_a", "field_146211_a");
		else if (CompatVersion.version().older(CompatBaseVersion.V11))
			return RefName.deobName("fontRendererInstance", "field_146211_a");
		else
			return RefName.deobName("fontRenderer", "field_146211_a");
	}).get();
	public static final @Nonnull RefName GuiTextFieldDrawTextBox = RefName.deobName("drawTextBox", "func_146194_f");
	public static final @Nonnull RefName GuiTextFieldDrawTextField = ((Supplier<RefName>) () -> {
		if (CompatVersion.version().older(CompatBaseVersion.V13))
			return RefName.deobName("drawTextField", "func_195608_a");
		else
			return RefName.deobName("renderButton", "renderButton");
	}).get();
	public static final @Nonnull RefName FontRendererDrawStringWithShadow = ((Supplier<RefName>) () -> {
		if (CompatVersion.version().older(CompatBaseVersion.V7))
			return RefName.deobName("drawStringWithShadow", "func_78261_a");
		else
			return RefName.deobName("drawStringWithShadow", "func_175063_a");
	}).get();
	public static final @Nonnull RefName FontRendererRenderStringAtPos = ((Supplier<RefName>) () -> {
		if (CompatVersion.version().newer(CompatBaseVersion.V15))
			return RefName.deobName("renderStringAtPos", "func_228081_c_");
		else if (CompatVersion.version().newer(CompatBaseVersion.V13))
			return RefName.deobName("renderStringAtPos", "func_211843_b");
		else
			return RefName.deobName("renderStringAtPos", "func_78255_a");
	}).get();
	public static final @Nonnull RefName FontRendererRenderChar = RefName.deobName("renderChar", "func_181559_a");
	public static final @Nonnull RefName FontRendererRenderCharAtPos = RefName.deobName("renderCharAtPos", "func_78278_a");
	public static final @Nonnull RefName FontRendererRenderGlyph = ((Supplier<RefName>) () -> {
		if (CompatVersion.version().newer(CompatBaseVersion.V15))
			return RefName.deobName("drawGlyph", "func_228077_a_");
		else if (CompatVersion.version().older(CompatBaseVersion.V13))
			return RefName.deobName("func_212452_a", "func_212452_a");
		else
			return RefName.deobName("renderGlyph", "func_212452_a");
	}).get();
	public static final @Nonnull RefName FontRendererGetStringWidth = RefName.deobName("getStringWidth", "func_78256_a");
	public static final @Nonnull RefName FontRendererGetCharWidth = ((Supplier<RefName>) () -> {
		if (!CompatVersion.version().newer(CompatBaseVersion.V13))
			return RefName.deobName("getCharWidth", "func_78263_a");
		else
			return RefName.deobName("getCharWidth", "func_211125_a");
	}).get();
	public static final @Nonnull RefName FontRendererPosX = RefName.deobName("posX", "field_78295_j");
	public static final @Nonnull RefName FontRendererPosY = RefName.deobName("posY", "field_78296_k");
	public static final @Nonnull RefName FontRendererRed = RefName.deobName("red", "field_78291_n");
	public static final @Nonnull RefName FontRendererGreen = RefName.deobName("green", "field_78306_p");
	public static final @Nonnull RefName FontRendererBlue = RefName.deobName("blue", "field_78292_o");
	public static final @Nonnull RefName FontRendererAlpha = RefName.deobName("alpha", "field_78305_q");
	public static final @Nonnull RefName FontFindGlyph = RefName.deobName("findGlyph", "func_211184_b");
	public static final @Nonnull RefName OptifineFontRendererFindGlyph = RefName.name("findGlyph");
	public static final @Nonnull RefName TexturedGlyphRender = RefName.deobName("render", "func_211234_a");
}
