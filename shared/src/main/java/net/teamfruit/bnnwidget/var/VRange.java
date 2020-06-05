package net.teamfruit.bnnwidget.var;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 範囲のある値
 *
 * @author TeamFruit
 */
public class VRange implements VCommon {
	private @Nullable VCommon min;
	private @Nullable VCommon max;
	private @Nonnull VCommon var;

	/**
	 * 範囲のある値を作成します
	 * @param min 最小値
	 * @param max 最大値
	 * @param var 値
	 */
	public VRange(final @Nullable VCommon min, final @Nullable VCommon max, final @Nonnull VCommon var) {
		this.min = min;
		this.max = max;
		this.var = var;
	}

	@Override
	public float get() {
		return this.var.get();
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		float v = this.var.getAbsCoord(a, b);
		if (this.min!=null) {
			final float min = this.min.getAbsCoord(a, b);
			v = Math.max(min, v);
		}
		if (this.max!=null) {
			final float max = this.max.getAbsCoord(a, b);
			v = Math.min(max, v);
		}
		return v;
	}

	@Override
	public String toString() {
		return String.format("VRange [min=%s, max=%s, var=%s]", this.min, this.max, this.var);
	}
}
