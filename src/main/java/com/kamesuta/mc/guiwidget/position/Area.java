package com.kamesuta.mc.guiwidget.position;

import com.kamesuta.mc.guiwidget.position.relative.RCommon;

public class Area {
	/**
	 * anchor x, usually, same as
	 */
	protected final int x;
	protected final int y;
	protected final int x1;
	protected final int y1;
	protected final int x2;
	protected final int y2;

	public Area(final int x, final int y, final int x1, final int y1, final int x2, final int y2) {
		this.x = x;
		this.y = y;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public int anc_x() {
		return this.x;
	}

	public int anc_y() {
		return this.y;
	}

	public int w() {
		return Math.abs(this.x2 - this.x1);
	}

	public int h() {
		return Math.abs(this.y2 - this.y1);
	}

	public int x1() {
		return this.x1;
	}

	public int y1() {
		return this.y1;
	}

	public int x2() {
		return this.x2;
	}

	public int y2() {
		return this.y2;
	}

	public int minx() {
		return Math.min(this.x1, this.x2);
	}

	public int maxx() {
		return Math.max(this.x1, this.x2);
	}

	public int miny() {
		return Math.min(this.y1, this.y2);
	}

	public int maxy() {
		return Math.max(this.y1, this.y2);
	}

	public Area child(final RCommon p) {
		return p.getAbsolute(this);
	}

	public boolean isVaild() {
		return this.x1!=this.x2 && this.y1!=this.y2;
	}

	public final boolean pointInside(final Point p) {
		return p.x >= this.x1 && p.x < this.x2 && p.y >= this.y1 && p.y < this.y2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.x;
		result = prime * result + this.x1;
		result = prime * result + this.x2;
		result = prime * result + this.y;
		result = prime * result + this.y1;
		result = prime * result + this.y2;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Area))
			return false;
		final Area other = (Area) obj;
		if (this.x != other.x)
			return false;
		if (this.x1 != other.x1)
			return false;
		if (this.x2 != other.x2)
			return false;
		if (this.y != other.y)
			return false;
		if (this.y1 != other.y1)
			return false;
		if (this.y2 != other.y2)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("PositionAbsolute [x=%s, y=%s, x1=%s, y1=%s, x2=%s, y2=%s]", this.x, this.y, this.x1, this.y1, this.x2, this.y2);
	}
}
