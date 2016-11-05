package com.kamesuta.mc.bnnwidget.component;

import com.kamesuta.mc.bnnwidget.WCommon;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.R;

public class MSelectLabel extends MSelect {

	protected MLabel field;

	public MSelectLabel(final R position, final float buttonwidth) {
		super(position, buttonwidth);
	}

	@Override
	protected WCommon getField() {
		return this.field = new MLabel(new R(Coord.left(this.buttonwidth), Coord.right(this.buttonwidth), Coord.top(0), Coord.bottom(0)), "") {
			@Override
			protected void onTextChanged(final String oldText) {
				onChanged(oldText, getText());
			}
		};
	}

	@Override
	public MSelect setText(final String text) {
		this.field.setText(text);
		return this;
	}
}
