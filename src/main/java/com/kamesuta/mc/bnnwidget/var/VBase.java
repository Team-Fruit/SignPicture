package com.kamesuta.mc.bnnwidget.var;

import javax.annotation.Nonnull;

public class VBase implements VCommon {
	private final float coord;
	private @Nonnull VType type;

	public VBase(final float coord, final @Nonnull VType type) {
		this.coord = coord;
		this.type = type;
	}

	@Override
	public float get() {
		return this.coord;
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		return this.type.calc(a, b, get());
	}
}