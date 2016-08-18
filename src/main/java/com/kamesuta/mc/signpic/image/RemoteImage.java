package com.kamesuta.mc.signpic.image;

import java.io.File;

import com.kamesuta.mc.signpic.Reference;

import net.minecraft.client.resources.I18n;

public class RemoteImage implements Image {
	protected final String id;
	protected ImageLocation location;
	protected ImageTexture texture;
	protected ImageState state = ImageState.INIT;
	protected ImageDownloader downloading;
	protected Thread downloadingprocess;

	public RemoteImage(final ImageLocation location, final String id) {
		this.location = location;
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public ImageTexture getTexture() {
		return this.texture;
	}

	@Override
	public ImageState getState() {
		return this.state;
	}

	@Override
	public void preload() {
		try {
			Reference.logger.info("PreLoading Start: " + this);
			final File local = this.location.localLocation(this);

			Reference.logger.info("PreLoading/Downloading Start: " + this);
			this.state = ImageState.LOADING;
			if (!local.exists()) {
				Reference.logger.info("File not exists: " + this);
				if (this.downloading == null)
					this.downloading = new ImageDownloader(this.location, this);
				if (this.downloadingprocess == null) {
					this.downloadingprocess = new Thread(this.downloading);
					this.downloadingprocess.start();
				}
			}
		} catch (final Exception e) {
			this.state = ImageState.ERROR;
			Reference.logger.info("ERROR: " + this + ": " + e);
		}
	}

	@Override
	public void load() {
		try {
			Reference.logger.info("Loading Start: " + this);
			final File local = this.location.localLocation(this);

			if (local.exists()) {
				this.texture = new ImageTexture(local);
				Reference.logger.info("Loaded: " + this);
				this.state = ImageState.AVAILABLE;
			}
		} catch (final Exception e) {
			this.state = ImageState.ERROR;
			Reference.logger.info("ERROR: " + this + ": " + e);
		}
	}

	@Override
	public float getProgress() {
		switch(this.state) {
		case AVAILABLE:
			return 1f;
		case DOWNLOADING:
		case FAILED:
			if (this.downloading != null)
				return this.downloading.getProgress();
		default:
			return 0;
		}
	}

	@Override
	public String getStatusMessage() {
		return I18n.format(this.state.msg, (int)getProgress()*100);
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
		final RemoteImage other = (RemoteImage) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Image[%s]", this.id);
	}
}
