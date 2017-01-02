package com.kamesuta.mc.bnnwidget.var;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VRange implements VCommon {
	private @Nullable VCommon min;
	private @Nullable VCommon max;
	private @Nonnull VCommon var;

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
}
