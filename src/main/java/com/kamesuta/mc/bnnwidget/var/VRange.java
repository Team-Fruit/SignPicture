package com.kamesuta.mc.bnnwidget.var;

public class VRange implements VCommon {
	private VCommon a;
	private VCommon b;
	private VCommon c;

	public VRange(final VCommon a, final VCommon b, final VCommon c) {
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
