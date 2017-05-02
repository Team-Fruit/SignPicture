package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.plugin.SignData;

public class AndFilterElement implements IFilterElement {

	public final @Nonnull IFilterElement filter1;
	public final @Nonnull IFilterElement filter2;

	public AndFilterElement(IFilterElement filter1, IFilterElement filter2) {
		this.filter1 = filter1;
		this.filter2 = filter2;
	}

	@Override
	public boolean filter(SignData data) {
		return this.filter1.filter(data)&&this.filter2.filter(data);
	}

}
