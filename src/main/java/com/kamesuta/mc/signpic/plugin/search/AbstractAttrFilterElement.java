package com.kamesuta.mc.signpic.plugin.search;

public abstract class AbstractAttrFilterElement<E, T extends AttrFilterProperty<?>> implements AttrFilterElement {

	public final T property;
	public final E data;

	public AbstractAttrFilterElement(final T property, final E data) {
		this.property = property;
		this.data = data;
	}

}
