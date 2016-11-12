package com.kamesuta.mc.bnnwidget;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class WFrame extends GuiScreen implements WContainer<WCommon> {
	protected GuiScreen parent;
	protected WPanel contentPane = new WPanel(new R());
	protected final WEvent event = new WEvent(this);
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
		if (super.width!=(int) this.width)
			this.width = super.width;
		return this.width;
	}

	public float height() {
		if (super.height!=(int) this.height)
			this.height = super.height;
		return this.height;
	}

	public WFrame(final GuiScreen parent) {
		this.parent = parent;
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
	}

	protected void init() {
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().onInit(this.event, gp, p);
	}

	protected void initWidget() {
	}

	@Override
	public void setWorldAndResolution(final Minecraft mc, final int i, final int j) {
		final boolean init = this.mc==null;
		sSetWorldAndResolution(mc, i, j);
		if (init) {
			init();
			initWidget();
		}
	}

	protected void sSetWorldAndResolution(final Minecraft mc, final int i, final int j) {
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
		if (this.parent!=null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, -200f);
			this.parent.drawScreen(mousex, mousey, f);
			GlStateManager.popMatrix();
		}
		super.drawScreen(mousex, mousey, f);
	}

	protected int mousebutton;

	@Override
	protected void mouseClicked(final int x, final int y, final int button) throws IOException {
		this.mousebutton = button;
		final Area gp = getAbsolute();
		final Point p = getMouseAbsolute();
		getContentPane().mouseClicked(this.event, gp, p, button);
		sMouseClicked(x, y, button);
	}

	protected void sMouseClicked(final int x, final int y, final int button) throws IOException {
		super.mouseClicked(x, y, button);
	}

	@Override
	protected void mouseReleased(final int x, final int y, final int button) {
		sMouseMovedOrUp(x, y, button);
	}

	protected void sMouseMovedOrUp(final int x, final int y, final int button) {
		super.mouseReleased(x, y, button);
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

	protected Point mouselast;

	@Override
	public void updateScreen() {
		sUpdateScreen();
		final Point p = getMouseAbsolute();
		final Area gp = getAbsolute();
		getContentPane().update(this.event, gp, p);
		final int m = Mouse.getEventButton();
		if (this.mousebutton!=m) {
			getContentPane().mouseReleased(this.event, gp, p, this.mousebutton);
			if (m>=0)
				this.mousebutton = m;
		}
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

	protected boolean closeRequest;

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
	public void handleMouseInput() throws IOException {
		final int i = Mouse.getEventDWheel();
		if (i!=0) {
			final Area gp = getAbsolute();
			final Point p = getMouseAbsolute();
			getContentPane().mouseScrolled(this.event, gp, p, i);
		}
		sHandleMouseInput();
	}

	protected void sHandleMouseInput() throws IOException {
		super.handleMouseInput();
	}

	@Override
	public void handleKeyboardInput() throws IOException {
		sHandleKeyboardInput();
	}

	protected void sHandleKeyboardInput() throws IOException {
		super.handleKeyboardInput();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return this.parent==null||sDoesGuiPauseGame();
	}

	protected boolean sDoesGuiPauseGame() {
		return this.parent!=null&&this.parent.doesGuiPauseGame();
	}
}
