package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.entry.content.ContentState;
import com.kamesuta.mc.signpic.entry.content.ContentStateType;
import com.kamesuta.mc.signpic.image.exception.InvaildImageException;

import net.minecraft.client.resources.I18n;

public class RemoteImage extends Image {
	public static final float ImageGarbageCollection = 15f;

	protected final ContentLocation location;
	protected ImageTextures texture;
	protected ContentState state;
	protected ImageIOLoader ioloading;
	protected File local;
	protected final Timer lastloaded = new Timer();
	protected boolean isTextureLoaded;
	protected boolean isAvailable;

	public RemoteImage(final ContentLocation location, final ContentId path, final ContentState state) {
		super(path);
		this.location = location;
		this.state = state;
		this.local = location.localLocation(path);
	}

	public void load() {
		new ImageLoader(this, this.location).onAsyncProcess();
	}

	protected void textureload() {
		ContentManager.instance.divisionqueue.offer(this);
	}

	protected int processing = 0;
	@Override
	public boolean onDivisionProcess() {
		final List<ImageTexture> texs = this.texture.getAll();
		if (this.processing < texs.size()) {
			final ImageTexture tex = texs.get(this.processing);
			tex.load();
			this.processing++;
			return false;
		} else {
			this.isAvailable = true;
			return true;
		}
	}

	@Override
	public void onAsyncProcess() {
		try {
			new ImageIOLoader(this, this.local).load();
		} catch (final InvaildImageException e) {
			this.state.setType(ContentStateType.ERROR);
			this.state.setMessage(I18n.format("signpic.advmsg.invaildimage"));
		} catch (final IOException e) {
			this.state.setType(ContentStateType.ERROR);
			this.state.setMessage(I18n.format("signpic.advmsg.ioerror", e));
		} catch (final Exception e) {
			this.state.setType(ContentStateType.ERROR);
			this.state.setMessage(I18n.format("signpic.advmsg.unknown", e));
		}
	}

	@Override
	public void onCollect() {
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
		result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
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
		if (this.path == null) {
			if (other.path != null)
				return false;
		} else if (!this.path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("RemoteImage[%s]", this.path);
	}
}
