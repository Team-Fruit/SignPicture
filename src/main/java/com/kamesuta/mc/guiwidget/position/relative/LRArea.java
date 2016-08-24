package com.kamesuta.mc.guiwidget.position.relative;

import com.kamesuta.mc.guiwidget.position.Area;

public class LRArea extends RBase {
	public int x1;
	public int y1;
	public int x2;
	public int y2;

	public LRArea(final int x1, final int y1, final int x2, final int y2, final boolean isAnchor) {
		super(isAnchor);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public boolean isVaild() {
		return this.x1!=this.x2 && this.y1!=this.y2;
	}

	@Override
	public Area getAbsolute(final Area parent) {
		final int px = parent.anc_x();
		final int py = parent.anc_y();
		final int px1 = parent.x1();
		final int py1 = parent.y1();
		final int px2 = parent.x2();
		final int py2 = parent.y2();
		final int abs_x1 = (this.x1>=0) ? px1+this.x1 : px2+this.x1+1;
		final int abs_y1 = (this.y1>=0) ? py1+this.y1 : py2+this.y1+1;
		final int abs_x2 = (this.x2>=0) ? px1+this.x2 : px2+this.x2+1;
		final int abs_y2 = (this.y2>=0) ? py1+this.y2 : py2+this.y2+1;
		final int rx1 = Math.min(abs_x1, abs_x2);
		final int ry1 = Math.min(abs_y1, abs_y2);
		final int rx2 = Math.max(abs_x1, abs_x2);
		final int ry2 = Math.max(abs_y1, abs_y2);
		final int ax1 = Math.max(px1, rx1);
		final int ay1 = Math.max(py1, ry1);
		final int ax2 = Math.min(px2, rx2);
		final int ay2 = Math.min(py2, ry2);
		final int ax = this.isAnchor ? ax1 : px;
		final int ay = this.isAnchor ? ay1 : py;
		return new Area(ax, ay, ax1, ay1, ax2, ay2);
	}

	public static LRArea createFit(final boolean isAnchor) {
		return new LRArea(0, 0, -1, -1, isAnchor);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + this.x1;
		result = prime * result + this.x2;
		result = prime * result + this.y1;
		result = prime * result + this.y2;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof LRArea))
			return false;
		final LRArea other = (LRArea) obj;
		if (this.x1 != other.x1)
			return false;
		if (this.x2 != other.x2)
			return false;
		if (this.y1 != other.y1)
			return false;
		if (this.y2 != other.y2)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("RelativePosition [x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}
}
