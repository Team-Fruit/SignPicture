package com.kamesuta.mc.guiwidget.position;

public class SimplePosition implements IPositionRelative {
	public final int x;
	public final int y;
	public final int w;
	public final int h;

	public SimplePosition(final int x, final int y, final int w, final int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	public IPositionAbsolute getAbsolute(final IPositionAbsolute parent) {
		final int px1 = parent.x1();
		final int py1 = parent.y1();
		final int px2 = parent.x2();
		final int py2 = parent.y2();
		final int abs_x1 = px1 + this.x;
		final int abs_y1 = py1 + this.y;
		final int abs_x2 = abs_x1 + this.w;
		final int abs_y2 = abs_y1 + this.h;
		return new PositionAbsolute(
				Math.max(px1, abs_x1),
				Math.max(py1, abs_y1),
				Math.min(px2, abs_x2),
				Math.min(py2, abs_y2));
	}

	@Override
	public boolean isVaild() {
		return this.w > 0 && this.h > 0;
	}
}
