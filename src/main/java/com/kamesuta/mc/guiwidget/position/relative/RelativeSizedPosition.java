package com.kamesuta.mc.guiwidget.position.relative;

import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.PositionAbsolute;

public class RelativeSizedPosition extends RelativeBase {
	public int x;
	public int y;
	public int w;
	public int h;

	public RelativeSizedPosition(final int x, final int y, final int w, final int h, final boolean isAnchor) {
		super(isAnchor);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	public boolean isVaild() {
		return 0!=this.w && 0!=this.h;
	}

	@Override
	public IPositionAbsolute getAbsolute(final IPositionAbsolute parent) {
		final int px = parent.x();
		final int py = parent.y();
		final int px1 = parent.x1();
		final int py1 = parent.y1();
		final int px2 = parent.x2();
		final int py2 = parent.y2();
		final int cx1 = this.x;
		final int cy1 = this.y;
		final int cx2 = this.x+this.w;
		final int cy2 = this.y+this.h;
		final int abs_x1 = (cx1>=0) ? px1+cx1 : px2+cx1+1;
		final int abs_y1 = (cy1>=0) ? py1+cy1 : py2+cy1+1;
		final int abs_x2 = (cx2>=0) ? px1+cx2 : px2+cx2+1;
		final int abs_y2 = (cy2>=0) ? py1+cy2 : py2+cy2+1;
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
		return new PositionAbsolute(ax, ay, ax1, ay1, ax2, ay2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + this.h;
		result = prime * result + this.w;
		result = prime * result + this.x;
		result = prime * result + this.y;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof RelativeSizedPosition))
			return false;
		final RelativeSizedPosition other = (RelativeSizedPosition) obj;
		if (this.h != other.h)
			return false;
		if (this.w != other.w)
			return false;
		if (this.x != other.x)
			return false;
		if (this.y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("RelativeSizedPosition [x=%s, y=%s, w=%s, h=%s]", this.x, this.y, this.w, this.h);
	}
}
