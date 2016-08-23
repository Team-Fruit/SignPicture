package com.kamesuta.mc.guiwidget;

import java.util.List;

public interface WContainer {

	boolean remove(final WCommon widget);

	boolean add(final WCommon widget);

	List<WCommon> getContainer();

}
