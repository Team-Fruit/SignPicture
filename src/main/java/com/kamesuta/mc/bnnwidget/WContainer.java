package com.kamesuta.mc.bnnwidget;

import java.util.List;

public interface WContainer {

	boolean remove(final WCommon widget);

	boolean add(final WCommon widget);

	List<WCommon> getContainer();

}
