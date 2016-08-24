package com.kamesuta.mc.guiwidget.position.relative;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.RBase;

public class RAbsolute extends RBase {
	public final float x;
	public final float y;
	public final float w;
	public final float h;

	public RAbsolute(final float x, final float y, final float w, final float h, final boolean isAnchor) {
		super(isAnchor);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	public Area getAbsolute(final Area parent) {
		final float px = parent.anc_x();
		final float py = parent.anc_y();
		final float px1 = parent.x1();
		final float py1 = parent.y1();
		final float px2 = parent.x2();
		final float py2 = parent.y2();
		final float abs_x1 = px1 + this.x;
		final float abs_y1 = py1 + this.y;
		final float abs_x2 = abs_x1 + this.w;
		final float abs_y2 = abs_y1 + this.h;
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
	public boolean isVaild() {
		return this.w > 0 && this.h > 0;
	}

	@Override
	public String toString() {
		return String.format("AbsolutePosition [x=%s, y=%s, w=%s, h=%s]", this.x, this.y, this.w, this.h);
	}
}
