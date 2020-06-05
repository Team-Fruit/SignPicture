package net.teamfruit.bnnwidget.position;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 絶対座標の点を表現します
 *
 * @author TeamFruit
 */
public class Point implements java.io.Serializable {
	/**
	 * X絶対座標
	 */
	protected float x;
	/**
	 * Y絶対座標
	 */
	protected float y;

	private static final long serialVersionUID = -5276940640259749850L;

	/**
	 * (0, 0)の絶対座標
	 */
	public Point() {
		this(0, 0);
	}

	/**
	 * (p.x, p.y)の絶対座標
	 * @param p 絶対座標
	 */
	public Point(final @Nonnull Point p) {
		this(p.x, p.y);
	}

	/**
	 * (x, y)の絶対座標
	 * @param x 絶対座標
	 * @param y 絶対座標
	 */
	public Point(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * X絶対座標
	 */
	public float x() {
		return this.x;
	}

	/**
	 * Y絶対座標
	 */
	public float y() {
		return this.y;
	}

	/**
	 * 2点の距離の2乗
	 * @param p 絶対座標
	 * @return 2点の距離の2乗
	 */
	public float d2(final @Nonnull Point p) {
		final float dx = x()-p.x();
		final float dy = y()-p.y();
		return dx*dx+dy*dy;
	}

	/**
	 * 2点の距離
	 * <p>
	 * ※距離の比較の場合は{@link #d2(Point)}(距離の2乗)のほうがより軽量です
	 * @param p
	 * @return 2点の距離
	 */
	public float d(final @Nonnull Point p) {
		return (float) Math.sqrt(d2(p));
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Point[x=%s, y=%s]", this.x, this.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result+Float.floatToIntBits(this.x);
		result = prime*result+Float.floatToIntBits(this.y);
		return result;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof Point))
			return false;
		final Point other = (Point) obj;
		if (Float.floatToIntBits(this.x)!=Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(this.y)!=Float.floatToIntBits(other.y))
			return false;
		return true;
	}
}
