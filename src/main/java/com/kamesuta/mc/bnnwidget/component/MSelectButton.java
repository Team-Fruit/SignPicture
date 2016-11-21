package com.kamesuta.mc.bnnwidget.component;

import java.util.List;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WBox;
import com.kamesuta.mc.bnnwidget.WCommon;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.MSelect.PosSelector;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public class MSelectButton extends WPanel {
	public MButton neg;
	public WBox field;
	public MButton pos;

	protected float buttonwidth;

	public ButtonSelector selector = new ButtonSelector();

	public void setSelector(final ButtonSelector selector) {
		this.selector = selector;
	}

	protected ButtonSelector getSelector() {
		return this.selector;
	}

	public MSelectButton(final R position, final float buttonwidth) {
		super(position);
		this.buttonwidth = buttonwidth;
		this.neg = new MButton(new R(Coord.left(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				getSelector().prev();
				MSelectButton.this.selectButton(getSelector().get());
				return true;
			}
		}.setText("<");
		add(this.neg);
		add(getField());
		this.pos = new MButton(new R(Coord.right(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				getSelector().next();
				MSelectButton.this.selectButton(getSelector().get());
				return true;
			}
		}.setText(">");
		add(this.pos);
	}

	@Override
	protected void initWidget() {
		selectButton(getSelector().get());
	}

	protected WCommon getField() {
		return this.field = new WBox(new R(Coord.left(this.buttonwidth), Coord.right(this.buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			public void set(final WCommon widget) {
				onChanged(widget);
				super.set(widget);
			}
		};
	}

	protected void onChanged(final WCommon widget) {
	}

	public MSelectButton selectButton(final MButton button) {
		this.field.set(button);
		return this;
	}

	public MSelectButton setPosLabel(final String s) {
		this.pos.setText(s);
		return this;
	}

	public MSelectButton setNegLabel(final String s) {
		this.neg.setText(s);
		return this;
	}

	public static class ButtonSelector extends PosSelector<MButton> {
		protected List<MButton> list = Lists.newArrayList();

		public void setList(final List<MButton> list) {
			this.list = list;
		}

		protected List<MButton> getList() {
			return this.list;
		}

		@Override
		public int length() {
			return getList().size();
		}

		@Override
		protected int indexOf(final MButton o) {
			return this.list.indexOf(o);
		}

		@Override
		public MButton get() {
			final int length = length();
			final int current = getCurrentPos();
			if (current<0||current>=length)
				return new MButton(new R());
			return getList().get(current);
		}
	}
}