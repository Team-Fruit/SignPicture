package com.kamesuta.mc.guiwidget.position;

public class Point implements java.io.Serializable {

	public int x;

	public int y;

	private static final long serialVersionUID = -5276940640259749850L;

	public Point() {
		this(0, 0);
	}

	public Point(final Point p) {
		this(p.x, p.y);
	}

	public Point(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	@Override
	public String toString() {
		return String.format("Point[x=%s, y=%s]", this.x, this.y);
	}
}
