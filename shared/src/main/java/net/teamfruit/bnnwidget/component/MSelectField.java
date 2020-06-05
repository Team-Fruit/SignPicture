package net.teamfruit.bnnwidget.component;

import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.OverridablePoint;
import net.teamfruit.bnnwidget.position.Coord;
import net.teamfruit.bnnwidget.position.R;

/**
 * テキストフィールド選択肢コンポーネントです
 * @author TeamFruit
 */
public class MSelectField extends MSelect<String> {
	/**
	 * テキストフィールド
	 */
	protected final @Nonnull MChatTextField field;
	private @Nonnull StringSelector selector = new StringSelector();

	/**
	 * 文字列セレクターを設定
	 * @param selector ボタンセレクター
	 */
	public void setSelector(final @Nonnull StringSelector selector) {
		this.selector = selector;
	}

	/**
	 * 文字列セレクター
	 */
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

	/**
	 * 選択が変更された際に呼ばれます
	 * @param widget ボタン
	 */
	@OverridablePoint
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
