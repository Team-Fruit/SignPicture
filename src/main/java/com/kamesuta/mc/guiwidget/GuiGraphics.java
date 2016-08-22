package com.kamesuta.mc.guiwidget;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiGraphics extends Gui {
	public static final ResourceLocation guiTex = new ResourceLocation("textures/gui/widgets.png");

	private final Minecraft mc;
	public final TextureManager renderEngine;
	public final FontRenderer fontRenderer;

	public GuiGraphics() {
		this.mc = Minecraft.getMinecraft();
		this.renderEngine = this.mc.renderEngine;
		this.fontRenderer = this.mc.fontRenderer;
	}

	public void drawString(final String text, final int x, final int y, final int colour, final boolean shadow) {
		if (shadow)
			this.fontRenderer.drawStringWithShadow(text, x, y, colour);
		else
			this.fontRenderer.drawString(text, x, y, colour);
	}

	public void drawString(final String text, final int x, final int y, final int colour) {
		drawString(text, x, y, colour, true);
	}

	public void drawStringC(final String text, final int x, final int y, final int w, final int h, final int colour, final boolean shadow) {
		drawString(text, x + (w - getStringWidth(text)) / 2, y + (h - 8) / 2, colour, shadow);
	}

	public void drawStringC(final String text, final int x, final int y, final int w, final int h, final int colour) {
		drawStringC(text, x, y, w, h, colour, true);
	}

	public void drawStringC(final String text, final int x, final int y, final int colour, final boolean shadow) {
		drawString(text, x - getStringWidth(text) / 2, y, colour, shadow);
	}

	public void drawStringC(final String text, final int x, final int y, final int colour) {
		drawStringC(text, x, y, colour, true);
	}

	public void drawStringR(final String text, final int x, final int y, final int colour, final boolean shadow) {
		drawString(text, x - getStringWidth(text), y, colour, shadow);
	}

	public void drawStringR(final String text, final int x, final int y, final int colour) {
		drawStringR(text, x, y, colour, true);
	}

	public int getStringWidth(final String s) {
		if (StringUtils.isEmpty(s))
			return 0;
		return this.fontRenderer.getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
	}
}
