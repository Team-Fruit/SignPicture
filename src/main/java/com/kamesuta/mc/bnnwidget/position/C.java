package com.kamesuta.mc.bnnwidget.position;

public class C {
	protected final float coord;
	protected CoordType type;

	public C(final float coord, final CoordType type) {
		this.coord = coord;
		this.type = type;
	}

	public C(final C coord) {
		this(coord.coord, coord.type);
	}

	public float get() {
		return this.coord;
	}

	public float getAbsCoord(final float a, final float b) {
		return this.type.calc(a, b, get());
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

	public static C a(final float n) {
		return new C(n, CoordType.Absolute);
	}

	public static C p(final float n) {
		return new C(n, CoordType.Percent);
	}
}
