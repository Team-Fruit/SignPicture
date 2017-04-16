package com.kamesuta.mc.signpic.plugin.gui.search;

public abstract class EnumFilterElement<T extends Enum> implements IFilterElement {

	public final T property;

	public EnumFilterElement(final T property) {
		this.property = property;
	}
}
