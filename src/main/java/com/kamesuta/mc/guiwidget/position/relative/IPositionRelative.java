package com.kamesuta.mc.guiwidget.position.relative;

import com.kamesuta.mc.guiwidget.position.IPosition;
import com.kamesuta.mc.guiwidget.position.IPositionAbsolute;

public interface IPositionRelative extends IPosition {

	IPositionAbsolute getAbsolute(IPositionAbsolute parent);

}