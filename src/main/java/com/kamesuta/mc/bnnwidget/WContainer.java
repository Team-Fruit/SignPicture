package com.kamesuta.mc.bnnwidget;

import java.util.List;

import javax.annotation.Nonnull;

public interface WContainer<W extends WCommon> {

	boolean remove(final @Nonnull W widget);

	boolean add(final @Nonnull W widget);

	@Nonnull
	List<W> getContainer();

}
