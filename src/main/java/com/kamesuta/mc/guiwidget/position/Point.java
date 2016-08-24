package com.kamesuta.mc.guiwidget.position;

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
}
