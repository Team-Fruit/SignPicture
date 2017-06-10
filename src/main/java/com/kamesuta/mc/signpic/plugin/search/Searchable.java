package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;
import com.kamesuta.mc.signpic.plugin.SignData;

public interface Searchable {

	void filter(@Nullable FilterExpression expression);

	IModCount<SignData> getNow();

	boolean isSearching();
}
