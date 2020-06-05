package net.teamfruit.emojicord.gui;

public class Rectangle2d {
	private int x;
	private int y;
	private int width;
	private int height;

	public Rectangle2d(final int xIn, final int yIn, final int widthIn, final int heightIn) {
		this.x = xIn;
		this.y = yIn;
		this.width = widthIn;
		this.height = heightIn;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Rectangle2d inner(final int left, final int top, final int right, final int bottom) {
		return new Rectangle2d(this.x+left, this.y+top, this.width-(left+right), this.height-(top+bottom));
	}

	public Rectangle2d outer(final int left, final int top, final int right, final int bottom) {
		return new Rectangle2d(this.x-left, this.y-top, this.width+left+right, this.height+top+bottom);
	}

	public boolean contains(final int x, final int y) {
		return x>=this.x&&x<=this.x+this.width&&y>=this.y&&y<=this.y+this.height;
	}

	public boolean contains(final Rectangle2d rect) {
		final int x = rect.getX();
		final int y = rect.getY();
		final int x2 = x+rect.getWidth();
		final int y2 = y+rect.getHeight();
		return x>=this.x&&x2<=this.x+this.width&&y>=this.y&&y2<=this.y+this.height;
	}

	public boolean overlap(final Rectangle2d rect) {
		final int x = rect.getX();
		final int y = rect.getY();
		final int x2 = x+rect.getWidth();
		final int y2 = y+rect.getHeight();
		return x2>=this.x&&x<=this.x+this.width&&y2>=this.y&&y<=this.y+this.height;
	}
}