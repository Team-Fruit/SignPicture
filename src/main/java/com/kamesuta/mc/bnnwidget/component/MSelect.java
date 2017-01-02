package com.kamesuta.mc.bnnwidget.component;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public abstract class MSelect<E> extends WPanel {
	protected final float buttonwidth;

	public @Nonnull MButton neg;
	public @Nonnull MButton pos;

	public MSelect(@Nonnull final R position, final float buttonwidth) {
		super(position);
		this.buttonwidth = buttonwidth;
		this.neg = new MButton(new R(Coord.left(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				getSelector().prev();
				MSelect.this.setText(getSelector().get());
				return true;
			}
		}.setText("<");
		add(this.neg);
		this.pos = new MButton(new R(Coord.right(0), Coord.width(buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				getSelector().next();
				MSelect.this.setText(getSelector().get());
				return true;
			}
		}.setText(">");
		add(this.pos);
	}

	public abstract @Nonnull MSelect<E> setText(final @Nonnull E text);

	protected abstract @Nonnull Selector<E> getSelector();

	public @Nonnull MSelect<E> setPosLabel(final @Nullable String s) {
		this.pos.setText(s);
		return this;
	}

	public @Nonnull MSelect<E> setNegLabel(final @Nullable String s) {
		this.neg.setText(s);
		return this;
	}

	public static interface Selector<E> {
		@Nonnull
		E get();

		void next();

		void prev();
	}

	public static abstract class PosSelector<E> implements Selector<E> {
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

		public void setCurrentPos(final @Nullable E o) {
			final int i = this.indexOf(o);
			if (i>=0)
				setCurrentPos(i);
		}

		protected abstract int indexOf(@Nullable E o);

		protected abstract int length();

		public int getCurrentPos() {
			final int length = length();
			if (length<=0)
				return 0;
			return this.current = (this.current%length+length)%length;
		}
	}

	public static class StringSelector extends PosSelector<String> {
		protected @Nonnull List<?> list = Lists.newArrayList();

		public void setList(final @Nonnull List<?> list) {
			this.list = list;
		}

		protected @Nonnull List<?> getList() {
			return this.list;
		}

		@Override
		public int length() {
			return getList().size();
		}

		@Override
		protected int indexOf(final @Nullable String o) {
			return this.list.indexOf(o);
		}

		@Override
		public @Nonnull String get() {
			final int length = length();
			final int current = getCurrentPos();
			if (current<0||current>=length)
				return "";
			return getList().get(current).toString();
		}
	}
}