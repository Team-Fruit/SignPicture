package com.kamesuta.mc.bnnwidget.var;

import java.util.Set;

import com.google.common.collect.Sets;

public class VCompound implements VCommon {
	protected VCommon coord;
	protected Set<VCommon> coords;

	public VCompound(final VCommon a, final VCommon b) {
		this.coord = a;
		this.coords = Sets.newHashSet(b);
	}

	@Override
	public float get() {
		float f = this.coord.get();
		for (final VCommon c : this.coords)
			f += c.get();
		return f;
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		float f = this.coord.getAbsCoord(a, b);
		for (final VCommon c : this.coords)
			f += c.getAbsCoord(0, b-a);
		return f;
	}
}