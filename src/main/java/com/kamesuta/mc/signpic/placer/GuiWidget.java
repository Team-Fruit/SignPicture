package com.kamesuta.mc.signpic.placer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class GuiWidget extends Gui {
	protected static final ResourceLocation guiTex = new ResourceLocation("textures/gui/widgets.png");
	public GuiScreen parentScreen;
	public TextureManager renderEngine;
	public FontRenderer fontRenderer;
	public int x;
	public int y;
	public int width;
	public int height;

	public GuiWidget(final int x, final int y, final int width, final int height) {
		setSize(x, y, width, height);
	}

	public void setSize(final int x, final int y, final int width, final int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean pointInside(final int px, final int py) {
		return (px >= this.x) && (px < this.x + this.width) && (py >= this.y) && (py < this.y + this.height);
	}

	public void mouseClicked(final int x, final int y, final int button) {
	}

	public void mouseReleased(final int x, final int y, final int button) {
	}

	public void mouseDragged(final int x, final int y, final int button, final long time) {
	}

	public void update() {
	}

	public void draw(final int mousex, final int mousey, final float frame) {
	}

	public void keyTyped(final char c, final int keycode) {
	}

	public void mouseScrolled(final int x, final int y, final int scroll) {
	}

	public void onAdded(final GuiScreen s) {
		final Minecraft mc = Minecraft.getMinecraft();
		this.renderEngine = mc.renderEngine;
		this.fontRenderer = mc.fontRenderer;
	}
}
