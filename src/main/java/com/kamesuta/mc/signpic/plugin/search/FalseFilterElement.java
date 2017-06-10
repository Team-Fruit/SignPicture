package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.plugin.SignData;

public class FalseFilterElement implements IFilterElement {
	public static final FalseFilterElement INSTANCE = new FalseFilterElement();

	@Override
	public boolean filter(final SignData data) {
		return false;
	}

}
