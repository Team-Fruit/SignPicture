package com.kamesuta.mc.bnnwidget.component;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.R;

public class MScaledLabel extends MLabel {
	public MScaledLabel(final R position, final String text) {
		super(position, text);
	}

	@Override
	public float getScaleWidth(final Area a) {
		final float f1 = a.w()/font().getStringWidth(getText());
		final float f2 = a.h()/font().FONT_HEIGHT;
		return Math.min(f1, f2);
	}

	@Override
	public float getScaleHeight(final Area a) {
		final float f1 = a.w()/font().getStringWidth(getText());
		final float f2 = a.h()/font().FONT_HEIGHT;
		return Math.min(f1, f2);
	}
}
