package com.kamesuta.mc.bnnwidget.var;

public abstract class V {
	public static VBase a(final float n) {
		return new VBase(n, VType.Absolute);
	}

	public static VBase p(final float n) {
		return new VBase(n, VType.Percent);
	}

	public static VMotion am(final float n) {
		return new VMotion(n, VType.Absolute);
	}

	public static VMotion pm(final float n) {
		return new VMotion(n, VType.Percent);
	}

	public static VPercent per(final VCommon start, final VCommon end, final VCommon percent) {
		return new VPercent(start, end, percent);
	}

	public static VRange range(final VCommon min, final VCommon max, final VCommon var) {
		return new VRange(min, max, var);
	}

	public static VRange min(final VCommon min, final VCommon var) {
		return range(min, null, var);
	}

	public static VRange max(final VCommon max, final VCommon var) {
		return range(null, max, var);
	}

	public static VCompound combine(final VCommon a, final VCommon b) {
		return new VCompound(a, b);
	}
}
