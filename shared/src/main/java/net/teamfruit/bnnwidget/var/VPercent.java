package net.teamfruit.bnnwidget.var;

import javax.annotation.Nonnull;

/**
 * 割合の値
 *
 * @author TeamFruit
 */
public class VPercent implements VCommon {
	private @Nonnull VCommon a;
	private @Nonnull VCommon b;
	private @Nonnull VCommon c;

	/**
	 * 割合の値を作成します
	 * @param a 始点
	 * @param b 終点
	 * @param c 相対的な値
	 */
	public VPercent(final @Nonnull VCommon a, final @Nonnull VCommon b, final @Nonnull VCommon c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public float get() {
		return this.c.get();
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		final float ca = this.a.getAbsCoord(a, b);
		final float cb = this.b.getAbsCoord(a, b);
		return this.c.getAbsCoord(ca, cb);
	}

	@Override
	public String toString() {
		return String.format("VPercent [start=%s, end=%s, percent=%s]", this.a, this.b, this.c);
	}
}
