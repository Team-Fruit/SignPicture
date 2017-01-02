package com.kamesuta.mc.bnnwidget.var;

import javax.annotation.Nonnull;

public class VPercent implements VCommon {
	private @Nonnull VCommon a;
	private @Nonnull VCommon b;
	private @Nonnull VCommon c;

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
}
