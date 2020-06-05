package net.teamfruit.bnnwidget.component;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.teamfruit.bnnwidget.OverridablePoint;
import net.teamfruit.bnnwidget.WBox;
import net.teamfruit.bnnwidget.WCommon;
import net.teamfruit.bnnwidget.position.Coord;
import net.teamfruit.bnnwidget.position.R;

/**
 * ボタン選択肢コンポーネントです
 * @author TeamFruit
 */
public class MSelectButton extends MSelect<MButton> {
	/**
	 * ボックス
	 */
	public final @Nonnull WBox field;

	/**
	 * ボタンセレクター
	 */
	public @Nonnull ButtonSelector selector = new ButtonSelector();

	/**
	 * ボタンセレクターを設定
	 * @param selector ボタンセレクター
	 */
	public void setSelector(final @Nonnull ButtonSelector selector) {
		this.selector = selector;
	}

	/**
	 * ボタンセレクター
	 */
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

	/**
	 * 選択が変更された際に呼ばれます
	 * @param widget ボタン
	 */
	@OverridablePoint
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

	/**
	 * 選択します
	 * @param button ボタン
	 * @return this
	 */
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

	/**
	 * ボタンセレクター
	 *
	 * @author TeamFruit
	 */
	public static class ButtonSelector extends PosSelector<MButton> {
		/**
		 * ボタン選択肢集合
		 */
		protected @Nonnull List<MButton> list = Lists.newArrayList();

		/**
		 * ボタン選択肢集合を設定します
		 * @param list
		 */
		public void setList(final @Nonnull List<MButton> list) {
			this.list = list;
		}

		/**
		 * ボタン選択肢集合
		 * @return
		 */
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