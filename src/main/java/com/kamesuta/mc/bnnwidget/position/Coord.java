package com.kamesuta.mc.bnnwidget.position;

import com.kamesuta.mc.bnnwidget.position.C.CoordType;

public class Coord {
	private C coord;
	private CoordSide side;

	public Coord(final C coord, final CoordSide side) {
		this.coord = coord;
		this.side = side;
	}

	@Deprecated
	public Coord(final C coord) {
		this(coord, CoordSide.Top);
	}

	public float get() {
		return this.coord.get();
	}

	public float getAbsCoord(final float abslength) {
		return this.coord.getAbsCoord(0, abslength);
	}

	@Override
	public String toString() {
		return String.format("Coord [coord=%s, side=%s]", get(), getSide());
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

	public static Coord top(final C n) {
		return new Coord(n, CoordSide.Top);
	}

	public static Coord top(final float n) {
		return Coord.top(new C(n, CoordType.Absolute));
	}

	public static Coord ptop(final float n) {
		return Coord.top(new C(n, CoordType.Percent));
	}

	public static Coord left(final C n) {
		return new Coord(n, CoordSide.Left);
	}

	public static Coord left(final float n) {
		return left(new C(n, CoordType.Absolute));
	}

	public static Coord pleft(final float n) {
		return left(new C(n, CoordType.Percent));
	}

	public static Coord bottom(final C n) {
		return new Coord(n, CoordSide.Bottom);
	}

	public static Coord bottom(final float n) {
		return bottom(new C(n, CoordType.Absolute));
	}

	public static Coord pbottom(final float n) {
		return bottom(new C(n, CoordType.Percent));
	}

	public static Coord right(final C n) {
		return new Coord(n, CoordSide.Right);
	}

	public static Coord right(final float n) {
		return right(new C(n, CoordType.Absolute));
	}

	public static Coord pright(final float n) {
		return right(new C(n, CoordType.Percent));
	}

	public static Coord width(final C n) {
		return new Coord(n, CoordSide.Width);
	}

	public static Coord width(final float n) {
		return width(new C(n, CoordType.Absolute));
	}

	public static Coord pwidth(final float n) {
		return width(new C(n, CoordType.Percent));
	}

	public static Coord height(final C n) {
		return new Coord(n, CoordSide.Height);
	}

	public static Coord height(final float n) {
		return height(new C(n, CoordType.Absolute));
	}

	public static Coord pheight(final float n) {
		return height(new C(n, CoordType.Percent));
	}
}
