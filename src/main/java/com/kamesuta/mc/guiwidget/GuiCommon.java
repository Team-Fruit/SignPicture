package com.kamesuta.mc.guiwidget;

import com.kamesuta.mc.guiwidget.position.Point;

public interface GuiCommon {

	void init(GuiTools tools, GuiPosition pgp);

	void draw(GuiTools tools, GuiPosition pgp, Point mouse, float frame);

	void update(GuiTools tools, GuiPosition pgp, Point mouse);

	void keyTyped(GuiTools tools, GuiPosition pgp, Point mouse, char c, int keycode);

	void mouseScrolled(GuiTools tools, GuiPosition pgp, Point mouse, int scroll);

	void mouseDragged(GuiTools tools, GuiPosition pgp, Point mouse, int button, long time);

	void mouseMovedOrUp(GuiTools tools, GuiPosition pgp, Point mouse, int button);

	void mouseClicked(GuiTools tools, GuiPosition pgp, Point mouse, int button);

}
