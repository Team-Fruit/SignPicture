package com.kamesuta.mc.signpic.image;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lwjgl.util.Timer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.util.ResourceLocation;

public class ImageManager {
	public static Deque<Image> lazyloadqueue = new ArrayDeque<Image>();
	public static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
	protected final HashMap<String, Image> pool = new HashMap<String, Image>();

	public ImageLocation location;

	public ImageManager(final ImageLocation location) {
		this.location = location;
	}

	public Image get(final String id) {
		if (id.startsWith("!")) {
			return get(new ResourceLocation(id.substring(1)));
		} else {
			Image image = this.pool.get(id);
			if (image == null) {
				image = new RemoteImage(id, this.location);
				this.pool.put(id, image);
			}
			image.onImageUsed();
			return image;
		}
	}

	public Image get(final ResourceLocation location) {
		final String id = "!" + location.toString();
		Image image = this.pool.get(id);
		if (image == null) {
			image = new ResourceImage(location);
			this.pool.put(id, image);
		}
		image.onImageUsed();
		return image;
	}

	public void delete(final Image image) {
		this.pool.remove(image.id);
		image.delete();
	}

	@SubscribeEvent
	public void renderTickProcess(final TickEvent.RenderTickEvent event) {
		Image textureload;
		if ((textureload = ImageManager.lazyloadqueue.peek()) != null) {
			if (textureload.processTexture()) {
				ImageManager.lazyloadqueue.poll();
			}
		}

		for (final Entry<String, Image> entry : this.pool.entrySet()) {
			final Image image = entry.getValue();
			image.process();
			if (image.shouldCollect())
				delete(image);
		}

		Timer.tick();
	}
}
