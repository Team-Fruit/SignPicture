package com.kamesuta.mc.signpic.asm;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.asm.lib.RefName;
import com.kamesuta.mc.signpic.compat.Compat.ASMCompatVersion;

public class ASMDeobfNames {
	public static final @Nonnull RefName GuiNewChatDrawnChatLines = ((Supplier<RefName>) () -> {
		switch (ASMCompatVersion.version()) {
			case V7:
				return RefName.deobName("field_146253_i", "field_146253_i");
			default:
				return RefName.deobName("drawnChatLines", "field_146253_i");
		}
	}).get();
	public static final @Nonnull RefName GuiScreenHandleInput = RefName.deobName("handleInput", "func_146269_k");
	public static final @Nonnull RefName FontRendererDrawStringWithShadow = ((Supplier<RefName>) () -> {
		switch (ASMCompatVersion.version()) {
			case V7:
				return RefName.deobName("drawStringWithShadow", "func_78261_a");
			default:
				return RefName.deobName("drawStringWithShadow", "func_175063_a");
		}
	}).get();
	public static final @Nonnull RefName FontRendererDrawSplitString = RefName.deobName("drawSplitString", "func_78279_b");
	public static final @Nonnull RefName GuiNewChatDrawChat = RefName.deobName("drawChat", "func_146230_a");
	public static final @Nonnull RefName GuiNewChatGetChatComponent = ((Supplier<RefName>) () -> {
		switch (ASMCompatVersion.version()) {
			case V7:
				return RefName.deobName("func_146236_a", "func_146236_a");
			default:
				return RefName.deobName("getChatComponent", "func_146236_a");
		}
	}).get();
	public static final @Nonnull RefName GuiScreenBookDrawScreen = RefName.deobName("drawScreen", "func_73863_a");
	public static final @Nonnull RefName GuiScreenBookPageInsertIntoCurrent = ((Supplier<RefName>) () -> {
		switch (ASMCompatVersion.version()) {
			case V7:
				return RefName.deobName("func_146459_b", "func_146459_b");
			default:
				return RefName.deobName("pageInsertIntoCurrent", "func_146459_b");
		}
	}).get();
}
