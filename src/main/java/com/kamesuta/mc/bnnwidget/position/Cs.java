package com.kamesuta.mc.bnnwidget.position;

import java.util.Set;

import com.google.common.collect.Sets;

public class Cs extends C {
	protected Set<C> coords;

	public Cs(final C a, final C... b) {
		super(a);
		this.coords = Sets.newHashSet(b);
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		float f = super.getAbsCoord(a, b);
		for (final C c : this.coords)
			f += c.getAbsCoord(a, b);
		return f;
	}

	public static Cs combine(final C a, final C... b) {
		return new Cs(a, b);
	}
}