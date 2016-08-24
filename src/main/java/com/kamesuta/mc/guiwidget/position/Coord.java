package com.kamesuta.mc.guiwidget.position;

public class Coord {
	public int coord;
	public CoordSide side;
	public CoordType type;

	public Coord(final int coord, final CoordSide side, final CoordType type) {
		this.coord = coord;
		this.side = side;
		this.type = type;
	}

	public int getAbsCoord(final int abslength) {
		return this.type.calc(abslength, this.coord);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.coord;
		result = prime * result + ((this.side == null) ? 0 : this.side.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Coord))
			return false;
		final Coord other = (Coord) obj;
		if (this.coord != other.coord)
			return false;
		if (this.side != other.side)
			return false;
		if (this.type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Coord [coord=%s, side=%s, type=%s]", this.coord, this.side, this.type);
	}

	public static enum CoordSide {
		Top,
		Left,
		Bottom,
		Right,
		Width,
		Height,
	}

	public static enum CoordType {
		Absolute {
			@Override
			public int calc(final int all, final int c) {
				return c;
			}
		},
		Percent {
			@Override
			public int calc(final int all, final int c) {
				return all * c / 100;
			}
		},
		;

		public abstract int calc(int all, int c);
	}

	public static Coord absolute(final int n, final CoordSide side) {
		return new Coord(n, side, CoordType.Absolute);
	}

	public static Coord percent(final int n, final CoordSide side) {
		return new Coord(n, side, CoordType.Percent);
	}

	public static Coord unknown(final String n, final CoordSide side) {
		if (n.endsWith("%")) {
			final int n1 = Integer.parseInt(n.substring(0, n.length()-1));
			return percent(n1, side);
		} else {
			final int n1 = Integer.parseInt(n);
			return absolute(n1, side);
		}
	}

	public static Coord top(final int n) {
		return absolute(n, CoordSide.Top);
	}

	public static Coord top(final String n) {
		return unknown(n, CoordSide.Top);
	}

	public static Coord left(final int n) {
		return absolute(n, CoordSide.Left);
	}

	public static Coord left(final String n) {
		return unknown(n, CoordSide.Left);
	}

	public static Coord bottom(final int n) {
		return absolute(n, CoordSide.Bottom);
	}

	public static Coord bottom(final String n) {
		return unknown(n, CoordSide.Bottom);
	}

	public static Coord right(final int n) {
		return absolute(n, CoordSide.Right);
	}

	public static Coord right(final String n) {
		return unknown(n, CoordSide.Right);
	}

	public static Coord width(final int n) {
		return absolute(n, CoordSide.Width);
	}

	public static Coord width(final String n) {
		return unknown(n, CoordSide.Width);
	}

	public static Coord height(final int n) {
		return absolute(n, CoordSide.Height);
	}

	public static Coord height(final String n) {
		return unknown(n, CoordSide.Height);
	}
}
