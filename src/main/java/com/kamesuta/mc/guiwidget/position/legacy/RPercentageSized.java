package com.kamesuta.mc.guiwidget.position.legacy;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.RBase;

public class RPercentageSized extends RBase {
	public final float per_x;
	public final float per_y;
	public final float per_w;
	public final float per_h;

	public RPercentageSized(final float per_x, final float per_y, final float per_w, final float per_h, final boolean isAnchor) {
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
	public Area getAbsolute(final Area parent) {
		final float px = parent.anc_x();
		final float py = parent.anc_y();
		final float px1 = parent.x1();
		final float py1 = parent.y1();
		final float px2 = parent.x2();
		final float py2 = parent.y2();
		final float pw = px2-px1;
		final float ph = py2-py1;
		final float cx1 = px * this.per_x;
		final float cy1 = py * this.per_y;
		final float cw = pw * this.per_w;
		final float ch = ph * this.per_h;
		final float abs_x1 = (cx1>=0) ? px1+cx1 : px2+cx1+1-cw;
		final float abs_y1 = (cy1>=0) ? py1+cy1 : py2+cy1+1-ch;
		final float abs_x2 = (cx1>=0) ? px1+cx1+cw : px2+cx1+1;
		final float abs_y2 = (cy1>=0) ? py1+cy1+ch : py2+cy1+1;
		final float rx1 = Math.min(abs_x1, abs_x2);
		final float ry1 = Math.min(abs_y1, abs_y2);
		final float rx2 = Math.max(abs_x1, abs_x2);
		final float ry2 = Math.max(abs_y1, abs_y2);
		final float ax1 = Math.max(px1, rx1);
		final float ay1 = Math.max(py1, ry1);
		final float ax2 = Math.min(px2, rx2);
		final float ay2 = Math.min(py2, ry2);
		final float ax = this.isAnchor ? ax1 : px;
		final float ay = this.isAnchor ? ay1 : py;
		return new Area(ax, ay, ax1, ay1, ax2, ay2);
	}

	@Override
	public String toString() {
		return String.format("PercentageSizedPosition [per_x=%s, per_y=%s, per_w=%s, per_h=%s]", this.per_x, this.per_y, this.per_w,
				this.per_h);
	}
}
