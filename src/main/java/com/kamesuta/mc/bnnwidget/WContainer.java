package com.kamesuta.mc.bnnwidget;

import java.util.List;

public interface WContainer<W extends WCommon> {

	boolean remove(final W widget);

	boolean add(final W widget);

	List<W> getContainer();

}
