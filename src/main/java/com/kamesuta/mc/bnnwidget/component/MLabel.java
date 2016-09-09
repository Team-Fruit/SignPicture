package com.kamesuta.mc.bnnwidget.component;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.render.RenderHelper;

public class MLabel extends WBase {
	protected int textcolor = 14737632;
	protected String text;

	public MLabel(final R position, final String text) {
		super(position);
		this.text = text;
	}

	public void setText(final String s) {
		if (StringUtils.equals(s, this.text)) {
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
		RenderHelper.startTexture();
		drawString(this.text, a.x1(), a.y1() + (a.h()-font().FONT_HEIGHT) / 2, this.textcolor);
	}
}
