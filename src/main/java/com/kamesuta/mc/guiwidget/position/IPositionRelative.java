package com.kamesuta.mc.guiwidget.position;

public interface IPositionRelative extends IPosition {

	IPositionAbsolute getAbsolute(IPositionAbsolute parent);

}