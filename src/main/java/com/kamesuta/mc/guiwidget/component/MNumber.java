package com.kamesuta.mc.guiwidget.component;

import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.WPanel;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Coord;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.R;
import com.kamesuta.mc.guiwidget.position.RArea;

public class MNumber extends WPanel {
	public MButton neg;
	public MTextField field;
	public MButton pos;

	public MNumber(final R position, final float buttonwidth) {
		super(position);
		this.neg = new MButton(new RArea(Coord.left(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0)), "-") {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				return MNumber.this.field.setText(Float.toString(NumberUtils.toFloat(MNumber.this.field.getText(), 0)-1f));
			}
		};
		add(this.neg);
		this.field = new MTextField(new RArea(Coord.left(buttonwidth), Coord.right(buttonwidth), Coord.top(0), Coord.bottom(0)), "");
		this.field.setAllowedCharacters("0123456789");
		add(this.field);
		this.pos = new MButton(new RArea(Coord.right(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0)), "+") {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				return MNumber.this.field.setText(Float.toString(NumberUtils.toFloat(MNumber.this.field.getText(), 0)+1f));
			}
		};
		add(this.pos);
	}

	public MNumber setNumber(final float f) {
		this.field.setText(Float.toString(f));
		return this;
	}
}
