package com.kamesuta.mc.bnnwidget.position;

import java.util.Set;

import com.google.common.collect.Sets;

public class Coords extends Coord {
	protected Coord coord;
	protected Set<Coord> coords;

	public Coords(final Coord a, final Coord... b) {
		super(0);
		this.coord = a;
		this.coords = Sets.newHashSet(b);
	}

	@Override
	public CoordSide getSide() {
		return this.coord.getSide();
	}

	@Override
	public float get() {
		return this.coord.get();
	}

	@Override
	public float getAbsCoord(final float abslength) {
		float f = this.coord.getAbsCoord(abslength);
		for (final Coord c : this.coords)
			f += c.getAbsCoord(abslength);
		return f;
	}

	public static Coords combine(final Coord a, final Coord... b) {
		return new Coords(a, b);
	}
}