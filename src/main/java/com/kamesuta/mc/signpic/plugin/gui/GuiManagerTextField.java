package com.kamesuta.mc.signpic.plugin.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MChatTextField;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;

public class GuiManagerTextField extends WPanel {

	protected final @Nonnull MChatTextField textField;

	public GuiManagerTextField(final @Nonnull R position) {
		super(position);
		this.textField = new MChatTextField(new R(Coord.left(2), Coord.top(2))) {
			@Override
			protected void onTextChanged(final String oldText) {
				GuiManagerTextField.this.onTextChanged(oldText);
			}
		};
		this.textField.setEnableBackgroundDrawing(false);
	}

	public @Nonnull MChatTextField getTextField() {
		return this.textField;
	}

	public void onTextChanged(final String oldText) {
	}

	@Override
	protected void initWidget() {
		add(this.textField);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity, @Nonnull final RenderOption opt) {
		final Area a = getGuiPosition(pgp);

		OpenGL.glPushMatrix();
		WRenderer.startShape();
		OpenGL.glColor4f(.1f, .1f, .1f, .5f);
		draw(a);
		OpenGL.glColor4f(1, 1, 1, .9f);
		OpenGL.glLineWidth(1.5f);
		draw(a, GL_LINE_LOOP);
		OpenGL.glPopMatrix();

		WRenderer.startTexture();
		super.draw(ev, pgp, p, frame, opacity, opt);
	}
}
