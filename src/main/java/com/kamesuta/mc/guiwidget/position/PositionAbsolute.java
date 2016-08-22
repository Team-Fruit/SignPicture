package com.kamesuta.mc.guiwidget.position;

public class PositionAbsolute implements IPositionAbsolute {
	public final int x1;
	public final int y1;
	public final int x2;
	public final int y2;

	public PositionAbsolute(final int x1, final int y1, final int x2, final int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public int x1() {
		return this.x1;
	}

	@Override
	public int y1() {
		return this.y1;
	}

	@Override
	public int x2() {
		return this.x2;
	}

	@Override
	public int y2() {
		return this.y2;
	}

	@Override
	public boolean isVaild() {
		return this.x1!=this.x2 && this.y1!=this.y2;
	}

	@Override
	public final boolean pointInside(final Point p) {
		return p.x >= this.x1 && p.x < this.x2 && p.y >= this.y1 && p.y < this.y2;
	}

	@Override
	public String toString() {
		return String.format("absolute[x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}

}
