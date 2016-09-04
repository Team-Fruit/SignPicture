package com.kamesuta.mc.bnnwidget.position;

public class RArea implements R {
	protected Coord x1;
	protected Coord y1;
	protected Coord x2;
	protected Coord y2;
	protected Coord w;
	protected Coord h;

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
		final float tx1 = base_x(a);
		final float ty1 = base_y(a);
		final float tx2 = next_x(a, tx1);
		final float ty2 = next_y(a, ty1);
		final float rx1 = Math.min(tx1, tx2);
		final float ry1 = Math.min(ty1, ty2);
		final float rx2 = Math.max(tx1, tx2);
		final float ry2 = Math.max(ty1, ty2);
		return new Area(rx1, ry1, rx2, ry2);
	}

	protected float base_x(final Area a) {
		switch (this.x1.side) {
		case Left:
			return a.x1() + this.x1.getAbsCoord(a.w());
		case Right:
			return a.x2() - this.x1.getAbsCoord(a.w());
		default:
			return 0;
		}
	}

	protected float base_y(final Area a) {
		switch (this.y1.side) {
		case Top:
			return a.y1() + this.y1.getAbsCoord(a.h());
		case Bottom:
			return a.y2() - this.y1.getAbsCoord(a.h());
		default:
			return 0;
		}
	}

	protected float next_x(final Area a, final float base) {
		if (exists(this.w)) {
			switch(this.x1.side) {
			case Left:
				return base + this.w.getAbsCoord(a.w());
			case Right:
				return base - this.w.getAbsCoord(a.w());
			default:
				return 0;
			}
		}
		switch (this.x2.side) {
		case Left:
			return a.x1() + this.x2.getAbsCoord(a.w());
		case Right:
			return a.x2() - this.x2.getAbsCoord(a.w());
		default:
			return 0;
		}
	}

	protected float next_y(final Area a, final float base) {
		if (exists(this.h)) {
			switch(this.y1.side) {
			case Top:
				return base + this.h.getAbsCoord(a.h());
			case Bottom:
				return base - this.h.getAbsCoord(a.h());
			default:
				return 0;
			}
		}
		switch (this.y2.side) {
		case Top:
			return a.y1() + this.y2.getAbsCoord(a.h());
		case Bottom:
			return a.y2() - this.y2.getAbsCoord(a.h());
		default:
			return 0;
		}
	}

	protected boolean exists(final Coord c) {
		return c!=null;
	}

	protected void set(final Coord n) {
		if (n==null) throw new IllegalStateException(String.format("null coord [%s]", this));
		if (n.side==null) throw new IllegalStateException(String.format("invaild coord [%s]", this));
		switch(n.side) {
		case Top:
			if (this.y1 == null) this.y1 = n;
			else if (this.y2 == null) this.y2 = n;
			break;
		case Left:
			if (this.x1 == null) this.x1 = n;
			else if (this.x2 == null) this.x2 = n;
			break;
		case Bottom:
			if (this.y1 == null) this.y1 = n;
			else if (this.y2 == null) this.y2 = n;
			break;
		case Right:
			if (this.x1 == null) this.x1 = n;
			else if (this.x2 == null) this.x2 = n;
			break;
		case Width:
			this.w = n;
			break;
		case Height:
			this.h = n;
			break;
		}
	}

	protected boolean check_x() {
		return exists(this.x1) && (exists(this.x2) || exists(this.w));
	}

	protected boolean check_y() {
		return exists(this.y1) && (exists(this.y2) || exists(this.h));
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
			return String.format("RArea[%s=%s, %s=%s, %s, %s]", exists(this.x2)?"x1":"x", this.x1, exists(this.y2)?"y1":"y", this.y1, exists(this.x2)?"x2="+this.x2:"w="+this.w, exists(this.y2)?"y2="+this.y2:"h="+this.h);
		else
			return String.format("RArea(Invaild)[x1=%s, y1=%s, x2=%s, y2=%s, w=%s, h=%s]", this.x1, this.y1, this.x2, this.y2, this.w, this.h);
	}
}