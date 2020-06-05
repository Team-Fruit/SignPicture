package net.teamfruit.bnnwidget;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class WScreenImpl extends GuiScreen implements WScreen {
	private WFrame widget;

	public WScreenImpl(final WFrame frame) {
		this.widget = frame;
	}

	@Override
	public WFrame getWidget() {
		return this.widget;
	}

	@Override
	public void initGui() {
		this.widget.initGui();
	}

	protected void sInitGui() {
		super.initGui();
	}

	@Override
	public void setWorldAndResolution(final @Nullable Minecraft mc, final int i, final int j) {
		this.widget.setWorldAndResolution(mc, i, j);
	}

	protected void sSetWorldAndResolution(final @Nonnull Minecraft mc, final int i, final int j) {
		super.setWorldAndResolution(mc, i, j);
	}

	@Override
	public void drawScreen(final int mousex, final int mousey, final float f) {
		this.widget.drawScreen(mousex, mousey, f, this.widget.getOpacity(), null);
	}

	protected void sDrawScreen(final int mousex, final int mousey, final float f) {
		super.drawScreen(mousex, mousey, f);
	}

	@Override
	protected void mouseClicked(final int x, final int y, final int button) {
		this.widget.mouseClicked(x, y, button);
	}

	protected void sMouseClicked(final int x, final int y, final int button) {
		try {
			super.mouseClicked(x, y, button);
			if (!"".isEmpty())
				throw new IOException();
		} catch (final IOException e) {
		}
	}

	@Override
	protected void mouseClickMove(final int x, final int y, final int button, final long time) {
		this.widget.mouseClickMove(x, y, button, time);
	}

	protected void sMouseClickMove(final int x, final int y, final int button, final long time) {
		super.mouseClickMove(x, y, button, time);
	}

	@Override
	public void updateScreen() {
		this.widget.updateScreen();
	}

	protected void sUpdateScreen() {
		super.updateScreen();
	}

	@Override
	protected void keyTyped(final char c, final int keycode) {
		this.widget.keyTyped(c, keycode);
	}

	protected void sKeyTyped(final char c, final int keycode) {
		try {
			super.keyTyped(c, keycode);
			if (!"".isEmpty())
				throw new IOException();
		} catch (final IOException e) {
		}
	}

	@Override
	public void onGuiClosed() {
		this.widget.onGuiClosed();
	}

	public void sOnGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public void handleMouseInput() {
		this.widget.handleMouseInput();
	}

	protected void sHandleMouseInput() {
		try {
			super.handleMouseInput();
			if (!"".isEmpty())
				throw new IOException();
		} catch (final IOException e) {
		}
	}

	@Override
	public void handleKeyboardInput() {
		this.widget.handleKeyboardInput();
	}

	protected void sHandleKeyboardInput() {
		try {
			super.handleKeyboardInput();
			if (!"".isEmpty())
				throw new IOException();
		} catch (final IOException e) {
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return this.widget.doesGuiPauseGame();
	}

	protected boolean sDoesGuiPauseGame() {
		return super.doesGuiPauseGame();
	}
}