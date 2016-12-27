package com.kamesuta.mc.bnnwidget.position;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

public class R {
	private static final @Nonnull Coord default_x1 = Coord.left(0f);
	private static final @Nonnull Coord default_y1 = Coord.top(0f);
	private static final @Nonnull Coord default_x2 = Coord.pwidth(1f);
	private static final @Nonnull Coord default_y2 = Coord.pheight(1f);

	protected @Nullable R parent;
	protected @Nullable Coord x1 = default_x1;
	protected @Nullable Coord y1 = default_y1;
	protected @Nullable Coord x2 = default_x2;
	protected @Nullable Coord y2 = default_y2;

	public R(final @Nonnull Coord... a) {
		for (final Coord c : a)
			set(c);
	}

	public R(final @Nonnull R parent, final @Nonnull Coord... a) {
		this(a);
		setParent(parent);
	}

	public R(final @Nonnull R a) {
		set(a);
	}

	public R(final @Nonnull R parent, final @Nonnull R a) {
		this(a);
		setParent(parent);
	}

	@Deprecated
	public static @Nonnull R diff(final float diff_x1, final float diff_y1, final float diff_x2, final float diff_y2) {
		return new R(Coord.left(diff_x1), Coord.top(diff_y1), Coord.right(-diff_x2), Coord.bottom(-diff_y2));
	}

	public @Nonnull Area getAbsolute(@Nonnull Area a) {
		final Coord x1 = this.x1;
		final Coord y1 = this.y1;
		final Coord x2 = this.x2;
		final Coord y2 = this.y2;
		if (x1!=null&&y1!=null&&x2!=null&&y2!=null) {
			if (this.parent!=null)
				a = this.parent.getAbsolute(a);
			final float tx1 = x1.base(a);
			final float ty1 = y1.base(a);
			final float tx2 = x2.next(a, x1);
			final float ty2 = y2.next(a, y1);
			final float rx1 = Math.min(tx1, tx2);
			final float ry1 = Math.min(ty1, ty2);
			final float rx2 = Math.max(tx1, tx2);
			final float ry2 = Math.max(ty1, ty2);
			return new Area(rx1, ry1, rx2, ry2);
		} else
			throw new IllegalStateException("attempt to get absolute from uncompleted relative");
	}

	protected void setParent(final @Nonnull R parent) {
		this.parent = parent;
	}

	protected void set(final @Nonnull R a) {
		this.x1 = a.x1;
		this.y1 = a.y1;
		this.x2 = a.x2;
		this.y2 = a.y2;
	}

	protected void set(final @Nonnull Coord n) {
		Validate.notNull(n, String.format("null coord [%s]", this));
		Validate.notNull(n.getSide(), String.format("invaild coord [%s]", this));
		switch (n.getSide()) {
			case Left:
			case Right:
				if (this.x1==default_x1) {
					this.x1 = n;
					break;
				}
			case Width:
				if (this.x2==default_x2) {
					this.x2 = n;
					break;
				}
			case Top:
			case Bottom:
				if (this.y1==default_y1) {
					this.y1 = n;
					break;
				}
			case Height:
				if (this.y2==default_y2) {
					this.y2 = n;
					break;
				}
				throw new IllegalStateException(String.format("conflic coord [%s]", this));
		}
	}

	public @Nonnull R child(final @Nonnull Coord... a) {
		return new R(this, a);
	}

	public @Nonnull R child(final @Nonnull R a) {
		return new R(this, a);
	}

	@Override
	public @Nonnull String toString() {
		return String.format("RArea[x1=%s, y1=%s, x2=%s, y2=%s]", this.x1, this.y1, this.x2, this.y2);
	}
}