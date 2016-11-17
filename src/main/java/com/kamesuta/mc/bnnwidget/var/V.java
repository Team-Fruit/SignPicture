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

	public static VRange of(final VCommon start, final VCommon end, final VCommon percent) {
		return new VRange(start, end, percent);
	}

	public static VCompound combine(final VCommon a, final VCommon b) {
		return new VCompound(a, b);
	}
}
