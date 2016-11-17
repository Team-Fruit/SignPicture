package com.kamesuta.mc.bnnwidget.var;

public class VRange implements VCommon {
	private VCommon min;
	private VCommon max;
	private VCommon var;

	public VRange(final VCommon min, final VCommon max, final VCommon var) {
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
}
