package com.kamesuta.mc.bnnwidget.position;

public class Point implements java.io.Serializable {

	protected float x;

	protected float y;

	private static final long serialVersionUID = -5276940640259749850L;

	public Point() {
		this(0, 0);
	}

	public Point(final Point p) {
		this(p.x, p.y);
	}

	public Point(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public float x() {
		return this.x;
	}

	public float y() {
		return this.y;
	}

	@Override
	public String toString() {
		return String.format("Point[x=%s, y=%s]", this.x, this.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.x);
		result = prime * result + Float.floatToIntBits(this.y);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point))
			return false;
		final Point other = (Point) obj;
		if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
}
