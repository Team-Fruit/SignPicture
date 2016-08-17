package com.kamesuta.mc.signpic.image;

import java.util.HashMap;

public class ImageManager {
	protected final HashMap<String, Image> pool = new HashMap<String, Image>();

	public ImageLocation location;

	public ImageManager(final ImageLocation location) {
		this.location = location;
	}

	public Image get(final String id) {
		Image image = this.pool.get(id);
		if (image == null) {
			image = new Image(id);
			this.pool.put(id, image);
		}
		if (image.state == ImageState.INIT) {
			image.preload(this.location);
		} else if (image.state == ImageState.LOADING) {
			image.load(this.location);
		}
		return image;
	}
}
