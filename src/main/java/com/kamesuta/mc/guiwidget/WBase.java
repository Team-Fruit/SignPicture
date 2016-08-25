package com.kamesuta.mc.guiwidget;

import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.R;

public abstract class WBase extends WComponent {

	protected R position;

	public WBase(final R position) {
		this.position = position;
	}

	public R getGuiRelative() {
		return this.position;
	}

	public Area getGuiPosition(final Area pgp) {
		return pgp.child(getGuiRelative());
	}
}
