package net.teamfruit.bnnwidget.position;

import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.var.V;
import net.teamfruit.bnnwidget.var.VCommon;

/**
 * 相対範囲を構成する相対座標
 *
 * @author TeamFruit
 */
public class Coord {
	private @Nonnull VCommon coord;
	private @Nonnull CoordSide side;

	public Coord(final @Nonnull VCommon coord, final @Nonnull CoordSide side) {
		this.coord = coord;
		this.side = side;
	}

	public float get() {
		return this.coord.get();
	}

	public float getAbsCoord(final float abslength) {
		return this.coord.getAbsCoord(0, abslength);
	}

	@Override
	public @Nonnull String toString() {
		return String.format("Coord [coord=%s, side=%s]", get(), getSide());
	}

	public float base(final @Nonnull Area a) {
		return getSide().base(a, this);
	}

	public float next(final @Nonnull Area a, final @Nonnull Coord base) {
		return getSide().next(a, base, this);
	}

	public @Nonnull CoordSide getSide() {
		return this.side;
	}

	public static enum CoordSide {
		Top(true, 1) {
			@Override
			public float base(final @Nonnull Area a, final @Nonnull Coord c) {
				return a.y1()+c.getAbsCoord(a.h());
			}

			@Override
			public float next(final @Nonnull Area a, final @Nonnull Coord base, final @Nonnull Coord c) {
				if (c.getSide().isAbs)
					return a.y1()+c.getAbsCoord(a.h());
				else
					return base.base(a)+base.getSide().calc*c.getAbsCoord(a.h());
			}
		},
		Left(true, 1) {
			@Override
			public float base(final @Nonnull Area a, final @Nonnull Coord c) {
				return a.x1()+c.getAbsCoord(a.w());
			}

			@Override
			public float next(final @Nonnull Area a, final @Nonnull Coord base, final @Nonnull Coord c) {
				if (c.getSide().isAbs)
					return a.x1()+c.getAbsCoord(a.w());
				else
					return base.base(a)+base.getSide().calc*c.getAbsCoord(a.w());
			}
		},
		Bottom(true, -1) {
			@Override
			public float base(final @Nonnull Area a, final @Nonnull Coord c) {
				return a.y2()-c.getAbsCoord(a.h());
			}

			@Override
			public float next(final @Nonnull Area a, final @Nonnull Coord base, final @Nonnull Coord c) {
				if (c.getSide().isAbs)
					return a.y2()-c.getAbsCoord(a.h());
				else
					return base.base(a)+base.getSide().calc*c.getAbsCoord(a.h());
			}
		},
		Right(true, -1) {
			@Override
			public float base(final @Nonnull Area a, final @Nonnull Coord c) {
				return a.x2()-c.getAbsCoord(a.w());
			}

			@Override
			public float next(final @Nonnull Area a, final @Nonnull Coord base, final @Nonnull Coord c) {
				if (c.getSide().isAbs)
					return a.x2()-c.getAbsCoord(a.w());
				else
					return base.base(a)+base.getSide().calc*c.getAbsCoord(a.w());
			}
		},
		Width(false, 1) {
			@Override
			public float next(final @Nonnull Area a, final @Nonnull Coord base, final @Nonnull Coord c) {
				return base.getSide().next(a, base, c);
			}
		},
		Height(false, 1) {
			@Override
			public float next(final @Nonnull Area a, final @Nonnull Coord base, final @Nonnull Coord c) {
				return base.getSide().next(a, base, c);
			}
		},
		;

		public float base(final @Nonnull Area a, final @Nonnull Coord c) {
			return 0;
		}

		public float next(final @Nonnull Area a, final @Nonnull Coord base, final @Nonnull Coord c) {
			return 0;
		}

		public final boolean isAbs;
		private final int calc;

		private CoordSide(final boolean isAbs, final int calc) {
			this.isAbs = isAbs;
			this.calc = calc;
		}
	}

	public static @Nonnull Coord top(final @Nonnull VCommon n) {
		return new Coord(n, CoordSide.Top);
	}

	public static @Nonnull Coord top(final float n) {
		return top(V.a(n));
	}

	public static @Nonnull Coord ptop(final float n) {
		return top(V.p(n));
	}

	public static @Nonnull Coord left(final @Nonnull VCommon n) {
		return new Coord(n, CoordSide.Left);
	}

	public static @Nonnull Coord left(final float n) {
		return left(V.a(n));
	}

	public static @Nonnull Coord pleft(final float n) {
		return left(V.p(n));
	}

	public static @Nonnull Coord bottom(final @Nonnull VCommon n) {
		return new Coord(n, CoordSide.Bottom);
	}

	public static @Nonnull Coord bottom(final float n) {
		return bottom(V.a(n));
	}

	public static @Nonnull Coord pbottom(final float n) {
		return bottom(V.p(n));
	}

	public static @Nonnull Coord right(final @Nonnull VCommon n) {
		return new Coord(n, CoordSide.Right);
	}

	public static @Nonnull Coord right(final float n) {
		return right(V.a(n));
	}

	public static @Nonnull Coord pright(final float n) {
		return right(V.p(n));
	}

	public static @Nonnull Coord width(final @Nonnull VCommon n) {
		return new Coord(n, CoordSide.Width);
	}

	public static @Nonnull Coord width(final float n) {
		return width(V.a(n));
	}

	public static @Nonnull Coord pwidth(final float n) {
		return width(V.p(n));
	}

	public static @Nonnull Coord height(final @Nonnull VCommon n) {
		return new Coord(n, CoordSide.Height);
	}

	public static @Nonnull Coord height(final float n) {
		return height(V.a(n));
	}

	public static @Nonnull Coord pheight(final float n) {
		return height(V.p(n));
	}
}
