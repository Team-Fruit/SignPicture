package com.kamesuta.mc.bnnwidget;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class WFrame extends GuiScreen implements WContainer<WCommon> {
	protected final ArrayList<WCommon> widgets = new ArrayList<WCommon>();
	protected final WEvent event = new WEvent();
	public float width;
	public float height;

	public WFrame setWidth(final float width) {
		this.width = width;
		super.width = (int) width;
		return this;
	}

	public WFrame setHeight(final float height) {
		this.height = height;
		super.height = (int) height;
		return this;
	}

	public float width() {
		if (super.width != (int) this.width)
			this.width = super.width;
		return this.width;
	}

	public float height() {
		if (super.height != (int) this.height)
			this.height = super.height;
		return this.height;
	}

	public WFrame() {
	}

	public Area getAbsolute() {
		return new Area(0, 0, width(), height());
	}

	public Point getMouseAbsolute() {
		return new Point(Mouse.getX() * width() / this.mc.displayWidth,
				height() - Mouse.getY() * height() / this.mc.displayHeight - 1);
	}

	@Override
	public List<WCommon> getContainer() {
		return this.widgets;
	}

	@Override
	public boolean add(final WCommon widget) {
		return this.widgets.add(widget);
	}

	@Override
	public boolean remove(final WCommon widget) {
		return this.widgets.remove(widget);
	}

	@Override
	public void initGui() {
		sInitGui();
	}

	protected void sInitGui() {
		super.initGui();
	}

	protected void initWidget() {
		for (final WCommon widget : this.widgets)
			widget.onAdded();
	}

	protected void init() {
	}

	public void reset() {
		this.widgets.clear();
		initGui();
		init();
		initWidget();
	}

	@Override
	public void setWorldAndResolution(final Minecraft mc, final int i, final int j) {
		final boolean init = this.mc == null;
		sSetWorldAndResolution(mc, i, j);
		if (init) {
			init();
			initWidget();
		}
	}

	protected void sSetWorldAndResolution(final Minecraft mc, final int i, final int j) {
		super.setWorldAndResolution(mc, i, j);
	}

	public void drawScreen(final int mousex, final int mousey, final float f, final float opacity) {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		for (final WCommon widget : this.widgets)
			widget.draw(this.event, gp, p, f, opacity);
		sDrawScreen(mousex, mousey, f);
	}

	@Override
	public void drawScreen(final int mousex, final int mousey, final float f) {
		drawScreen(mousex, mousey, f, getOpacity());
	}

	protected float getOpacity() {
		return 1f;
	}

	protected void sDrawScreen(final int mousex, final int mousey, final float f) {
		super.drawScreen(mousex, mousey, f);
	}

	protected int mousebutton;

	@Override
	protected void mouseClicked(final int x, final int y, final int button) {
		this.mousebutton = button;
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		for (final WCommon widget : this.widgets)
			widget.mouseClicked(this.event, gp, p, button);
		sMouseClicked(x, y, button);
	}

	protected void sMouseClicked(final int x, final int y, final int button) {
		super.mouseClicked(x, y, button);
	}

	@Override
	protected void mouseMovedOrUp(final int x, final int y, final int button) {
		sMouseMovedOrUp(x, y, button);
	}

	protected void sMouseMovedOrUp(final int x, final int y, final int button) {
		super.mouseMovedOrUp(x, y, button);
	}

	@Override
	protected void mouseClickMove(final int x, final int y, final int button, final long time) {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		for (final WCommon widget : this.widgets)
			widget.mouseDragged(this.event, gp, p, button, time);
		sMouseClickMove(x, y, button, time);
	}

	protected void sMouseClickMove(final int x, final int y, final int button, final long time) {
		super.mouseClickMove(x, y, button, time);
	}

	protected Point mouselast;
	@Override
	public void updateScreen() {
		final Point p = getMouseAbsolute();
		if (this.mc.currentScreen == this) {
			final Area gp = getAbsolute();
			for (final WCommon widget : this.widgets)
				widget.update(this.event, gp, p);
		}
		final int m = Mouse.getEventButton();
		if (this.mousebutton != m) {
			final Area gp = getAbsolute();
			for (final WCommon widget : this.widgets)
				widget.mouseReleased(this.event, gp, p, this.mousebutton);
			if (m >= 0)
				this.mousebutton = m;
		}
		if (!p.equals(this.mouselast)) {
			this.mouselast = p;
			final Area gp = getAbsolute();
			for (final WCommon widget : this.widgets)
				widget.mouseMoved(this.event, gp, p, this.mousebutton);
		}
		if (this.closeRequest) {
			onCloseRequest();
		}
		sUpdateScreen();
	}

	protected void sUpdateScreen() {
		super.updateScreen();
	}

	@Override
	protected void keyTyped(final char c, final int keycode) {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		for (final WCommon widget : this.widgets)
			widget.keyTyped(this.event, gp, p, c, keycode);
		sKeyTyped(c, keycode);
	}

	protected void sKeyTyped(final char c, final int keycode) {
		if (keycode == Keyboard.KEY_ESCAPE) {
			requestClose();
		}
	}

	protected boolean closeRequest;
	protected void onCloseRequest() {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		boolean closable = true;
		for (final WCommon widget : this.widgets)
			closable = closable && widget.onClosing(this.event, gp, p);
		if (closable)
			close();
	}

	protected void close() {
		this.mc.displayGuiScreen((GuiScreen)null);
		this.mc.setIngameFocus();
	}

	public void requestClose() {
		for (final WCommon widget : this.widgets)
			widget.onCloseRequest();
		this.closeRequest = true;
	}

	protected void cancelCloseRequest() {
		this.closeRequest = false;
	}

	@Override
	public void handleMouseInput() {
		final int i = Mouse.getEventDWheel();
		if (i != 0) {
			final Area gp = getAbsolute();
			final Point p = getMouseAbsolute();
			for (final WCommon widget : this.widgets)
				widget.mouseScrolled(this.event, gp, p, i);
		}
		sHandleMouseInput();
	}

	protected void sHandleMouseInput() {
		super.handleMouseInput();
	}
}
