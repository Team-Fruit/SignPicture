package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.plugin.SignData;

public interface IFilterProperty<E> {

	@Nullable
	E get(SignData data);
}
