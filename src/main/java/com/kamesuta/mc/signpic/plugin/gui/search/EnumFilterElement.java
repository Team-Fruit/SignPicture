package com.kamesuta.mc.signpic.plugin.gui.search;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.plugin.SignData;

public abstract class EnumFilterElement<E, T extends Enum> implements IFilterElement {

	public final T property;

	public EnumFilterElement(final T property) {
		this.property = property;
	}

	protected abstract @Nullable E get(SignData data);
}
