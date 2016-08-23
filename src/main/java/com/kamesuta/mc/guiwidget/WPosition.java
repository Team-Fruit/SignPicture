package com.kamesuta.mc.guiwidget;

import javax.annotation.Nullable;

import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.relative.IPositionRelative;

public class WPosition {
	public final @Nullable WPosition parent;
	public final IPositionRelative position;
	public final IPositionAbsolute absolute;

	public WPosition(@Nullable final WPosition parent, @Nullable final IPositionRelative position, final IPositionAbsolute absolute) {
		this.parent = parent;
		this.position = position;
		this.absolute = absolute;
	}

	public WPosition child(final IPositionRelative position) {
		return new WPosition(this, position, this.absolute);
	}

	public IPositionAbsolute getAbsolute() {
		return getAbsolute(this.absolute, this);
	}

	protected IPositionAbsolute getAbsolute(final IPositionAbsolute absolute) {
		return getAbsolute(absolute, this);
	}

	protected IPositionAbsolute getAbsolute(final IPositionAbsolute absolute, @Nullable final WPosition parent) {
		if (parent != null && parent.position != null) {
			return parent.position.getAbsolute(parent.getAbsolute(absolute, parent.parent));
		} else
			return absolute;
	}

	public static WPosition createBase(final IPositionAbsolute absolute) {
		return new WPosition(null, null, absolute);
	}

	@Override
	public String toString() {
		return String.format("%s => %s", (this.parent!=null)?this.parent:"root", this.position);
	}

}
