package com.kamesuta.mc.signpic.entry.content;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.information.Info;
import com.kamesuta.mc.signpic.util.Downloader;

public class ContentDownloader implements IAsyncProcessable {
	private static final Gson gson = new Gson();

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
			final File metaFile = this.content.location.metaLocation();
			final File cacheFile = this.content.location.createCacheLocation();
			if (!metaFile.exists()) {
				final ContentMeta meta;
				if (metaFile.exists())
					meta = gson.fromJson(new JsonReader(new InputStreamReader(new FileInputStream(metaFile), CharEncoding.UTF_8)), Info.class);
				else
					meta = new ContentMeta();

				if (!StringUtils.isEmpty(meta.cache))
					this.content.location
					final HttpUriRequest req = new HttpGet(this.content.location.remoteLocation());
				final HttpResponse response = Downloader.downloader.client.execute(req);
				final HttpEntity entity = response.getEntity();

				final ContentType type = ContentType.get(entity);
				meta.mime = type.getMimeType();

				this.content.state.progress.overall = entity.getContentLength();
				input = entity.getContent();
				countoutput = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(cacheFile))) {
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
