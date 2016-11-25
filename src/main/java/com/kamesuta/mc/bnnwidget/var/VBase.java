package com.kamesuta.mc.bnnwidget.var;

public class VBase implements VCommon {
	private final float coord;
	private VType type;

	public VBase(final float coord, final VType type) {
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