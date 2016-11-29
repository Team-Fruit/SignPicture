package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.LoadCanceledException;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentBlockedException;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.entry.content.RetryCountOverException;
import com.kamesuta.mc.signpic.entry.content.meta.ContentCache;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.download.ContentDownload;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.StateType;

public class RemoteImage extends Image {
	protected RemoteImageTexture texture;
	protected File local;
	private ContentDownload downloader;
	private ImageIOLoader ioloader;
	private boolean canceled;

	public RemoteImage(final Content content) {
		super(content);
	}

	private int processing = 0;

	@Override
	public void cancel() {
		if (this.downloader!=null)
			this.downloader.cancel();
		if (this.ioloader!=null)
			this.ioloader.cancel();
		this.canceled = true;
	}

	@Override
	public void onInit() {
		try {
			if (this.content.meta.isBlocked())
				throw new ContentBlockedException();

			final String cacheid = this.content.meta.getCacheID();
			ContentCache cachemeta = null;
			if (!StringUtils.isEmpty(cacheid))
				cachemeta = new ContentCache(ContentLocation.cachemetaLocation(cacheid));
			if (cachemeta==null||cachemeta.isDirty()||!cachemeta.isAvailable()||!ContentLocation.cacheLocation(cacheid).exists()) {
				if (Config.instance.contentMaxRetry>0&&this.content.meta.getTryCount()>Config.instance.contentMaxRetry)
					throw new RetryCountOverException();
				this.content.meta.setTryCount(this.content.meta.getTryCount()+1);
				this.content.state.setType(StateType.DOWNLOADING);
				this.content.state.setProgress(new Progress());
				this.downloader = new ContentDownload(this.content);
				this.downloader.setCallback(new ICommunicateCallback() {
					@Override
					public void onDone(final ICommunicateResponse res) {
						RemoteImage.this.content.state.setType(StateType.DOWNLOADED);
						if (res.isSuccess())
							ContentManager.instance.enqueueAsync(RemoteImage.this);
						else if (res.getError()!=null)
							RemoteImage.this.content.state.setErrorMessage(res.getError());
						RemoteImage.this.downloader = null;
					}
				});
				Communicator.instance.communicate(this.downloader);
			} else
				ContentManager.instance.enqueueAsync(this);
		} catch (final RetryCountOverException e) {
			this.content.state.setErrorMessage(e);
		} catch (final ContentBlockedException e) {
			this.content.state.setErrorMessage(e);
		}
	}

	@Override
	public void onAsyncProcess() {
		try {
			this.ioloader = new ImageIOLoader(this.content, new ImageIOLoader.InputFactory.ContentInputFactory(this.content));
			this.texture = this.ioloader.load();
			this.content.state.setType(StateType.LOADING);
			this.content.state.setProgress(new Progress());
			ContentManager.instance.enqueueDivision(this);
		} catch (final Throwable e) {
			this.content.state.setErrorMessage(e);
		}
	}

	@Override
	public boolean onDivisionProcess() {
		final List<DynamicImageTexture> texs = this.texture.getAll();
		if (this.canceled) {
			this.content.state.setErrorMessage(new LoadCanceledException());
			return true;
		} else if (this.processing<(this.content.state.getProgress().overall = texs.size())) {
			final DynamicImageTexture tex = texs.get(this.processing);
			tex.load();
			this.processing++;
			this.content.state.getProgress().done = this.processing;
			return false;
		} else {
			this.content.state.setType(StateType.AVAILABLE);
			this.content.state.getProgress().done = this.content.state.getProgress().overall;
			this.content.meta.setTryCount(0);
			final ContentCache cachemeta = new ContentCache(ContentLocation.cachemetaLocation(this.content.meta.getCacheID()));
			cachemeta.setAvailable(true);
			return true;
		}
	}

	@Override
	public void onCollect() {
		if (this.downloader!=null)
			this.downloader.cancel();
		if (this.texture!=null)
			this.texture.delete();
	}

	@Override
	public ImageTexture getTexture() throws IllegalStateException {
		return getTextures().get();
	}

	public RemoteImageTexture getTextures() {
		if (this.content.state.getType()==StateType.AVAILABLE)
			return this.texture;
		else
			throw new IllegalStateException("Not Available");
	}

	@Override
	public String getLocal() {
		if (this.local!=null)
			return "File:"+this.local.getName();
		else
			return "None";
	}

	@Override
	public String toString() {
		return String.format("RemoteImage[%s]", this.content.id);
	}
}
