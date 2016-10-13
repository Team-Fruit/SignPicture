package com.kamesuta.mc.bnnwidget.position;

public class RArea implements R {
	public static final Coord default_x1 = Coord.left(0f);
	public static final Coord default_y1 = Coord.top(0f);
	public static final Coord default_x2 = Coord.pwidth(1f);
	public static final Coord default_y2 = Coord.pheight(1f);

	protected Coord x1 = default_x1;
	protected Coord y1 = default_y1;
	protected Coord x2 = default_x2;
	protected Coord y2 = default_y2;

	public RArea(final Coord... a) {
		for (final Coord c : a) {
			set(c);
		}
	}

	public static RArea diff(final float diff_x1, final float diff_y1, final float diff_x2, final float diff_y2) {
		return new RArea(Coord.left(diff_x1), Coord.top(diff_y1), Coord.right(-diff_x2), Coord.bottom(-diff_y2));
	}

	public Area build(final Area a) {
		final float tx1 = this.x1.base(a);
		final float ty1 = this.y1.base(a);
		final float tx2 = this.x2.next(a, this.x1);
		final float ty2 = this.y2.next(a, this.y1);
		final float rx1 = Math.min(tx1, tx2);
		final float ry1 = Math.min(ty1, ty2);
		final float rx2 = Math.max(tx1, tx2);
		final float ry2 = Math.max(ty1, ty2);
		return new Area(rx1, ry1, rx2, ry2);
	}

	protected void set(final Coord n) {
		if (n==null) throw new IllegalStateException(String.format("null coord [%s]", this));
		if (n.side==null) throw new IllegalStateException(String.format("invaild coord [%s]", this));
		switch(n.side) {
		case Left:
		case Right:
			if (this.x1 == default_x1) {
				this.x1 = n;
				break;
			}
		case Width:
			if (this.x2 == default_x2) {
				this.x2 = n;
				break;
			}
		case Top:
		case Bottom:
			if (this.y1 == default_y1) {
				this.y1 = n;
				break;
			}
		case Height:
			if (this.y2 == default_y2) {
				this.y2 = n;
				break;
			}
			throw new IllegalStateException(String.format("conflic coord [%s]", this));
		}
	}

	@Override
	public boolean isVaild() {
		return true;
	}

	@Override
	public Area getAbsolute(final Area parent) {
		return build(parent);
	}

	@Override
	public String toString() {
		return String.format("RArea[x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}
}