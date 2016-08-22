package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.guiwidget.GuiBase;
import com.kamesuta.mc.guiwidget.GuiEvent;
import com.kamesuta.mc.guiwidget.GuiFrame;
import com.kamesuta.mc.guiwidget.GuiPosition;
import com.kamesuta.mc.guiwidget.component.GuiTextField;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.relative.RelativePosition;

public class GuiSignPicture extends GuiFrame {
	protected String url;
	protected String[] signurl;

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void initWidgets() {
		add(new GuiBase(RelativePosition.createFit(true)) {
			@Override
			public void draw(final GuiEvent ev, final GuiPosition pgp, final Point p, final float frame) {
				glColor4f(0f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(getGuiPosition(pgp).getAbsolute(), GL_QUADS);
				glEnable(GL_TEXTURE_2D);
			}
		});
		add(new GuiBase(new RelativePosition(5, -151, 135, -21, true)) {
			@Override
			public void draw(final GuiEvent ev, final GuiPosition pgp, final Point p, final float frame) {
				glColor4f(0f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(getGuiPosition(pgp).getAbsolute(), GL_QUADS);
				glEnable(GL_TEXTURE_2D);


			}
		});
		add(new GuiTextField(new RelativePosition(5, -21, -6, -6, true), "aaaa") {
			@Override
			public void onFocusChanged() {
				super.onFocusChanged();
				GuiSignPicture.this.url = getText();
			}
		});
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
}
