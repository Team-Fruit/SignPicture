package com.kamesuta.mc.bnnwidget.position;

public class Area {
	/**
	 * anchor x, usually, same as
	 */
	protected final float x1;
	protected final float y1;
	protected final float x2;
	protected final float y2;

	public Area(final float ax1, final float ay1, final float ax2, final float ay2) {
		this.x1 = ax1;
		this.y1 = ay1;
		this.x2 = ax2;
		this.y2 = ay2;
	}

	public float w() {
		return Math.abs(this.x2 - this.x1);
	}

	public float h() {
		return Math.abs(this.y2 - this.y1);
	}

	public float x1() {
		return this.x1;
	}

	public float y1() {
		return this.y1;
	}

	public float x2() {
		return this.x2;
	}

	public float y2() {
		return this.y2;
	}

	public Area child(final R p) {
		return p.getAbsolute(this);
	}

	public Area child(final float ax1, final float ay1, final float ax2, final float ay2) {
		return new Area(this.x1 + ax1, this.y1 + ay1, this.x2 + ax2, this.y2 + ay2);
	}

	public boolean isVaild() {
		return this.x1!=this.x2 && this.y1!=this.y2;
	}

	public final boolean pointInside(final Point p) {
		return p.x >= this.x1 && p.x < this.x2 && p.y >= this.y1 && p.y < this.y2;
	}

}
