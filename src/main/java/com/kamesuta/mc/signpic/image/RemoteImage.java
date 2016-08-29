package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.util.List;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.signpic.Reference;

public class RemoteImage extends Image {
	public static final float ImageGarbageCollection = 15f;

	protected final ImageLocation location;
	protected ImageTextures texture;
	protected String advmsg;
	protected ImageDownloader downloading;
	protected ImageIOLoader ioloading;
	protected File local;
	protected final Timer lastloaded = new Timer();

	public RemoteImage(final String id, final ImageLocation location) {
		super(id);
		this.location = location;
	}

	protected void init() {
		this.state = ImageState.INITALIZED;
	}

	protected void load() {
		this.state = ImageState.DOWNLOADING;
		ImageManager.threadpool.execute(new ImageLoader(this, this.location));
	}

	protected void textureload() {
		this.state = ImageState.TEXTURELOADING;
		ImageManager.lazyloadqueue.offer(this);
	}

	protected int processing = 0;
	@Override
	public boolean processTexture() {
		if (this.state == ImageState.TEXTURELOADING) {
			final List<ImageTexture> texs = this.texture.getAll();
			if (this.processing < texs.size()) {
				final ImageTexture tex = texs.get(this.processing);
				tex.load();
				this.processing++;
				return false;
			} else {
				this.state = ImageState.TEXTURELOADED;
				return true;
			}
		} else {
			Reference.logger.warn("Image#loadTexture only must be called TEXTURELOADING phase");
			return true;
		}
	}

	protected void complete() {
		this.state = ImageState.AVAILABLE;
	}

	@Override
	public void process() {
		switch(this.state) {
		case INIT:
			init();
			break;
		case INITALIZED:
			load();
			break;
		case IOLOADED:
			textureload();
			break;
		case TEXTURELOADED:
			complete();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean shouldCollect() {
		return this.lastloaded.getTime() > ImageGarbageCollection;
	}

	@Override
	public void delete() {
		if (this.texture!=null)
			this.texture.delete();
	}

	@Override
	public float getProgress() {
		switch(this.state) {
		case AVAILABLE:
		case DOWNLOADED:
		case IOLOADED:
		case TEXTURELOADED:
			return 1f;
		case DOWNLOADING:
			if (this.downloading != null)
				return this.downloading.getProgress();
		case IOLOADING:
			if (this.ioloading != null)
				return this.ioloading.getProgress();
		case TEXTURELOADING:
			if (this.texture != null && !this.texture.getAll().isEmpty())
				return  (float)this.processing / this.texture.getAll().size();
		default:
			return 0;
		}
	}

	@Override
	public IImageTexture getTexture() throws IllegalStateException {
		return getTextures().get();
	}

	public ImageTextures getTextures() {
		if (this.state == ImageState.AVAILABLE)
			return this.texture;
		else
			throw new IllegalStateException("Not Available");
	}

	@Override
	public void onImageUsed() {
		this.lastloaded.set(0);
	}

	@Override
	public String advMessage() {
		return this.advmsg;
	}

	@Override
	public String getLocal() {
		if (this.local != null)
			return "File:"+this.local.getName();
		else
			return "None";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RemoteImage))
			return false;
		final Image other = (Image) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("RemoteImage[%s]", this.id);
	}
}
