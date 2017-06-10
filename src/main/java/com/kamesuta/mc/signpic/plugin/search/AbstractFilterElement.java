package com.kamesuta.mc.signpic.plugin.search;

public abstract class AbstractFilterElement<E, T extends DataFilterProperty<?>> implements DataFilterElement {

	public final T property;
	public final E data;

	public AbstractFilterElement(final T property, final E data) {
		this.property = property;
		this.data = data;
	}
}
