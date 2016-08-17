package com.kamesuta.mc.signpic.image;

import java.io.File;

import com.kamesuta.mc.signpic.Reference;

public class Image {

	public final String id;
	public ImageTexture texture;
	public ImageState state = ImageState.INIT;
	public ImageDownloader downloading;
	public Thread downloadingprocess;

	public Image(final String url) {
		this.id = url;
	}

	public void preload(final ImageLocation location) {
		try {
			Reference.logger.info("PreLoading Start: " + this);
			final File local = location.localLocation(this);

			Reference.logger.info("PreLoading/Downloading Start: " + this);
			this.state = ImageState.LOADING;
			if (!local.exists()) {
				Reference.logger.info("File not exists: " + this);
				if (this.downloading == null)
					this.downloading = new ImageDownloader(location, this);
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

	public void load(final ImageLocation location) {
		try {
			Reference.logger.info("Loading Start: " + this);
			final File local = location.localLocation(this);

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

	public float getProgress() {
		switch(this.state) {
		case AVAILABLE:
			return 1f;
		case DOWNLOADING:
			return this.downloading.getProgress();
		default:
			return 0;
		}
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
		if (!(obj instanceof Image))
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
		return String.format("Image[%s]", this.id);
	}
}
