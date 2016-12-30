package com.kamesuta.mc.bnnwidget;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.render.OpenGL;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class WFrame extends GuiScreen implements WContainer<WCommon> {
	protected @Nullable GuiScreen parent;
	protected @Nonnull WPanel contentPane = new WPanel(new R());
	protected final @Nonnull WEvent event = new WEvent(this);
	protected boolean initialized;
	protected boolean doesPauseGui = true;
	public float width;
	public float height;

	protected int mousebutton = -1;
	protected @Nullable Point mouselast;
	protected int lastbutton = -1;
	protected boolean closeRequest;

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
		if (super.width!=(int) this.width)
			this.width = super.width;
		return this.width;
	}

	public float height() {
		if (super.height!=(int) this.height)
			this.height = super.height;
		return this.height;
	}

	public WFrame(final @Nullable GuiScreen parent) {
		this.parent = parent;
		this.mc = Client.mc;
	}

	public WFrame() {
	}

	public Area getAbsolute() {
		return new Area(0, 0, width(), height());
	}

	public Point getMouseAbsolute() {
		return new Point(Mouse.getX()*width()/this.mc.displayWidth,
				height()-Mouse.getY()*height()/this.mc.displayHeight-1);
	}

	@Override
	public List<WCommon> getContainer() {
		return getContentPane().getContainer();
	}

	@Override
	public boolean add(final WCommon widget) {
		return getContentPane().add(widget);
	}

	@Override
	public boolean remove(final WCommon widget) {
		return getContentPane().remove(widget);
	}

	public WPanel getContentPane() {
		return this.contentPane;
	}

	public void setContentPane(final WPanel panel) {
		this.contentPane = panel;
	}

	@Override
	public void initGui() {
		sInitGui();
	}

	protected void sInitGui() {
		if (this.parent!=null)
			this.parent.initGui();
		super.initGui();
		if (!this.initialized) {
			init();
			initWidget();
			this.initialized = true;
		}
	}

	protected void init() {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().onInit(this.event, gp, p);
	}

	protected void initWidget() {
	}

	@Override
	public void setWorldAndResolution(final @Nullable Minecraft mc, final int i, final int j) {
		sSetWorldAndResolution(mc, i, j);
	}

	protected void sSetWorldAndResolution(final @Nullable Minecraft mc, final int i, final int j) {
		if (this.parent!=null)
			this.parent.setWorldAndResolution(mc, i, j);
		super.setWorldAndResolution(mc, i, j);
	}

	public void drawScreen(final int mousex, final int mousey, final float f, final float opacity) {
		sDrawScreen(mousex, mousey, f);
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().draw(this.event, gp, p, f, opacity);
	}

	@Override
	public void drawScreen(final int mousex, final int mousey, final float f) {
		drawScreen(mousex, mousey, f, getOpacity());
	}

	protected float getOpacity() {
		return 1f;
	}

	protected void sDrawScreen(final int mousex, final int mousey, final float f) {
		final GuiScreen parent = this.parent;
		if (parent!=null) {
			OpenGL.glPushMatrix();
			OpenGL.glTranslatef(0, 0, -200f);
			parent.drawScreen(mousex, mousey, f);
			OpenGL.glPopMatrix();
		}
		super.drawScreen(mousex, mousey, f);
	}

	@Override
	protected void mouseClicked(final int x, final int y, final int button) {
		this.mousebutton = button;
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().mouseClicked(this.event, gp, p, button);
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
		getContentPane().mouseDragged(this.event, gp, p, button, time);
		sMouseClickMove(x, y, button, time);
	}

	protected void sMouseClickMove(final int x, final int y, final int button, final long time) {
		super.mouseClickMove(x, y, button, time);
	}

	@Override
	public void updateScreen() {
		sUpdateScreen();
		final Point p = getMouseAbsolute();
		final Area gp = getAbsolute();
		getContentPane().update(this.event, gp, p);
		final int m = Mouse.getEventButton();
		if (this.lastbutton==-1&&m!=this.lastbutton||!Mouse.isButtonDown(this.mousebutton))
			getContentPane().mouseReleased(this.event, gp, p, this.mousebutton);
		this.lastbutton = m;
		if (this.mousebutton!=m&&m!=-1)
			this.mousebutton = m;
		if (!p.equals(this.mouselast)) {
			this.mouselast = p;
			getContentPane().mouseMoved(this.event, gp, p, this.mousebutton);
		}
		if (this.closeRequest)
			onCloseRequest();
	}

	protected void sUpdateScreen() {
		if (this.parent!=null)
			this.parent.updateScreen();
		super.updateScreen();
	}

	@Override
	protected void keyTyped(final char c, final int keycode) {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().keyTyped(this.event, gp, p, c, keycode);
		sKeyTyped(c, keycode);
	}

	protected void sKeyTyped(final char c, final int keycode) {
		if (keycode==Keyboard.KEY_ESCAPE)
			requestClose();
	}

	protected void onCloseRequest() {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		if (getContentPane().onClosing(this.event, gp, p))
			close();
	}

	protected void close() {
		this.mc.displayGuiScreen(this.parent);
	}

	public void requestClose() {
		getContentPane().onCloseRequest();
		this.closeRequest = true;
	}

	@Deprecated
	protected void cancelCloseRequest() {
		this.closeRequest = false;
	}

	@Override
	public void onGuiClosed() {
		sOnGuiClosed();
	}

	public void sOnGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public void handleMouseInput() {
		final int i = Mouse.getEventDWheel();
		if (i!=0) {
			final Area gp = getAbsolute();
			final Point p = getMouseAbsolute();
			getContentPane().mouseScrolled(this.event, gp, p, i);
		}
		sHandleMouseInput();
	}

	protected void sHandleMouseInput() {
		super.handleMouseInput();
	}

	@Override
	public void handleKeyboardInput() {
		sHandleKeyboardInput();
	}

	protected void sHandleKeyboardInput() {
		super.handleKeyboardInput();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return this.doesPauseGui||parentDoesGuiPauseGame();
	}

	protected boolean parentDoesGuiPauseGame() {
		return this.parent!=null&&this.parent.doesGuiPauseGame();
	}

	public WFrame setGuiPauseGame(final boolean doesPause) {
		this.doesPauseGui = doesPause;
		return this;
	}
}
