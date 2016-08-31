package com.kamesuta.mc.guiwidget.component;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.R;

public class MLabel extends WBase {
	protected int textcolor = 14737632;
	protected String text;

	public MLabel(final R position, final String text) {
		super(position);
		this.text = text;
	}

	public void setText(final String s) {
		if (s.equals(this.text)) {
			return;
		}
		final String oldText = this.text;
		this.text = s;
		onTextChanged(oldText);
	}

	public final String getText() {
		return this.text;
	}

	protected void onTextChanged(final String oldText) {
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area out = getGuiPosition(pgp);
		drawText(out);
	}

	protected void drawText(final Area a) {
		drawString(this.text, a.x1(), a.y1() + (a.h()-font.FONT_HEIGHT) / 2, this.textcolor);
	}
}
