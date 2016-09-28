package com.kamesuta.mc.bnnwidget;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;

public interface WCommon {
	void onAdded();

	void onInit(WEvent ev, Area pgp, Point mouse);

	void draw(WEvent ev, Area pgp, Point mouse, float frame);

	void update(WEvent ev, Area pgp, Point mouse);

	void keyTyped(WEvent ev, Area pgp, Point mouse, char c, int keycode);

	void mouseScrolled(WEvent ev, Area pgp, Point mouse, int scroll);

	void mouseMoved(WEvent ev, Area pgp, Point mouse, int button);

	void mouseClicked(WEvent ev, Area pgp, Point mouse, int button);

	void mouseDragged(WEvent ev, Area pgp, Point mouse, int button, long time);

	void mouseReleased(WEvent ev, Area pgp, Point mouse, int button);

	boolean onCloseRequest();

	boolean onClosing(WEvent ev, Area pgp, Point mouse);

	WCommon top(WEvent ev, Area pgp, Point point);
}
