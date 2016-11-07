package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicate;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.download.ContentDownload;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.StateType;

public class RemoteImage extends Image {
	protected ImageTextures texture;
	protected File local;
	private ICommunicate downloader;

	public RemoteImage(final Content content) {
		super(content);
	}

	private int processing = 0;

	@Override
	public void onInit() {
		final File local = this.content.location.cacheLocation();
		if (!local.exists()) {
			this.content.state.setType(StateType.DOWNLOADING);
			this.content.state.setProgress(new Progress());
			try {
				this.downloader = new ContentDownload(this.content.location, this.content.state);
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
			} catch (final URISyntaxException e) {
				this.content.state.setErrorMessage(e);
			}
		} else
			ContentManager.instance.enqueueAsync(this);
	}

	@Override
	public void onAsyncProcess() {
		try {
			this.texture = new ImageIOLoader(this.content, new ImageIOLoader.InputFactory.ContentInputFactory(this.content)).load();
			this.content.state.setType(StateType.LOADING);
			this.content.state.setProgress(new Progress());
			ContentManager.instance.enqueueDivision(this);
		} catch (final Throwable e) {
			this.content.state.setErrorMessage(e);
		}
	}

	@Override
	public boolean onDivisionProcess() {
		final List<ImageTexture> texs = this.texture.getAll();
		if (this.processing<(this.content.state.getProgress().overall = texs.size())) {
			final ImageTexture tex = texs.get(this.processing);
			tex.load();
			this.processing++;
			this.content.state.getProgress().done = this.processing;
			return false;
		} else {
			this.content.state.setType(StateType.AVAILABLE);
			this.content.state.getProgress().done = this.content.state.getProgress().overall;
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
	public IImageTexture getTexture() throws IllegalStateException {
		return getTextures().get();
	}

	public ImageTextures getTextures() {
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
