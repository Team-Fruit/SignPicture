package com.kamesuta.mc.signpic.plugin.gui.search;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.plugin.SignData;
import com.kamesuta.mc.signpic.plugin.gui.search.StringFilterElement.StringFilterProperty;

public class UniversalFilterElement implements IFilterElement {
	public final @Nullable String str;

	public UniversalFilterElement(String src) {
		this.str = src;
	}

	@Override
	public boolean filter(SignData data) {
		for (StringFilterProperty line : StringFilterProperty.values())
			if (new StringFilterElement.EqualsIgnoreCaseStringFilterElement(line, this.str).filter(data))
				return true;
		return false;
	}

}
