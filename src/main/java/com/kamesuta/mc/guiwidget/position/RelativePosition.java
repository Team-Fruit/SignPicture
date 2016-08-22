package com.kamesuta.mc.guiwidget.position;

public class RelativePosition implements IPositionRelative {

	public int x1;
	public int y1;
	public int x2;
	public int y2;

	public RelativePosition(final int x1, final int y1, final int x2, final int y2) {
		set(x1, y1, x2, y2);
	}

	public RelativePosition set(final int x1, final int y1, final int x2, final int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		return this;
	}

	@Override
	public boolean isVaild() {
		return this.x1!=this.x2 && this.y1!=this.y2;
	}

	@Override
	public IPositionAbsolute getAbsolute(final IPositionAbsolute parent) {
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
		return new PositionAbsolute(
				Math.max(px1, rx1),
				Math.max(py1, ry1),
				Math.min(px2, rx2),
				Math.min(py2, ry2));
	}

	@Override
	public String toString() {
		return String.format("RelativePosition[x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}

}
