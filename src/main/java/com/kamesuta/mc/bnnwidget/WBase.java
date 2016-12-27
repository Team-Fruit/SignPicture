package com.kamesuta.mc.bnnwidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VCommon;

public abstract class WBase extends WComponent {
	protected @Nonnull R position;
	protected @Nonnull VCommon opacity;

	public WBase(final @Nonnull R position) {
		this.position = initPosition(position);
		this.opacity = initOpacity();
	}

	protected @Nonnull R initPosition(final @Nonnull R position) {
		return position;
	}

	protected @Nonnull VCommon initOpacity() {
		return V.p(1f);
	}

	public @Nonnull R getGuiPosition() {
		return this.position;
	}

	public @Nonnull VCommon getGuiOpacity() {
		return this.opacity;
	}

	public @Nonnull Area getGuiPosition(final @Nonnull Area pgp) {
		return pgp.child(getGuiPosition());
	}

	public float getGuiOpacity(final float popacity) {
		return getGuiOpacity().getAbsCoord(0, popacity);
	}

	@Override
	public @Nullable WCommon top(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point point) {
		final Area a = getGuiPosition(pgp);
		if (a.pointInside(point))
			return this;
		return null;
	}
}
