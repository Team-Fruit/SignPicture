package com.kamesuta.mc.guiwidget;

import com.kamesuta.mc.guiwidget.position.Point;

import net.minecraft.client.gui.Gui;

public class GuiComponent extends Gui implements GuiCommon {
	@Override
	public void init(final GuiEvent ev, final GuiPosition pgp) {}

	@Override
	public void draw(final GuiEvent ev, final GuiPosition pgp, final Point p, final float frame) {}

	@Override
	public void update(final GuiEvent ev, final GuiPosition pgp, final Point p) {}

	@Override
	public void keyTyped(final GuiEvent ev, final GuiPosition pgp, final Point p, final char c, final int keycode) {}

	@Override
	public void mouseScrolled(final GuiEvent ev, final GuiPosition pgp, final Point p, final int scroll) {}

	@Override
	public void mouseMoved(final GuiEvent ev, final GuiPosition pgp, final Point p, final int button) {}

	@Override
	public void mouseClicked(final GuiEvent ev, final GuiPosition pgp, final Point p, final int button) {}

	@Override
	public void mouseDragged(final GuiEvent ev, final GuiPosition pgp, final Point p, final int button, final long time) {}

	@Override
	public void mouseReleased(final GuiEvent ev, final GuiPosition pgp, final Point mouse, final int button) {}
}
