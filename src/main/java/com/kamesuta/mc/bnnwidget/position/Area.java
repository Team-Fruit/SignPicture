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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.x1);
		result = prime * result + Float.floatToIntBits(this.x2);
		result = prime * result + Float.floatToIntBits(this.y1);
		result = prime * result + Float.floatToIntBits(this.y2);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Area))
			return false;
		final Area other = (Area) obj;
		if (Float.floatToIntBits(this.x1) != Float.floatToIntBits(other.x1))
			return false;
		if (Float.floatToIntBits(this.x2) != Float.floatToIntBits(other.x2))
			return false;
		if (Float.floatToIntBits(this.y1) != Float.floatToIntBits(other.y1))
			return false;
		if (Float.floatToIntBits(this.y2) != Float.floatToIntBits(other.y2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Area [x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}
}
