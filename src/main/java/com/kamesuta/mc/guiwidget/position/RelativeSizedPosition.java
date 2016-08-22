package com.kamesuta.mc.guiwidget.position;

public class RelativeSizedPosition implements IPositionRelative {

	public int x;
	public int y;
	public int w;
	public int h;

	public RelativeSizedPosition(final int x, final int y, final int w, final int h) {
		set(x, y, w, h);
	}

	public RelativeSizedPosition set(final int x, final int y, final int w, final int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		return this;
	}

	@Override
	public boolean isVaild() {
		return this.x!=this.w && this.y!=this.h;
	}

	@Override
	public IPositionAbsolute getAbsolute(final IPositionAbsolute parent) {
		final int px1 = parent.x1();
		final int py1 = parent.y1();
		final int px2 = parent.x2();
		final int py2 = parent.y2();
		final int abs_x1 = (this.x>=0) ? px1+this.x : px2+this.x+1-this.w;
		final int abs_y1 = (this.y>=0) ? py1+this.y : py2+this.y+1-this.h;
		final int abs_x2 = (this.x>=0) ? px1+this.x+this.w : px2+this.x+1;
		final int abs_y2 = (this.y>=0) ? py1+this.y+this.h : py2+this.y+1;
		final int rx1 = Math.min(abs_x1, abs_x2);
		final int ry1 = Math.min(abs_y1, abs_y2);
		final int rx2 = Math.max(abs_x1, abs_x2);
		final int ry2 = Math.max(abs_y1, abs_y2);
		return new PositionAbsolute(
				Math.max(px1, rx1),
				Math.max(py1, ry1),
				Math.min(px2, rx2),
				Math.min(py2, ry2));
	}

	@Override
	public String toString() {
		return String.format("RelativePosition[x=%s, y=%s, w=%s, h=%s]", this.x, this.y, this.w, this.h);
	}

}
