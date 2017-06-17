package com.kamesuta.mc.signpic.image;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

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
import com.kamesuta.mc.signpic.image.ImageIOLoader.InputFactory.FileInputFactory;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.StateType;

public class RemoteImage extends Image {
	protected @Nullable RemoteImageTexture texture;
	private @Nullable ContentDownload downloader;
	private @Nullable ImageIOLoader ioloader;
	private boolean canceled;

	public RemoteImage(final @Nonnull Content content) {
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
			if (cacheid!=null&&!StringUtils.isEmpty(cacheid))
				cachemeta = new ContentCache(ContentLocation.cachemetaLocation(cacheid));
			if (cachemeta==null||cachemeta.isDirty()||!cachemeta.isAvailable()||!(cacheid!=null&&ContentLocation.cacheLocation(cacheid).exists())) {
				if (Config.getConfig().contentMaxRetry.get()>0&&this.content.meta.getTryCount()>Config.getConfig().contentMaxRetry.get())
					throw new RetryCountOverException();
				this.content.meta.setTryCount(this.content.meta.getTryCount()+1);
				this.content.state.setType(StateType.DOWNLOADING);
				this.content.state.setProgress(new Progress());
				this.downloader = new ContentDownload(this.content);
				this.downloader.setCallback(new ICommunicateCallback() {
					@Override
					public void onDone(final @Nonnull ICommunicateResponse res) {
						RemoteImage.this.content.state.setType(StateType.DOWNLOADED);
						if (res.isSuccess())
							onDoneInit();
						else {
							final Throwable t = res.getError();
							if (t!=null)
								RemoteImage.this.content.state.setErrorMessage(t);
						}
						RemoteImage.this.downloader = null;
					}
				});
				if (this.downloader!=null)
					Communicator.instance.communicate(this.downloader);
			} else
				onDoneInit();
		} catch (final RetryCountOverException e) {
			this.content.state.setErrorMessage(e);
		} catch (final ContentBlockedException e) {
			this.content.state.setErrorMessage(e);
		}
	}

	private void onDoneInit() {
		this.content.imagemeta = this.content.meta.getImageMeta();
		ContentManager.instance.enqueueAsync(this);
	}

	@Override
	public void onAsyncProcess() {
		try {
			final FileInputFactory factory = ImageIOLoader.InputFactory.FileInputFactory.createFromContent(this.content);
			if (factory!=null) {
				this.ioloader = new ImageIOLoader(this.content, factory);
				this.texture = this.ioloader.load();
				this.content.state.setType(StateType.LOADING);
				this.content.state.setProgress(new Progress());
				onDoneAsyncProcess();
			}
		} catch (final Throwable e) {
			this.content.state.setErrorMessage(e);
		}
	}

	private void onDoneAsyncProcess() {
		ContentManager.instance.enqueueDivision(this);
	}

	@Override
	public boolean onDivisionProcess() throws InvaildImageException {
		if (this.texture!=null) {
			final List<Pair<Float, DynamicImageTexture>> texs = this.texture.getAll();
			if (this.canceled) {
				this.content.state.setErrorMessage(new LoadCanceledException());
				return true;
			} else if (this.processing<(this.content.state.getProgress().overall = texs.size())) {
				final DynamicImageTexture tex = texs.get(this.processing).getRight();
				tex.load();
				this.processing++;
				this.content.state.getProgress().done = this.processing;
				return false;
			} else {
				onDoneDivisionProcess();
				return true;
			}
		} else
			throw new InvaildImageException();
	}

	private void onDoneDivisionProcess() {
		this.content.state.setType(StateType.AVAILABLE);
		this.content.state.getProgress().done = this.content.state.getProgress().overall;
		this.content.meta.setTryCount(0);
		final String id = this.content.meta.getCacheID();
		if (id!=null) {
			final ContentCache cachemeta = new ContentCache(ContentLocation.cachemetaLocation(id));
			cachemeta.setAvailable(true);
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
	public @Nonnull ImageTexture getTexture() throws IllegalStateException {
		return getTextures().get();
	}

	public @Nonnull RemoteImageTexture getTextures() {
		if (this.content.state.getType()==StateType.AVAILABLE&&this.texture!=null)
			return this.texture;
		else
			throw new IllegalStateException("Not Available");
	}

	@Override
	public @Nonnull String getLocal() {
		return String.format("RemoteImage:{Meta:%s,Cache:%s}", this.content.meta.getMetaID(), this.content.meta.getCacheID());
	}

	@Override
	public @Nonnull String toString() {
		return String.format("RemoteImage[%s]", this.content.id);
	}
}
