package com.kamesuta.mc.guiwidget;

import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.Area;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;

public interface WCommon {
	Minecraft mc = FMLClientHandler.instance().getClient();
	WGraphics g = WGraphics.INSTANCE;

	void init(WEvent ev, Area pgp);

	void draw(WEvent ev, Area pgp, Point mouse, float frame);

	void update(WEvent ev, Area pgp, Point mouse);

	void keyTyped(WEvent ev, Area pgp, Point mouse, char c, int keycode);

	void mouseScrolled(WEvent ev, Area pgp, Point mouse, int scroll);

	void mouseMoved(WEvent ev, Area pgp, Point mouse, int button);

	void mouseClicked(WEvent ev, Area pgp, Point mouse, int button);

	void mouseDragged(WEvent ev, Area pgp, Point mouse, int button, long time);

	void mouseReleased(WEvent ev, Area pgp, Point mouse, int button);

}
