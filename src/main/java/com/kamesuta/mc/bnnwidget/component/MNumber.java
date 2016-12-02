package com.kamesuta.mc.bnnwidget.component;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.bnnwidget.ShortestFloatFormatter;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class MNumber extends WPanel {
	public MButton neg;
	public MChatTextField field;
	public MButton pos;

	public MNumber(final R position, final float buttonwidth) {
		super(position);
		this.neg = new MButton(new R(Coord.left(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				return negClicked();
			}
		}.setText(I18n.format("signpic.gui.editor.number.neg"));
		add(this.neg);
		this.field = new MChatTextField(new R(Coord.left(buttonwidth), Coord.right(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected void onTextChanged(final String oldText) {
				onNumberChanged(oldText, getText());
			}
		}.setAllowedCharacters("+-.eE0123456789").setWatermark(I18n.format("signpic.gui.editor.number.unknown"));
		add(this.field);
		this.pos = new MButton(new R(Coord.right(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				return posClicked();
			}
		}.setText(I18n.format("signpic.gui.editor.number.pos"));
		add(this.pos);
	}

	protected boolean negClicked() {
		float f;
		if (GuiScreen.isShiftKeyDown())
			f = .1f;
		else if (GuiScreen.isCtrlKeyDown())
			f = .01f;
		else
			f = 1f;
		MNumber.this.field.setText(ShortestFloatFormatter.format(NumberUtils.toFloat(MNumber.this.field.getText())-f));
		return true;
	}

	protected boolean posClicked() {
		float f;
		if (GuiScreen.isShiftKeyDown())
			f = .1f;
		else if (GuiScreen.isCtrlKeyDown())
			f = .01f;
		else
			f = 1f;
		MNumber.this.field.setText(ShortestFloatFormatter.format(NumberUtils.toFloat(MNumber.this.field.getText())+f));
		return true;
	}

	public MNumber setNumber(final float f) {
		this.field.setText(Float.isNaN(f) ? "" : ShortestFloatFormatter.format(f));
		return this;
	}

	public MNumber setPosLabel(final String s) {
		if (!StringUtils.isEmpty(s)&&!StringUtils.contains(s, "signpic."))
			this.pos.setText(s);
		return this;
	}

	public MNumber setNegLabel(final String s) {
		if (!StringUtils.isEmpty(s)&&!StringUtils.contains(s, "signpic."))
			this.neg.setText(s);
		return this;
	}

	public MNumber setUnknownLabel(final String s) {
		if (!StringUtils.isEmpty(s)&&!StringUtils.contains(s, "signpic."))
			this.field.setWatermark(s);
		return this;
	}

	protected void onNumberChanged(final String oldText, final String newText) {
	}
}
