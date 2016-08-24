package com.kamesuta.mc.guiwidget.position;

import com.kamesuta.mc.guiwidget.position.relative.RBase;

public class RArea extends RBase {
	protected Coord x1;
	protected Coord y1;
	protected Coord x2;
	protected Coord y2;
	protected Coord w;
	protected Coord h;

	public RArea(final Coord a, final Coord b, final Coord c, final Coord d, final boolean isAnchor) {
		super(isAnchor);
		set(a);
		set(b);
		set(c);
		set(d);
	}

	public Area build(final Area a) {
		if (!check_x() || !check_y()) throw new IllegalStateException("insufficient coors");
		final int px = a.anc_x();
		final int py = a.anc_y();
		final int px1 = a.minx();
		final int py1 = a.miny();
		final int px2 = a.maxx();
		final int py2 = a.maxy();
		final int tx1 = base_x(a);
		final int ty1 = base_y(a);
		final int tx2 = next_x(a, tx1);
		final int ty2 = next_y(a, ty1);
		final int rx1 = Math.min(tx1, tx2);
		final int ry1 = Math.min(ty1, ty2);
		final int rx2 = Math.max(tx1, tx2);
		final int ry2 = Math.max(ty1, ty2);
		final int ax1 = Math.max(px1, rx1);
		final int ay1 = Math.max(py1, ry1);
		final int ax2 = Math.min(px2, rx2);
		final int ay2 = Math.min(py2, ry2);
		final int ax = this.isAnchor ? ax1 : px;
		final int ay = this.isAnchor ? ay1 : py;
		return new Area(ax, ay, ax1, ay1, ax2, ay2);
	}

	protected int base_x(final Area a) {
		switch (this.x1.side) {
		case Left:
			return a.minx() + this.x1.getAbsCoord(a.w());
		case Right:
			return a.maxx() - this.x1.getAbsCoord(a.w());
		default:
			return 0;
		}
	}

	protected int base_y(final Area a) {
		switch (this.y1.side) {
		case Top:
			return a.miny() + this.y1.getAbsCoord(a.h());
		case Bottom:
			return a.maxy() - this.y1.getAbsCoord(a.h());
		default:
			return 0;
		}
	}

	protected int next_x(final Area a, final int base) {
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
			return a.minx() + this.x2.getAbsCoord(a.w());
		case Right:
			return a.maxx() - this.x2.getAbsCoord(a.w());
		default:
			return 0;
		}
	}

	protected int next_y(final Area a, final int base) {
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
			return a.miny() + this.y2.getAbsCoord(a.h());
		case Bottom:
			return a.maxy() - this.y2.getAbsCoord(a.h());
		default:
			return 0;
		}
	}

	protected boolean exists(final Coord c) {
		return c!=null;
	}

	protected void set(final Coord n) {
		if (n==null) throw new IllegalStateException("null coord");
		if (n.side==null) throw new IllegalStateException("invaild coord");
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
		default:
			throw new IllegalStateException("unknown type");
		}
	}

	protected boolean check_x() {
		return this.x1!=null && (this.x2!=null || this.w!=null);
	}

	protected boolean check_y() {
		return this.y1!=null && (this.y2!=null || this.h!=null);
	}

	@Override
	public boolean isVaild() {
		return true;
	}

	@Override
	public Area getAbsolute(final Area parent) {
		return build(parent);
	}
}