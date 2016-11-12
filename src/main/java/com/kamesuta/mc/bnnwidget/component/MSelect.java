package com.kamesuta.mc.bnnwidget.component;

import java.util.List;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WCommon;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public class MSelect extends WPanel {
	public MButton neg;
	public MChatTextField field;
	public MButton pos;

	protected float buttonwidth;

	public Selector selector = new ListSelector();

	public void setSelector(final Selector selector) {
		this.selector = selector;
	}

	protected Selector getSelector() {
		return this.selector;
	}

	public MSelect(final R position, final float buttonwidth) {
		super(position);
		this.buttonwidth = buttonwidth;
		this.neg = new MButton(new R(Coord.left(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0)), "<") {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				getSelector().prev();
				MSelect.this.setText(getSelector().get());
				return true;
			}
		};
		add(this.neg);
		add(getField());
		this.pos = new MButton(new R(Coord.right(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0)), ">") {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				getSelector().next();
				MSelect.this.setText(getSelector().get());
				return true;
			}
		};
		add(this.pos);
	}

	@Override
	protected void initWidget() {
		setText(getSelector().get());
	}

	protected WCommon getField() {
		return this.field = new MChatTextField(new R(Coord.left(this.buttonwidth), Coord.right(this.buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected void onTextChanged(final String oldText) {
				onChanged(oldText, getText());
			}
		};
	}

	protected void onChanged(final String oldText, final String newText) {
	}

	public MSelect setText(final String text) {
		this.field.setText(text);
		return this;
	}

	public MSelect setPosLabel(final String s) {
		this.pos.setText(s);
		return this;
	}

	public MSelect setNegLabel(final String s) {
		this.neg.setText(s);
		return this;
	}

	public static interface Selector {
		String get();

		void next();

		void prev();
	}

	public static class ListSelector implements Selector {
		protected List<?> list = Lists.newArrayList();

		public void setList(final List<String> list) {
			this.list = list;
		}

		protected List<?> getList() {
			return this.list;
		}

		public int length() {
			return getList().size();
		}

		@Override
		public String get() {
			final int length = length();
			final int current = getCurrentPos();
			if (current<0||current>=length)
				return "";
			return getList().get(current).toString();
		}

		@Override
		public void next() {
			this.current++;
		}

		@Override
		public void prev() {
			this.current--;
		}

		protected int current;

		public void setCurrentPos(final int current) {
			this.current = current;
		}

		public void setCurrentPos(final Object o) {
			final int i = this.list.indexOf(o);
			if (i>=0)
				setCurrentPos(i);
		}

		public int getCurrentPos() {
			final int length = length();
			if (length<=0)
				return 0;
			return this.current = (this.current%length+length)%length;
		}
	}
}
