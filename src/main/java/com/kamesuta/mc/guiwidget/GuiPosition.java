package com.kamesuta.mc.guiwidget;

import javax.annotation.Nullable;

import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;
import com.kamesuta.mc.guiwidget.position.IPositionRelative;

public class GuiPosition {
	public final @Nullable GuiPosition parent;
	public final IPositionRelative position;
	public final IPositionAbsolute absolute;

	public GuiPosition(@Nullable final GuiPosition parent, final IPositionRelative position, final IPositionAbsolute absolute) {
		this.parent = parent;
		this.position = position;
		this.absolute = absolute;
	}

	public GuiPosition child(final IPositionRelative position) {
		return new GuiPosition(this, position, this.absolute);
	}

	public IPositionAbsolute getAbsolute() {
		return getAbsolute(this.absolute, this);
	}

	public IPositionAbsolute getAbsolute(final IPositionAbsolute absolute) {
		return getAbsolute(absolute, this);
	}

	private IPositionAbsolute getAbsolute(final IPositionAbsolute absolute, @Nullable final GuiPosition parent) {
		if (parent != null) {
			return parent.position.getAbsolute(parent.getAbsolute(absolute, parent.parent));
		} else
			return absolute;
	}

	@Override
	public String toString() {
		return String.format("%s => %s", (this.parent!=null)?this.parent:"root", this.position);
	}

}
