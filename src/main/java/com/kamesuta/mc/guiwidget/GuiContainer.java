package com.kamesuta.mc.guiwidget;

import java.util.List;

public interface GuiContainer {

	boolean remove(final GuiCommon widget);

	boolean add(final GuiCommon widget);

	List<GuiCommon> getContainer();

}
