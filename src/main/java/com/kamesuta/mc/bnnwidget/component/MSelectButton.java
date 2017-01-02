package com.kamesuta.mc.bnnwidget.component;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kamesuta.mc.bnnwidget.WBox;
import com.kamesuta.mc.bnnwidget.WCommon;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.R;

public class MSelectButton extends MSelect<MButton> {
	public final @Nonnull WBox field;

	public @Nonnull ButtonSelector selector = new ButtonSelector();

	public void setSelector(final @Nonnull ButtonSelector selector) {
		this.selector = selector;
	}

	@Override
	protected @Nonnull ButtonSelector getSelector() {
		return this.selector;
	}

	public MSelectButton(final @Nonnull R position, final float buttonwidth) {
		super(position, buttonwidth);
		add(this.field = new WBox(new R(Coord.left(this.buttonwidth), Coord.right(this.buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			public void set(final @Nullable WCommon widget) {
				onChanged(widget);
				super.set(widget);
			}
		});
	}

	protected void onChanged(final @Nullable WCommon widget) {
	}

	@Override
	protected void initWidget() {
		selectButton(getSelector().get());
	}

	@Override
	public @Nonnull MSelectButton setText(final @Nonnull MButton text) {
		this.field.set(text);
		return this;
	}

	public @Nonnull MSelectButton selectButton(final @Nullable MButton button) {
		this.field.set(button);
		return this;
	}

	@Override
	public @Nonnull MSelectButton setPosLabel(final @Nullable String s) {
		this.pos.setText(s);
		return this;
	}

	@Override
	public @Nonnull MSelectButton setNegLabel(final @Nullable String s) {
		this.neg.setText(s);
		return this;
	}

	public static class ButtonSelector extends PosSelector<MButton> {
		protected @Nonnull List<MButton> list = Lists.newArrayList();

		public void setList(final @Nonnull List<MButton> list) {
			this.list = list;
		}

		protected @Nonnull List<MButton> getList() {
			return this.list;
		}

		@Override
		public int length() {
			return getList().size();
		}

		@Override
		protected int indexOf(final @Nullable MButton o) {
			return this.list.indexOf(o);
		}

		@Override
		public @Nonnull MButton get() {
			final int length = length();
			final int current = getCurrentPos();
			MButton m = null;
			if (current>=0&&current<length&&(m = getList().get(current))!=null)
				return m;
			return new MButton(new R());
		}
	}
}