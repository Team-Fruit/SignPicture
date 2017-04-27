package com.kamesuta.mc.signpic.plugin.gui.search;

public abstract class EnumFilterElement<E, T extends IFilterProperty<?>> implements IFilterElement {

	public final T property;

	public EnumFilterElement(final T property) {
		this.property = property;
	}
}
