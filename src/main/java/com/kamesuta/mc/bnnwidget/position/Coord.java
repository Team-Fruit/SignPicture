package com.kamesuta.mc.bnnwidget.position;

public class Coord {
	private final float coord;
	private CoordSide side;
	private CoordType type;

	public Coord(final float coord, final CoordSide side, final CoordType type) {
		this.coord = coord;
		this.side = side;
		this.type = type;
	}

	public Coord(final float coord) {
		this(coord, CoordSide.Top, CoordType.Percent);
	}

	public float get() {
		return this.coord;
	}

	public float getAbsCoord(final float abslength) {
		return getType().calc(0, abslength, get());
	}

	@Override
	public String toString() {
		return String.format("Coord [coord=%s, side=%s, type=%s]", get(), getSide(), getType());
	}

	public float base(final Area a) {
		return getSide().base(a, this);
	}

	public float next(final Area a, final Coord base) {
		return getSide().next(a, base, this);
	}

	public CoordSide getSide() {
		return this.side;
	}

	protected CoordType getType() {
		return this.type;
	}

	public static enum CoordSide {
		Top(true, 1) {
			@Override
			public float base(final Area a, final Coord c) {
				return a.y1()+c.getAbsCoord(a.h());
			}

			@Override
			public float next(final Area a, final Coord base, final Coord c) {
				if (c.getSide().isAbs)
					return a.y1()+c.getAbsCoord(a.h());
				else
					return base.base(a)+base.getSide().calc*c.getAbsCoord(a.h());
			}
		},
		Left(true, 1) {
			@Override
			public float base(final Area a, final Coord c) {
				return a.x1()+c.getAbsCoord(a.w());
			}

			@Override
			public float next(final Area a, final Coord base, final Coord c) {
				if (c.getSide().isAbs)
					return a.x1()+c.getAbsCoord(a.w());
				else
					return base.base(a)+base.getSide().calc*c.getAbsCoord(a.w());
			}
		},
		Bottom(true, -1) {
			@Override
			public float base(final Area a, final Coord c) {
				return a.y2()-c.getAbsCoord(a.h());
			}

			@Override
			public float next(final Area a, final Coord base, final Coord c) {
				if (c.getSide().isAbs)
					return a.y2()-c.getAbsCoord(a.h());
				else
					return base.base(a)+base.getSide().calc*c.getAbsCoord(a.h());
			}
		},
		Right(true, -1) {
			@Override
			public float base(final Area a, final Coord c) {
				return a.x2()-c.getAbsCoord(a.w());
			}

			@Override
			public float next(final Area a, final Coord base, final Coord c) {
				if (c.getSide().isAbs)
					return a.x2()-c.getAbsCoord(a.w());
				else
					return base.base(a)+base.getSide().calc*c.getAbsCoord(a.w());
			}
		},
		Width(false, 1) {
			@Override
			public float next(final Area a, final Coord base, final Coord c) {
				return base.getSide().next(a, base, c);
			}
		},
		Height(false, 1) {
			@Override
			public float next(final Area a, final Coord base, final Coord c) {
				return base.getSide().next(a, base, c);
			}
		},
		;

		public float base(final Area a, final Coord c) {
			return 0;
		}

		public float next(final Area a, final Coord base, final Coord c) {
			return 0;
		}

		public final boolean isAbs;
		private final int calc;

		private CoordSide(final boolean isAbs, final int calc) {
			this.isAbs = isAbs;
			this.calc = calc;
		}
	}

	public static enum CoordType {
		Absolute {
			@Override
			public float calc(final float a, final float b, final float c) {
				return c;
			}
		},
		Percent {
			@Override
			public float calc(final float a, final float b, final float c) {
				return a*(1f-c)+b*c;
			}
		},
		;

		public abstract float calc(float a, float b, float f);
	}

	public static Coord top(final float n) {
		return new Coord(n, CoordSide.Top, CoordType.Absolute);
	}

	public static Coord ptop(final float n) {
		return new Coord(n, CoordSide.Top, CoordType.Percent);
	}

	public static Coord left(final float n) {
		return new Coord(n, CoordSide.Left, CoordType.Absolute);
	}

	public static Coord pleft(final float n) {
		return new Coord(n, CoordSide.Left, CoordType.Percent);
	}

	public static Coord bottom(final float n) {
		return new Coord(n, CoordSide.Bottom, CoordType.Absolute);
	}

	public static Coord pbottom(final float n) {
		return new Coord(n, CoordSide.Bottom, CoordType.Percent);
	}

	public static Coord right(final float n) {
		return new Coord(n, CoordSide.Right, CoordType.Absolute);
	}

	public static Coord pright(final float n) {
		return new Coord(n, CoordSide.Right, CoordType.Percent);
	}

	public static Coord width(final float n) {
		return new Coord(n, CoordSide.Width, CoordType.Absolute);
	}

	public static Coord pwidth(final float n) {
		return new Coord(n, CoordSide.Width, CoordType.Percent);
	}

	public static Coord height(final float n) {
		return new Coord(n, CoordSide.Height, CoordType.Absolute);
	}

	public static Coord pheight(final float n) {
		return new Coord(n, CoordSide.Height, CoordType.Percent);
	}
}
