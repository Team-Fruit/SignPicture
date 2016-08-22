package com.kamesuta.mc.guiwidget;

import com.kamesuta.mc.guiwidget.position.Point;

public interface GuiCommon {

	void init(GuiEvent ev, GuiPosition pgp);

	void draw(GuiEvent ev, GuiPosition pgp, Point mouse, float frame);

	void update(GuiEvent ev, GuiPosition pgp, Point mouse);

	void keyTyped(GuiEvent ev, GuiPosition pgp, Point mouse, char c, int keycode);

	void mouseScrolled(GuiEvent ev, GuiPosition pgp, Point mouse, int scroll);

	void mouseMoved(GuiEvent ev, GuiPosition pgp, Point mouse, int button);

	void mouseClicked(GuiEvent ev, GuiPosition pgp, Point mouse, int button);

	void mouseDragged(GuiEvent ev, GuiPosition pgp, Point mouse, int button, long time);

	void mouseReleased(GuiEvent ev, GuiPosition pgp, Point mouse, int button);

}
