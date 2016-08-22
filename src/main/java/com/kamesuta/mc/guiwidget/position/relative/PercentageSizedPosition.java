package com.kamesuta.mc.guiwidget.position.relative;

import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.PositionAbsolute;

public class PercentageSizedPosition extends RelativeBase {
	public final float per_x;
	public final float per_y;
	public final float per_w;
	public final float per_h;

	public PercentageSizedPosition(final float per_x, final float per_y, final float per_w, final float per_h, final boolean isAnchor) {
		super(isAnchor);
		this.per_x = per_x;
		this.per_y = per_y;
		this.per_w = per_w;
		this.per_h = per_h;
	}

	@Override
	public boolean isVaild() {
		return this.per_w > 0 && this.per_h > 0;
	}

	@Override
	public IPositionAbsolute getAbsolute(final IPositionAbsolute parent) {
		final int px = parent.x();
		final int py = parent.y();
		final int px1 = parent.x1();
		final int py1 = parent.y1();
		final int px2 = parent.x2();
		final int py2 = parent.y2();
		final int pw = px2-px1;
		final int ph = py2-py1;
		final int cx1 = (int) (px * this.per_x);
		final int cy1 = (int) (py * this.per_y);
		final int cw = (int) (pw * this.per_w);
		final int ch = (int) (ph * this.per_h);
		final int abs_x1 = (cx1>=0) ? px1+cx1 : px2+cx1+1-cw;
		final int abs_y1 = (cy1>=0) ? py1+cy1 : py2+cy1+1-ch;
		final int abs_x2 = (cx1>=0) ? px1+cx1+cw : px2+cx1+1;
		final int abs_y2 = (cy1>=0) ? py1+cy1+ch : py2+cy1+1;
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
		result = prime * result + Float.floatToIntBits(this.per_h);
		result = prime * result + Float.floatToIntBits(this.per_w);
		result = prime * result + Float.floatToIntBits(this.per_x);
		result = prime * result + Float.floatToIntBits(this.per_y);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof PercentageSizedPosition))
			return false;
		final PercentageSizedPosition other = (PercentageSizedPosition) obj;
		if (Float.floatToIntBits(this.per_h) != Float.floatToIntBits(other.per_h))
			return false;
		if (Float.floatToIntBits(this.per_w) != Float.floatToIntBits(other.per_w))
			return false;
		if (Float.floatToIntBits(this.per_x) != Float.floatToIntBits(other.per_x))
			return false;
		if (Float.floatToIntBits(this.per_y) != Float.floatToIntBits(other.per_y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("PercentageSizedPosition [per_x=%s, per_y=%s, per_w=%s, per_h=%s]", this.per_x, this.per_y, this.per_w,
				this.per_h);
	}
}
