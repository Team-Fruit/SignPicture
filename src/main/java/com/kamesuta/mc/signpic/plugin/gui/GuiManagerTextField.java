package com.kamesuta.mc.signpic.plugin.gui;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.component.MChatTextField;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;

public class GuiManagerTextField extends MChatTextField {

	public GuiManagerTextField(final R position) {
		super(position);
		setEnableBackgroundDrawing(false);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
		final Area a = getGuiPosition(pgp);

		WRenderer.startShape();
		OpenGL.glColor4f(1, 1, 1, .5f);
		draw(a);
		super.draw(ev, pgp, p, frame, opacity);
	}
}
