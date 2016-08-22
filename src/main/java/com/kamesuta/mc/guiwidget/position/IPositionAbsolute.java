package com.kamesuta.mc.guiwidget.position;

public interface IPositionAbsolute extends IPosition {

	int x1();

	int y1();

	int x2();

	int y2();

	boolean pointInside(Point p);

}
