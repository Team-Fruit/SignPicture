package com.kamesuta.mc.signpic.entry.content;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.util.Downloader;

public class ContentDownloader implements IAsyncProcessable {
	protected final Content content;

	public ContentDownloader(final Content content) {
		this.content = content;
	}

	@Override
	public void onAsyncProcess() throws URISyntaxException, IllegalStateException, IOException {
		InputStream input = null;
		CountingOutputStream countoutput = null;
		this.content.state.setType(ContentStateType.DOWNLOADING);
		try {
			final File local = this.content.location.createCacheLocation();
			if (!local.exists()) {
				final HttpUriRequest req = new HttpGet(this.content.location.remoteLocation());
				final HttpResponse response = Downloader.downloader.client.execute(req);
				final HttpEntity entity = response.getEntity();

				final long max = Config.instance.contentMaxByte;
				final long size = entity.getContentLength();
				if (max > 0 && (size < 0 || size > max))
					throw new ContentCapacityOverException();

				this.content.state.progress.overall = entity.getContentLength();
				input = entity.getContent();
				countoutput = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(local))) {
					@Override
					protected void afterWrite(final int n) throws IOException {
						ContentDownloader.this.content.state.progress.done = getByteCount();
					}
				};
				IOUtils.copy(input, countoutput);
			}
			this.content.state.setType(ContentStateType.DOWNLOADED);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(countoutput);
		}
	}
}
