package com.kamesuta.mc.guiwidget;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.IPositionRelative;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.PositionAbsolute;
import com.kamesuta.mc.guiwidget.position.RelativePosition;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiFrame extends GuiScreen implements GuiContainer {
	protected final ArrayList<GuiCommon> widgets = new ArrayList<GuiCommon>();
	protected final IPositionRelative position = new RelativePosition(0, 0, -1, -1);

	protected final GuiTools tools = new GuiTools();

	public GuiFrame() {
	}

	@Override
	public void initGui() {
		super.initGui();
		final GuiPosition gp = new GuiPosition(null, this.position, getAbsolute());
		for (final GuiCommon widget : this.widgets)
			widget.init(this.tools, gp);
	}

	public void reset() {
		this.widgets.clear();
		initGui();
		initWidgets();
	}

	@Override
	public void setWorldAndResolution(final Minecraft mc, final int i, final int j) {
		final boolean init = this.mc == null;
		super.setWorldAndResolution(mc, i, j);
		if (init) {
			initWidgets();
		}
	}

	@Override
	public List<GuiCommon> getContainer() {
		return this.widgets;
	}

	@Override
	public boolean add(final GuiComponent widget) {
		return this.widgets.add(widget);
	}

	@Override
	public boolean remove(final GuiComponent widget) {
		return this.widgets.remove(widget);
	}

	@Override
	public void drawScreen(final int mousex, final int mousey, final float f) {
		final GuiPosition gp = new GuiPosition(null, this.position, getAbsolute());
		final Point p = getAbsoluteMousePosition();
		for (final GuiCommon widget : this.widgets)
			widget.draw(this.tools, gp, p, f);
	}

	@Override
	protected void mouseClicked(final int x, final int y, final int button) {
		super.mouseClicked(x, y, button);
		final GuiPosition gp = new GuiPosition(null, this.position, getAbsolute());
		final Point p = getAbsoluteMousePosition();
		for (final GuiCommon widget : this.widgets)
			widget.mouseClicked(this.tools, gp, p, button);
	}

	@Override
	protected void mouseMovedOrUp(final int x, final int y, final int button) {
		super.mouseMovedOrUp(x, y, button);
		final GuiPosition gp = new GuiPosition(null, this.position, getAbsolute());
		final Point p = getAbsoluteMousePosition();
		for (final GuiCommon widget : this.widgets)
			widget.mouseMovedOrUp(this.tools, gp, p, button);
	}

	@Override
	protected void mouseClickMove(final int x, final int y, final int button, final long time) {
		super.mouseClickMove(x, y, button, time);
		final GuiPosition gp = new GuiPosition(null, this.position, getAbsolute());
		final Point p = getAbsoluteMousePosition();
		for (final GuiCommon widget : this.widgets)
			widget.mouseDragged(this.tools, gp, p, button, time);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (this.mc.currentScreen == this) {
			final GuiPosition gp = new GuiPosition(null, this.position, getAbsolute());
			final Point p = getAbsoluteMousePosition();
			for (final GuiCommon widget : this.widgets) {
				widget.update(this.tools, gp, p);
			}
		}
	}

	@Override
	public void keyTyped(final char c, final int keycode) {
		super.keyTyped(c, keycode);
		final GuiPosition gp = new GuiPosition(null, this.position, getAbsolute());
		final Point p = getAbsoluteMousePosition();
		for (final GuiCommon widget : this.widgets)
			widget.keyTyped(this.tools, gp, p, c, keycode);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		final int i = Mouse.getEventDWheel();
		if (i != 0) {
			final GuiPosition gp = new GuiPosition(null, this.position, getAbsolute());
			final Point p = getAbsoluteMousePosition();
			for (final GuiCommon widget : this.widgets)
				widget.mouseScrolled(this.tools, gp, p, i);
		}
	}

	public IPositionAbsolute getAbsolute() {
		return new PositionAbsolute(0, 0, this.width, this.height);
	}

	public Point getAbsoluteMousePosition() {
		return new Point(Mouse.getX() * this.width / this.mc.displayWidth,
				this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1);
	}

	protected void initWidgets() {
	}
}
