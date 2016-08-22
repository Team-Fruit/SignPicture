package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenWidget extends GuiScreen {
	public ArrayList<GuiWidget> widgets = new ArrayList<GuiWidget>();
	public int xSize;
	public int ySize;
	public int guiTop;
	public int guiLeft;

	public GuiScreenWidget() {
		this(176, 166);
	}

	public GuiScreenWidget(final int xSize, final int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
	}

	@Override
	public void initGui() {
		this.guiTop = ((this.height - this.ySize) / 2);
		this.guiLeft = ((this.width - this.xSize) / 2);
		if (!this.widgets.isEmpty()) {
			resize();
		}
	}

	public void reset() {
		this.widgets.clear();
		initGui();
		addWidgets();
		resize();
	}

	@Override
	public void setWorldAndResolution(final Minecraft mc, final int i, final int j) {
		final boolean init = this.mc == null;
		super.setWorldAndResolution(mc, i, j);
		if (init) {
			addWidgets();
			resize();
		}
	}

	public void add(final GuiWidget widget) {
		this.widgets.add(widget);
		widget.onAdded(this);
	}

	@Override
	public void drawScreen(final int mousex, final int mousey, final float f) {
		glPushMatrix();
		glTranslatef(this.guiLeft, this.guiTop, 0.0F);
		drawBackground();
		for (final GuiWidget widget : this.widgets) {
			widget.draw(mousex - this.guiLeft, mousey - this.guiTop, f);
		}
		drawForeground();
		glPopMatrix();
	}

	public void drawBackground() {
	}

	public void drawForeground() {
	}

	@Override
	protected void mouseClicked(final int x, final int y, final int button) {
		super.mouseClicked(x, y, button);
		for (final GuiWidget widget : this.widgets) {
			widget.mouseClicked(x - this.guiLeft, y - this.guiTop, button);
		}
	}

	@Override
	protected void mouseClickMove(final int x, final int y, final int button, final long time) {
		super.mouseClickMove(x, y, button, time);
		for (final GuiWidget widget : this.widgets) {
			widget.mouseDragged(x - this.guiLeft, y - this.guiTop, button, time);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (this.mc.currentScreen == this) {
			for (final GuiWidget widget : this.widgets) {
				widget.update();
			}
		}
	}

	@Override
	public void keyTyped(final char c, final int keycode) {
		super.keyTyped(c, keycode);
		for (final GuiWidget widget : this.widgets) {
			widget.keyTyped(c, keycode);
		}
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		final int i = Mouse.getEventDWheel();
		Point p;
		int scroll;
		if (i != 0) {
			p = GuiDraw.getMousePosition();
			scroll = i > 0 ? 1 : -1;
			for (final GuiWidget widget : this.widgets) {
				widget.mouseScrolled(p.x, p.y, scroll);
			}
		}
	}

	public void actionPerformed(final String ident, final Object... params) {
	}

	public void resize() {
	}

	public void addWidgets() {
	}
}
