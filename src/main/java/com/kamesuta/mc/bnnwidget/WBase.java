package com.kamesuta.mc.bnnwidget;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public abstract class WBase extends WComponent {

	protected R position;
	protected Coord opacity;

	public WBase(final R position) {
		initPosition(position);
		initOpacity();
	}

	public void setPosition(final R position) {
		this.position = position;
	}

	public void setOpacity(final Coord opacity) {
		this.opacity = opacity;
	}

	protected void initPosition(final R position) {
		setPosition(position);
	}

	protected void initOpacity() {
		setOpacity(new Coord(1f));
	}

	public R getGuiPosition() {
		return this.position;
	}

	public Coord getGuiOpacity() {
		return this.opacity;
	}

	public Area getGuiPosition(final Area pgp) {
		return pgp.child(getGuiPosition());
	}

	public float getGuiOpacity(final float popacity) {
		return getGuiOpacity().getAbsCoord(popacity);
	}

	@Override
	public WCommon top(final WEvent ev, final Area pgp, final Point point) {
		final Area a = getGuiPosition(pgp);
		if (a.pointInside(point))
			return this;
		return null;
	}
}
