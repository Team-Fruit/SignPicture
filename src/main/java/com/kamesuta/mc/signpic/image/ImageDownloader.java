package com.kamesuta.mc.signpic.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.util.Downloader;

import net.minecraft.client.resources.I18n;

public class ImageDownloader {

	public static Downloader downloader = new Downloader();

	protected final ImageLocation location;
	protected final RemoteImage image;

	protected long maxsize;
	protected CountingOutputStream countoutput;

	public ImageDownloader(final RemoteImage image, final ImageLocation location) {
		this.location = location;
		this.image = image;
	}

	public void load() {
		InputStream input = null;
		try {
			final File local = this.location.localLocation(this.image);

			final HttpUriRequest req = new HttpGet(this.location.remoteLocation(this.image));
			final HttpResponse response = downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			this.maxsize = entity.getContentLength();
			input = entity.getContent();
			this.countoutput = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(local)));
			IOUtils.copy(input, this.countoutput);

			this.image.state = ImageState.DOWNLOADED;
		} catch (final URISyntaxException e) {
			this.image.state = ImageState.ERROR;
			this.image.advmsg = I18n.format("signpic.advmsg.invaildurl");
		} catch (final Exception e) {
			this.image.state = ImageState.FAILED;
			this.image.advmsg = I18n.format("signpic.advmsg.dlerror", e);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(this.countoutput);
		}
	}

	public float getProgress() {
		if (this.maxsize > 0)
			return Math.max(0, Math.min(1, (getRate() / (float) this.maxsize)));
		return 0;
	}

	protected long getRate() {
		if (this.countoutput != null)
			return this.countoutput.getByteCount();
		return 0;
	}
}
