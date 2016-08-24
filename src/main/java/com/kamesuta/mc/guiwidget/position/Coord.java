package com.kamesuta.mc.guiwidget.position;

import com.kamesuta.mc.guiwidget.animation.Motion;
import com.kamesuta.mc.guiwidget.animation.MotionQueue;

public class Coord {
	public final MotionQueue motion;
	public CoordSide side;
	public CoordType type;

	public Coord(final float coord, final CoordSide side, final CoordType type) {
		this.motion = new MotionQueue(coord);
		this.side = side;
		this.type = type;
	}

	public float get() {
		return (float) this.motion.get();
	}

	public float getAbsCoord(final float abslength) {
		return this.type.calc(abslength, get());
	}

	public Coord add(final Motion animation) {
		this.motion.add(animation);
		return this;
	}

	@Override
	public String toString() {
		return String.format("Coord [coord=%s, side=%s, type=%s]", get(), this.side, this.type);
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
			public float calc(final float all, final float c) {
				return c;
			}
		},
		Percent {
			@Override
			public float calc(final float all, final float c) {
				return all * c / 100;
			}
		},
		;

		public abstract float calc(float all, float f);
	}

	public static Coord absolute(final float n, final CoordSide side) {
		return new Coord(n, side, CoordType.Absolute);
	}

	public static Coord percent(final float n, final CoordSide side) {
		return new Coord(n, side, CoordType.Percent);
	}

	public static Coord unknown(final String n, final CoordSide side) {
		if (n.endsWith("%")) {
			final float n1 = Integer.parseInt(n.substring(0, n.length()-1));
			return percent(n1, side);
		} else {
			final float n1 = Integer.parseInt(n);
			return absolute(n1, side);
		}
	}

	public static Coord top(final float n) {
		return absolute(n, CoordSide.Top);
	}

	public static Coord top(final String n) {
		return unknown(n, CoordSide.Top);
	}

	public static Coord left(final float n) {
		return absolute(n, CoordSide.Left);
	}

	public static Coord left(final String n) {
		return unknown(n, CoordSide.Left);
	}

	public static Coord bottom(final float n) {
		return absolute(n, CoordSide.Bottom);
	}

	public static Coord bottom(final String n) {
		return unknown(n, CoordSide.Bottom);
	}

	public static Coord right(final float n) {
		return absolute(n, CoordSide.Right);
	}

	public static Coord right(final String n) {
		return unknown(n, CoordSide.Right);
	}

	public static Coord width(final float n) {
		return absolute(n, CoordSide.Width);
	}

	public static Coord width(final String n) {
		return unknown(n, CoordSide.Width);
	}

	public static Coord height(final float n) {
		return absolute(n, CoordSide.Height);
	}

	public static Coord height(final String n) {
		return unknown(n, CoordSide.Height);
	}
}
