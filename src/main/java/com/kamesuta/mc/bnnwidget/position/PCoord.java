package com.kamesuta.mc.bnnwidget.position;

public class PCoord extends Coord {
	protected Coord a;
	protected Coord b;
	protected Coord c;

	public PCoord(final Coord a, final Coord b, final Coord c) {
		super(0);
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	protected CoordType getType() {
		return this.c.getType();
	}

	@Override
	public CoordSide getSide() {
		return this.c.getSide();
	}

	@Override
	public float get() {
		return this.c.get();
	}

	@Override
	public float getAbsCoord(final float abslength) {
		final float a = this.a.getAbsCoord(abslength);
		final float b = this.b.getAbsCoord(abslength);
		return getType().calc(a, b, this.c.get());
	}

	public static PCoord of(Coord start, Coord end, final Coord percent) {
		if (start==null)
			start = new Coord(0);
		if (end==null)
			end = new Coord(1);
		return new PCoord(start, end, percent);
	}
}
