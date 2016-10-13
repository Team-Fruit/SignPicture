package com.kamesuta.mc.bnnwidget.position;

public class RArea implements R {
	protected Coord x1;
	protected Coord y1;
	protected Coord x2;
	protected Coord y2;

	public RArea(final Coord a, final Coord b, final Coord c, final Coord d) {
		set(a);
		set(b);
		set(c);
		set(d);
	}

	public static RArea diff(final float diff_x1, final float diff_y1, final float diff_x2, final float diff_y2) {
		return new RArea(Coord.left(diff_x1), Coord.top(diff_y1), Coord.right(-diff_x2), Coord.bottom(-diff_y2));
	}

	public Area build(final Area a) {
		if (!check_x() || !check_y()) throw new IllegalStateException(String.format("insufficient coors [%s]", this));
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

	protected boolean exists(final Coord c) {
		return c!=null;
	}

	protected boolean hasAbs(final Coord a, final Coord b) {
		return a.side.isAbs || b.side.isAbs;
	}

	protected void set(final Coord n) {
		if (n==null) throw new IllegalStateException(String.format("null coord [%s]", this));
		if (n.side==null) throw new IllegalStateException(String.format("invaild coord [%s]", this));
		switch(n.side) {
		case Right:
		case Left:
		case Width:
			if (this.x1 == null) this.x1 = n;
			else if (this.x2 == null) this.x2 = n;
			break;
		case Top:
		case Bottom:
		case Height:
			if (this.y1 == null) this.y1 = n;
			else if (this.y2 == null) this.y2 = n;
			break;
		}
	}

	protected boolean check_x() {
		return exists(this.x1) && (exists(this.x2));
	}

	protected boolean check_y() {
		return exists(this.y1) && (exists(this.y2));
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
		if (check_x() && check_y())
			return String.format("RArea[x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
		else
			return String.format("RArea(Invaild)[x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}
}