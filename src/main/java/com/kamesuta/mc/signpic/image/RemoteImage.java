package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.kamesuta.mc.signpic.entry.content.ContentDownloader;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.entry.content.ContentState;
import com.kamesuta.mc.signpic.entry.content.ContentStateType;
import com.kamesuta.mc.signpic.image.exception.InvaildImageException;

import net.minecraft.client.resources.I18n;

public class RemoteImage extends Image {
	protected ImageTextures texture;
	protected File local;
	protected boolean isTextureLoaded;

	public RemoteImage(final ContentLocation location, final ContentId id, final ContentState state) {
		super(location, id, state);
		this.local = location.localLocation(id);
	}

	private int processing = 0;
	@Override
	public boolean onDivisionProcess() {
		if (this.isTextureLoaded) {
			final List<ImageTexture> texs = this.texture.getAll();
			if (this.processing < (this.state.progress.overall = texs.size())) {
				final ImageTexture tex = texs.get(this.processing);
				tex.load();
				this.processing++;
				this.state.setType(ContentStateType.LOADING);
				this.state.progress.done = this.processing;
				return false;
			} else {
				this.state.setType(ContentStateType.AVAILABLE);
				this.state.progress.done = this.state.progress.overall;
				return true;
			}
		}
		throw new IllegalStateException("No Texture Loaded");
	}

	@Override
	public void onAsyncProcess() {
		try {
			new ContentDownloader(this.location, this.id, this.state).onAsyncProcess();
			new ImageIOLoader(this, this.local).load();
			ContentManager.instance.divisionqueue.offer(this);
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
	public IImageTexture getTexture() throws IllegalStateException {
		return getTextures().get();
	}

	public ImageTextures getTextures() {
		if (this.state.getType() == ContentStateType.AVAILABLE)
			return this.texture;
		else
			throw new IllegalStateException("Not Available");
	}

	@Override
	public String getLocal() {
		if (this.local != null)
			return "File:" + this.local.getName();
		else
			return "None";
	}

	@Override
	public String toString() {
		return String.format("RemoteImage[%s]", this.id);
	}
}
