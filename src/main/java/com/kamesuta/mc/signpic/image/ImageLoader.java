package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.minecraft.client.resources.I18n;

public class ImageLoader implements Runnable {
	protected RemoteImage image;
	protected ImageLocation remote;

	public ImageLoader(final RemoteImage image, final ImageLocation remote) {
		this.image = image;
		this.remote = remote;
	}

	@Override
	public void run() {
		final File local = this.image.location.localLocation(this.image);
		try {
			this.image.state = ImageState.DOWNLOADING;
			if (local.exists()) {
				this.image.state = ImageState.DOWNLOADED;
			} else {
				if (this.image.downloading == null) {
					this.image.downloading = new ImageDownloader(this.image, this.image.location);
					this.image.downloading.load();
				}
			}

			if (this.image.state == ImageState.DOWNLOADED) {
				this.image.state = ImageState.IOLOADING;
				if (local.exists()) {
					this.image.local = local;
					if (this.image.ioloading == null) {
						this.image.ioloading = new ImageIOLoader(this.image, local);
						this.image.ioloading.load();
					}
				} else {
					throw new FileNotFoundException("The file was changed");
				}
			}
		} catch (final IOException e) {
			this.image.state = ImageState.ERROR;
			this.image.advmsg = I18n.format("signpic.advmsg.ioerror", e);
		} catch (final Exception e) {
			this.image.state = ImageState.ERROR;
			this.image.advmsg = I18n.format("signpic.advmsg.unknown", e);
		}
	}
}
