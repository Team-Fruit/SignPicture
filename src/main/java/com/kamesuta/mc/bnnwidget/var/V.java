package com.kamesuta.mc.bnnwidget.var;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class V {
	public static @Nonnull VBase a(final float n) {
		return new VBase(n, VType.Absolute);
	}

	public static @Nonnull VBase p(final float n) {
		return new VBase(n, VType.Percent);
	}

	public static @Nonnull VMotion am(final float n) {
		return new VMotion(n, VType.Absolute);
	}

	public static @Nonnull VMotion pm(final float n) {
		return new VMotion(n, VType.Percent);
	}

	public static @Nonnull VPercent per(final @Nonnull VCommon start, final @Nonnull VCommon end, final @Nonnull VCommon percent) {
		return new VPercent(start, end, percent);
	}

	public static @Nonnull VRange range(final @Nullable VCommon min, final @Nullable VCommon max, final @Nonnull VCommon var) {
		return new VRange(min, max, var);
	}

	public static @Nonnull VRange min(final @Nonnull VCommon min, final @Nonnull VCommon var) {
		return range(min, null, var);
	}

	public static @Nonnull VRange max(final @Nonnull VCommon max, final @Nonnull VCommon var) {
		return range(null, max, var);
	}

	public static @Nonnull VCompound combine(final @Nonnull VCommon a, final @Nonnull VCommon b) {
		return new VCompound(a, b);
	}
}
