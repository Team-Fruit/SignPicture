package com.kamesuta.mc.signpic.image;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.Reference;

public class ImageDownloader implements Runnable {

	public static Downloader downloader = new Downloader().setMax(3);

	protected final ImageLocation location;
	protected final Image image;

	protected long maxsize;
	protected CountingOutputStream countoutput;

	public ImageDownloader(final ImageLocation location, final Image image) {
		this.location = location;
		this.image = image;
	}

	@Override
	public void run() {
		this.image.state = ImageState.DOWNLOADING;
		Reference.logger.info("Downloading Start: " + this.image);
		InputStream input = null;
		try {
			final HttpUriRequest req = new HttpGet(this.location.remoteLocation(this.image));
			final HttpResponse response = downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			this.maxsize = entity.getContentLength();
			input = entity.getContent();
			this.countoutput = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(this.location.localLocationPrepare(this.image))));
			IOUtils.copy(input, this.countoutput);
			// Reference.logger.info(EntityUtils.toString(entity));
			this.image.state = ImageState.LOADING;
		} catch (final Exception e) {
			this.image.state = ImageState.FAILED;
			Reference.logger.warn("Failed to download :", e);
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
