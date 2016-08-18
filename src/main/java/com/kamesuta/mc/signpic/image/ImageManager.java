package com.kamesuta.mc.signpic.image;

import java.util.HashMap;

import net.minecraft.util.ResourceLocation;

public class ImageManager {
	protected final HashMap<String, Image> pool = new HashMap<String, Image>();

	public ImageLocation location;

	public ImageManager(final ImageLocation location) {
		this.location = location;
	}

	public Image get(final String id) {
		Image image = this.pool.get(id);
		if (image == null) {
			image = new RemoteImage(this.location, id);
			this.pool.put(id, image);
		}
		if (image.getState() == ImageState.INIT) {
			image.preload();
		} else if (image.getState() == ImageState.LOADING) {
			image.load();
		}
		return image;
	}

	public Image get(final ResourceLocation location) {
		final String id = location.toString();
		Image image = this.pool.get(id);
		if (image == null) {
			image = new ResourceImage(location);
			this.pool.put(id, image);
		}
		if (image.getState() == ImageState.INIT) {
			image.preload();
		} else if (image.getState() == ImageState.LOADING) {
			image.load();
		}
		return image;
	}
}
