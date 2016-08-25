package com.kamesuta.mc.guiwidget.position.legacy;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.RBase;

public class RRelativeSized extends RBase {
	public float x;
	public float y;
	public float w;
	public float h;

	public RRelativeSized(final float x, final float y, final float w, final float h, final boolean isAnchor) {
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
	public Area getAbsolute(final Area parent) {
		final float px = parent.anc_x();
		final float py = parent.anc_y();
		final float px1 = parent.x1();
		final float py1 = parent.y1();
		final float px2 = parent.x2();
		final float py2 = parent.y2();
		final float cx1 = this.x;
		final float cy1 = this.y;
		final float cx2 = this.x+this.w;
		final float cy2 = this.y+this.h;
		final float abs_x1 = (cx1>=0) ? px1+cx1 : px2+cx1+1;
		final float abs_y1 = (cy1>=0) ? py1+cy1 : py2+cy1+1;
		final float abs_x2 = (cx2>=0) ? px1+cx2 : px2+cx2+1;
		final float abs_y2 = (cy2>=0) ? py1+cy2 : py2+cy2+1;
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
		return String.format("RelativeSizedPosition [x=%s, y=%s, w=%s, h=%s]", this.x, this.y, this.w, this.h);
	}
}
