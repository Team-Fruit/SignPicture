package com.kamesuta.mc.guiwidget.position.legacy;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.RBase;

public class LRArea extends RBase {
	public float x1;
	public float y1;
	public float x2;
	public float y2;

	public LRArea(final float x1, final float y1, final float x2, final float y2, final boolean isAnchor) {
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
		final float px = parent.anc_x();
		final float py = parent.anc_y();
		final float px1 = parent.x1();
		final float py1 = parent.y1();
		final float px2 = parent.x2();
		final float py2 = parent.y2();
		final float abs_x1 = (this.x1>=0) ? px1+this.x1 : px2+this.x1+1;
		final float abs_y1 = (this.y1>=0) ? py1+this.y1 : py2+this.y1+1;
		final float abs_x2 = (this.x2>=0) ? px1+this.x2 : px2+this.x2+1;
		final float abs_y2 = (this.y2>=0) ? py1+this.y2 : py2+this.y2+1;
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

	public static LRArea createFit(final boolean isAnchor) {
		return new LRArea(0, 0, -1, -1, isAnchor);
	}

	@Override
	public String toString() {
		return String.format("RelativePosition [x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}
}
