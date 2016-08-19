package com.kamesuta.mc.signpic.image;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;

import org.lwjgl.util.Timer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.util.ResourceLocation;

public class ImageManager {
	public static final float LoadSpan = .5f;

	public static Deque<Image> lazyloadqueue = new ArrayDeque<Image>();

	protected final HashMap<String, Image> pool = new HashMap<String, Image>();

	protected final Timer timer = new Timer();
	protected final int currentprocess = 0;
	protected final ArrayList<Image> processes = new ArrayList<Image>();

	public ImageLocation location;

	public ImageManager(final ImageLocation location) {
		this.location = location;
	}

	public Image get(final String id) {
		Image image = this.pool.get(id);
		if (image == null) {
			image = new RemoteImage(id, this.location);
			this.pool.put(id, image);
			this.processes.add(image);
		}
		return image;
	}

	public Image get(final ResourceLocation location) {
		final String id = location.toString();
		Image image = this.pool.get(id);
		if (image == null) {
			image = new ResourceImage(location);
			this.pool.put(id, image);
			this.processes.add(image);
		}
		return image;
	}

	@SubscribeEvent
	public void renderTickProcess(final TickEvent.RenderTickEvent event) {
		Timer.tick();

		Image textureload;
		if ((textureload = ImageManager.lazyloadqueue.peek()) != null) {
			if (textureload.processTexture()) {
				ImageManager.lazyloadqueue.poll();
			}
		}

		if (!this.processes.isEmpty()) {
			final Image task = this.processes.get(this.currentprocess);
			if(this.timer.getTime() > LoadSpan){
				this.timer.set(0);
				this.currentprocess = (this.currentprocess<this.processes.size()-1)?this.currentprocess+1:0;
			}
			this.processes.get(this.currentprocess).process();
		}
	}
}
