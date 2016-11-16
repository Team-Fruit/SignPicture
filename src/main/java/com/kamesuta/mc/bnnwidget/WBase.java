package com.kamesuta.mc.bnnwidget;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.C;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;

public abstract class WBase extends WComponent {

	protected R position;
	protected C opacity;

	public WBase(final R position) {
		initPosition(position);
		initOpacity();
	}

	public void setPosition(final R position) {
		this.position = position;
	}

	public void setOpacity(final C c) {
		this.opacity = c;
	}

	protected void initPosition(final R position) {
		setPosition(position);
	}

	protected void initOpacity() {
		setOpacity(C.p(1f));
	}

	public R getGuiPosition() {
		return this.position;
	}

	public C getGuiOpacity() {
		return this.opacity;
	}

	public Area getGuiPosition(final Area pgp) {
		return pgp.child(getGuiPosition());
	}

	public float getGuiOpacity(final float popacity) {
		return getGuiOpacity().getAbsCoord(0, popacity);
	}

	@Override
	public WCommon top(final WEvent ev, final Area pgp, final Point point) {
		final Area a = getGuiPosition(pgp);
		if (a.pointInside(point))
			return this;
		return null;
	}
}
