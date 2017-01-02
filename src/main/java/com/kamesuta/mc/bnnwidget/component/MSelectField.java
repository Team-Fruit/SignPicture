package com.kamesuta.mc.bnnwidget.component;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.R;

public class MSelectField extends MSelect<String> {
	protected final @Nonnull MChatTextField field;
	private @Nonnull StringSelector selector = new StringSelector();

	public void setSelector(final @Nonnull StringSelector selector) {
		this.selector = selector;
	}

	@Override
	protected @Nonnull Selector<String> getSelector() {
		return this.selector;
	}

	public MSelectField(final @Nonnull R position, final float buttonwidth) {
		super(position, buttonwidth);
		add(this.field = new MChatTextField(new R(Coord.left(this.buttonwidth), Coord.right(this.buttonwidth), Coord.top(0), Coord.bottom(0))) {
			@Override
			protected void onTextChanged(final @Nonnull String oldText) {
				onChanged(oldText, getText());
			}
		});
	}

	protected void onChanged(final @Nonnull String oldText, final @Nonnull String newText) {
	}

	@Override
	protected void initWidget() {
		setText(getSelector().get());
	}

	@Override
	public @Nonnull MSelectField setText(final @Nonnull String text) {
		this.field.setText(text);
		return this;
	}
}
