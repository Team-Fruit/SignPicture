package net.teamfruit.bnnwidget.component;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.teamfruit.bnnwidget.WEvent;
import net.teamfruit.bnnwidget.WPanel;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.position.Coord;
import net.teamfruit.bnnwidget.position.Point;
import net.teamfruit.bnnwidget.position.R;

/**
 * 選択肢コンポーネントです
 * @param <E> 選択肢
 * @author TeamFruit
 */
public abstract class MSelect<E> extends WPanel {
	protected final float buttonwidth;

	/**
	 * マイナスボタン
	 */
	public @Nonnull MButton neg;
	/**
	 * プラスボタン
	 */
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

	/**
	 * 選択肢を設定します
	 * @param text 選択肢
	 * @return this
	 */
	public abstract @Nonnull MSelect<E> setText(final @Nonnull E text);

	/**
	 * セレクター
	 * @return セレクター
	 */
	protected abstract @Nonnull Selector<E> getSelector();

	/**
	 * プラスボタンのラベルを変更します
	 * @param s ラベル
	 * @return this
	 */
	public @Nonnull MSelect<E> setPosLabel(final @Nullable String s) {
		this.pos.setText(s);
		return this;
	}

	/**
	 * マイナスボタンのラベルを変更します
	 * @param s ラベル
	 * @return this
	 */
	public @Nonnull MSelect<E> setNegLabel(final @Nullable String s) {
		this.neg.setText(s);
		return this;
	}

	/**
	 * セレクター
	 * @param <E> 選択肢
	 * @author TeamFruit
	 */
	public static interface Selector<E> {
		/**
		 * 選択中の選択肢
		 * @return
		 */
		@Nonnull
		E get();

		/**
		 * 次へ進みます
		 */
		void next();

		/**
		 * 前へ戻ります
		 */
		void prev();
	}

	/**
	 * ポジションセレクター
	 * @param <E> 選択肢
	 * @author TeamFruit
	 */
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

		/**
		 * 現在の選択ポジションを設定します
		 * @param current 選択ポジション
		 */
		public void setCurrentPos(final int current) {
			this.current = current;
		}

		/**
		 * 現在の選択肢を設定します
		 * @param o 選択肢
		 */
		public void setCurrentPos(final @Nullable E o) {
			final int i = this.indexOf(o);
			if (i>=0)
				setCurrentPos(i);
		}

		/**
		 * 選択肢のポジション
		 * @param o 選択肢
		 * @return ポジション
		 */
		protected abstract int indexOf(@Nullable E o);

		/**
		 * 選択肢の数
		 * @return
		 */
		protected abstract int length();

		/**
		 * 現在の選択ポジション
		 * @return
		 */
		public int getCurrentPos() {
			final int length = length();
			if (length<=0)
				return 0;
			return this.current = (this.current%length+length)%length;
		}
	}

	/**
	 * 文字列のセレクター
	 *
	 * @author TeamFruit
	 */
	public static class StringSelector extends PosSelector<String> {
		/**
		 * 選択肢集合
		 */
		protected @Nonnull List<?> list = Lists.newArrayList();

		/**
		 * 選択肢集合を設定します
		 * @param list
		 */
		public void setList(final @Nonnull List<?> list) {
			this.list = list;
		}

		/**
		 * 選択肢集合
		 * @return
		 */
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