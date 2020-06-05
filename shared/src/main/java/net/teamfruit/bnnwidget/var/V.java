package net.teamfruit.bnnwidget.var;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 様々な値を作成します
 *
 * @author TeamFruit
 */
public abstract class V {
	/**
	 * 絶対的な値
	 * @param n 値
	 * @return 絶対的な値
	 */
	public static @Nonnull VBase a(final float n) {
		return new VBase(n, VType.Absolute);
	}

	/**
	 * 相対的な値
	 * @param n 値
	 * @return 相対的な値
	 */
	public static @Nonnull VBase p(final float n) {
		return new VBase(n, VType.Percent);
	}

	/**
	 * 絶対的な動きのある値
	 * @param n 値
	 * @return 絶対的な動きのある値
	 */
	public static @Nonnull VMotion am(final float n) {
		return new VMotion(n, VType.Absolute);
	}

	/**
	 * 相対的な動きのある値
	 * @param n 値
	 * @return 相対的な動きのある値
	 */
	public static @Nonnull VMotion pm(final float n) {
		return new VMotion(n, VType.Percent);
	}

	/**
	 * startからendまでのpercent%の値をとります
	 * @param start 値
	 * @param end 値
	 * @param percent 相対的な値
	 * @return startからendまでのpercent%の値
	 */
	public static @Nonnull VPercent per(final @Nonnull VCommon start, final @Nonnull VCommon end, final @Nonnull VCommon percent) {
		return new VPercent(start, end, percent);
	}

	/**
	 * varをminからminまでの範囲内に収めます
	 * @param min 最小値
	 * @param max 最大値
	 * @param var 値
	 * @return varをminからminまでの範囲内に収めまた値
	 */
	public static @Nonnull VRange range(final @Nullable VCommon min, final @Nullable VCommon max, final @Nonnull VCommon var) {
		return new VRange(min, max, var);
	}

	/**
	 * varはmin以上になります
	 * @param min 最小値
	 * @param var 値
	 * @return min以上のvar
	 */
	public static @Nonnull VRange min(final @Nonnull VCommon min, final @Nonnull VCommon var) {
		return range(min, null, var);
	}

	/**
	 * varはmin以下になります
	 * @param max 最大値
	 * @param var 値
	 * @return min以上のvar
	 */
	public static @Nonnull VRange max(final @Nonnull VCommon max, final @Nonnull VCommon var) {
		return range(null, max, var);
	}

	/**
	 * 複数の値を足した値を作ります
	 * @param a ベースとなる値
	 * @param b 足す値
	 * @return 値
	 */
	public static @Nonnull VCompound combine(final @Nonnull VCommon a, final @Nonnull VCommon... b) {
		return new VCompound(a, b);
	}
}
