package com.kamesuta.mc.signpic.render;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class RenderTick {

	public RenderTick() {
	}


	@SubscribeEvent
	public void onRenderTick(final TickEvent.RenderTickEvent event) {
		Timer.tick();

		Image textureload;
		if ((textureload = ImageManager.lazyloadqueue.peek()) != null) {
			if (textureload.processTexture()) {
				ImageManager.lazyloadqueue.poll();
			}
		}
	}
}
