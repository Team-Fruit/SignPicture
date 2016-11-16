package com.kamesuta.mc.bnnwidget.position;

public class P extends C {
	protected C b;
	protected C c;

	public P(final C a, final C b, final C c) {
		super(a);
		this.b = b;
		this.c = c;
	}

	@Override
	public float get() {
		return this.c.get();
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		final float ca = super.getAbsCoord(0, a+b);
		final float cb = this.b.getAbsCoord(0, a+b);
		return this.c.getAbsCoord(ca, cb);
	}

	public static P of(final C start, final C end, final C percent) {
		return new P(start, end, percent);
	}
}
