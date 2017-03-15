package com.kamesuta.mc.signpic.plugin.gui;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MChatTextField;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public class GuiManagerSarchBox extends WPanel {

	protected final MChatTextField textField;

	public GuiManagerSarchBox(final R position) {
		super(position);
		this.textField = new GuiManagerTextField(new R(Coord.height(15), Coord.width(200), Coord.top(10)));
	}

	@Override
	protected void initWidget() {
		add(this.textField);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
		super.draw(ev, pgp, p, frame, popacity);
	}
}
