package com.kamesuta.mc.guiwidget;

import com.kamesuta.mc.guiwidget.position.Point;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;

public interface WCommon {
	Minecraft mc = FMLClientHandler.instance().getClient();
	WGraphics g = WGraphics.INSTANCE;

	void init(WEvent ev, WPosition pgp);

	void draw(WEvent ev, WPosition pgp, Point mouse, float frame);

	void update(WEvent ev, WPosition pgp, Point mouse);

	void keyTyped(WEvent ev, WPosition pgp, Point mouse, char c, int keycode);

	void mouseScrolled(WEvent ev, WPosition pgp, Point mouse, int scroll);

	void mouseMoved(WEvent ev, WPosition pgp, Point mouse, int button);

	void mouseClicked(WEvent ev, WPosition pgp, Point mouse, int button);

	void mouseDragged(WEvent ev, WPosition pgp, Point mouse, int button, long time);

	void mouseReleased(WEvent ev, WPosition pgp, Point mouse, int button);

}
