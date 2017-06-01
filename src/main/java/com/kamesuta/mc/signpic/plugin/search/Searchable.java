package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nullable;

public interface Searchable {

	void filter(@Nullable FilterExpression expression);
}
