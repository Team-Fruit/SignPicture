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
import com.kamesuta.mc.signpic.entry.EntryLocation;
import com.kamesuta.mc.signpic.entry.EntryPath;
import com.kamesuta.mc.signpic.handler.CoreEvent;

import cpw.mods.fml.common.gameevent.TickEvent;

public class ImageManager {
	public static Deque<Image> lazyloadqueue = new ArrayDeque<Image>();
	public static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
	protected final HashMap<EntryPath, Image> pool = new HashMap<EntryPath, Image>();

	public EntryLocation location;

	public ImageManager(final EntryLocation location) {
		this.location = location;
	}

	public Image get(final EntryPath path) {
		Image image = this.pool.get(path);
		if (image == null) {
			if (path.isResource())
				image = new ResourceImage(path);
			else
				image = new RemoteImage(path, this.location);
			this.pool.put(path, image);
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

			for (final Iterator<Entry<EntryPath, Image>> itr = this.pool.entrySet().iterator(); itr.hasNext();) {
				final Entry<EntryPath, Image> entry = itr.next();
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
