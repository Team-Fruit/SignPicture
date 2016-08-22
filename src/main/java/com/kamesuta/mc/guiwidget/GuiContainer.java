package com.kamesuta.mc.guiwidget;

import java.util.List;

public interface GuiContainer {

	boolean remove(final GuiComponent widget);

	boolean add(final GuiComponent widget);

	List<GuiCommon> getContainer();

}
