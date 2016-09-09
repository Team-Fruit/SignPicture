package com.kamesuta.mc.signpic.image;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.handler.CoreEvent;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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

	@CoreEvent
	public void onRenderTick(final TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Client.startSection("signpic-load");
			Image textureload;
			if ((textureload = ImageManager.lazyloadqueue.peek()) != null) {
				if (textureload.processTexture()) {
					ImageManager.lazyloadqueue.poll();
				}
			}

			for (final Iterator<Entry<String, Image>> itr = this.pool.entrySet().iterator(); itr.hasNext();) {
				final Entry<String, Image> entry = itr.next();
				final Image image = entry.getValue();
				image.process();
				if (image.shouldCollect()) {
					image.delete();
					itr.remove();
				}
			}

			Timer.tick();
			Client.endSection();
		}
	}
}
