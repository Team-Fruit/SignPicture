package com.kamesuta.mc.guiwidget.position.relative;

import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.PositionAbsolute;

public class AbsolutePosition extends RelativeBase {
	public final int x;
	public final int y;
	public final int w;
	public final int h;

	public AbsolutePosition(final int x, final int y, final int w, final int h, final boolean isAnchor) {
		super(isAnchor);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	public IPositionAbsolute getAbsolute(final IPositionAbsolute parent) {
		final int px = parent.x();
		final int py = parent.y();
		final int px1 = parent.x1();
		final int py1 = parent.y1();
		final int px2 = parent.x2();
		final int py2 = parent.y2();
		final int abs_x1 = px1 + this.x;
		final int abs_y1 = py1 + this.y;
		final int abs_x2 = abs_x1 + this.w;
		final int abs_y2 = abs_y1 + this.h;
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
	public boolean isVaild() {
		return this.w > 0 && this.h > 0;
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
		if (!(obj instanceof AbsolutePosition))
			return false;
		final AbsolutePosition other = (AbsolutePosition) obj;
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
		return String.format("AbsolutePosition [x=%s, y=%s, w=%s, h=%s]", this.x, this.y, this.w, this.h);
	}
}
