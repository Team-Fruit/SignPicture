package net.teamfruit.bnnwidget.position;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

/**
 * 4つの{@link Coord}から構成される相対範囲です
 * @see Coord
 * @author TeamFruit
 */
public class R {
	private static final @Nonnull Coord default_x1 = Coord.left(0f);
	private static final @Nonnull Coord default_y1 = Coord.top(0f);
	private static final @Nonnull Coord default_x2 = Coord.pwidth(1f);
	private static final @Nonnull Coord default_y2 = Coord.pheight(1f);

	/**
	 * この値がセットされている場合はこの値からの相対座標になります
	 */
	protected @Nullable R parent;
	/**
	 * 1つ目のX相対座標
	 */
	protected @Nullable Coord x1 = default_x1;
	/**
	 * 1つ目のY相対座標
	 */
	protected @Nullable Coord y1 = default_y1;
	/**
	 * 2つ目のX相対座標
	 */
	protected @Nullable Coord x2 = default_x2;
	/**
	 * 2つ目のY相対座標
	 */
	protected @Nullable Coord y2 = default_y2;

	/**
	 * いくつかの{@link Coord}より相対範囲を構成します
	 * <p>
	 * 指定されていないサイドの座標はデフォルトの値(左:0, 上:0, 幅:100%, 高さ:100%)が使用されます。
	 * <br>
	 * 幅、高さはそれぞれ一度ずつまで含むことができます。
	 * <br>
	 * 横軸座標系(左、右、幅)、縦軸座標系(上、下、高さ)はそれぞれ2回ずつ含むことができます。
	 * @param a 相対座標
	 * @throws IllegalStateException 各座標系がそれぞれ3つ以上、または幅、高さが2つ以上指定された場合。
	 */
	public R(final @Nonnull Coord... a) throws IllegalStateException {
		for (final Coord c : a)
			if (c!=null)
				set(c);
	}

	/**
	 * いくつかの{@link Coord}より、parentからの相対範囲を構成します
	 * <p>
	 * 指定されていないサイドの座標はデフォルトの値(左:0, 上:0, 幅:100%, 高さ:100%)が使用されます。
	 * <br>
	 * 幅、高さはそれぞれ一度ずつまで含むことができます。
	 * <br>
	 * 横軸座標系(左、右、幅)、縦軸座標系(上、下、高さ)はそれぞれ2回ずつ含むことができます。
	 * @param parent 相対範囲の基準範囲
	 * @param a 相対座標
	 * @throws IllegalStateException 各座標系がそれぞれ3つ以上、または幅、高さが2つ以上指定された場合。
	 */
	public R(final @Nonnull R parent, final @Nonnull Coord... a) throws IllegalStateException {
		this(a);
		setParent(parent);
	}

	/**
	 * 相対範囲のコピーを作成します
	 * @param a 相対座標
	 */
	public R(final @Nonnull R a) {
		set(a);
	}

	/**
	 * 相対範囲より、parentからの相対範囲を構成します
	 * @param parent 相対範囲の基準範囲
	 * @param a 相対座標
	 */
	public R(final @Nonnull R parent, final @Nonnull R a) {
		this(a);
		setParent(parent);
	}

	/**
	 * {@code (Coord.left(diff_x1), Coord.top(diff_y1), Coord.right(-diff_x2), Coord.bottom(-diff_y2))}の相対範囲を作成します
	 * <p>
	 * 代わりに{@code (Coord.left(diff_x1), Coord.top(diff_y1), Coord.right(-diff_x2), Coord.bottom(-diff_y2))}を使用してください
	 * @param diff_x1 相対座標
	 * @param diff_y1 相対座標
	 * @param diff_x2 相対座標
	 * @param diff_y2 相対座標
	 * @return
	 */
	@Deprecated
	public static @Nonnull R diff(final float diff_x1, final float diff_y1, final float diff_x2, final float diff_y2) {
		return new R(Coord.left(diff_x1), Coord.top(diff_y1), Coord.right(-diff_x2), Coord.bottom(-diff_y2));
	}

	/**
	 * 絶対座標を基準に相対座標を求めます
	 * @param a 基準絶対座標
	 * @return 絶対範囲
	 */
	public @Nonnull Area getAbsolute(@Nonnull Area a) {
		final Coord x1 = this.x1;
		final Coord y1 = this.y1;
		final Coord x2 = this.x2;
		final Coord y2 = this.y2;
		if (x1!=null&&y1!=null&&x2!=null&&y2!=null) {
			if (this.parent!=null)
				a = this.parent.getAbsolute(a);
			final float tx1 = x1.base(a);
			final float ty1 = y1.base(a);
			final float tx2 = x2.next(a, x1);
			final float ty2 = y2.next(a, y1);
			final float rx1 = Math.min(tx1, tx2);
			final float ry1 = Math.min(ty1, ty2);
			final float rx2 = Math.max(tx1, tx2);
			final float ry2 = Math.max(ty1, ty2);
			return new Area(rx1, ry1, rx2, ry2);
		} else
			throw new IllegalStateException("attempt to get absolute from uncompleted relative");
	}

	/**
	 * 親相対範囲を指定します
	 * @param parent
	 */
	protected void setParent(final @Nonnull R parent) {
		this.parent = parent;
	}

	/**
	 * 相対範囲を読み込みます
	 * @param a 相対範囲
	 */
	protected void set(final @Nonnull R a) {
		this.x1 = a.x1;
		this.y1 = a.y1;
		this.x2 = a.x2;
		this.y2 = a.y2;
	}

	/**
	 * Coordを1つ追加し、相対座標を構築していきます。
	 * <p>
	 * 指定されていないサイドの座標はデフォルトの値(左:0, 上:0, 幅:100%, 高さ:100%)が使用されます。
	 * <br>
	 * 幅、高さはそれぞれ一度ずつまで含むことができます。
	 * <br>
	 * 横軸座標系(左、右、幅)、縦軸座標系(上、下、高さ)はそれぞれ2回ずつ含むことができます。
	 * @param parent 相対範囲の基準範囲
	 * @param n 相対座標
	 * @throws IllegalStateException 各座標系がそれぞれ3つ以上、または幅、高さが2つ以上指定された場合。
	 */
	protected void set(final @Nonnull Coord n) throws IllegalStateException {
		Validate.notNull(n, String.format("null coord [%s]", this));
		Validate.notNull(n.getSide(), String.format("invaild coord [%s]", this));
		switch (n.getSide()) {
			case Left:
			case Right:
				if (this.x1==default_x1) {
					this.x1 = n;
					break;
				}
			case Width:
				if (this.x2==default_x2) {
					this.x2 = n;
					break;
				}
			case Top:
			case Bottom:
				if (this.y1==default_y1) {
					this.y1 = n;
					break;
				}
			case Height:
				if (this.y2==default_y2) {
					this.y2 = n;
					break;
				}
				throw new IllegalStateException(String.format("conflic coord [%s]", this));
		}
	}

	/**
	 * いくつかの{@link Coord}より、この相対範囲からの相対範囲を構成します
	 * @param a 相対範囲の基準範囲
	 * @return 相対範囲
	 */
	public @Nonnull R child(final @Nonnull Coord... a) {
		return new R(this, a);
	}

	/**
	 * 相対範囲より、この相対範囲からの相対範囲を構成します
	 * @param parent 相対範囲の基準範囲
	 * @param a 相対座標
	 * @return 相対範囲
	 */
	public @Nonnull R child(final @Nonnull R a) {
		return new R(this, a);
	}

	@Override
	public @Nonnull String toString() {
		return String.format("RArea[parent=%s, x1=%s, y1=%s, x2=%s, y2=%s]", this.parent, this.x1, this.y1, this.x2, this.y2);
	}
}