package com.kamesuta.mc.signpic.http.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.content.ContentCapacityOverException;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.http.ICommunicate;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.util.Downloader;

public class ContentDownload implements ICommunicate<ContentDownload.ContentDLResult>, Progressable {
	protected final String name;
	protected final URI remote;
	protected final File local;
	protected final Progress progress;

	public ContentDownload(final String name, final URI remote, final File local, final Progress progress) {
		this.name = name;
		this.remote = remote;
		this.local = local;
		this.progress = progress;
	}

	public ContentDownload(final ContentLocation location, final Progress progress) throws URISyntaxException {
		this(location.id.id(), location.remoteLocation(), location.createCacheLocation(), progress);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Progress getProgress() {
		return this.progress;
	}

	@Override
	public ICommunicateResponse<ContentDLResult> communicate() {
		InputStream input = null;
		CountingOutputStream countoutput = null;
		try {
			final HttpUriRequest req = new HttpGet(this.remote);
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			final long max = Config.instance.contentMaxByte;
			final long size = entity.getContentLength();
			if (max > 0 && (size < 0 || size > max))
				throw new ContentCapacityOverException();

			this.progress.overall = entity.getContentLength();
			input = entity.getContent();
			countoutput = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(this.local))) {
				@Override
				protected void afterWrite(final int n) throws IOException {
					ContentDownload.this.progress.done = getByteCount();
				}
			};
			IOUtils.copy(input, countoutput);
			return new CommunicateResponse<ContentDLResult>(new ContentDLResult());
		} catch (final Exception e) {
			return new CommunicateResponse<ContentDLResult>(e);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(countoutput);
		}
	}

	public static class ContentDLResult {
	}
}
