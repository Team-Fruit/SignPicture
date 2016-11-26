package com.kamesuta.mc.signpic.http.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.content.ContentCapacityOverException;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateCanceledException;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;

public class ContentDownload extends Communicate implements Progressable {
	protected final String name;
	protected final URI remote;
	protected final File local;
	protected final State state;
	protected boolean canceled;

	public ContentDownload(final String name, final URI remote, final File local, final State state) {
		this.name = name;
		this.remote = remote;
		this.local = local;
		this.state = state;
	}

	public ContentDownload(final ContentLocation location, final State state) throws URISyntaxException {
		this(location.id.id(), location.remoteLocation(), location.cacheLocation(), state);
	}

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public void communicate() {
		File tmp = null;
		InputStream input = null;
		OutputStream output = null;
		try {
			final HttpUriRequest req = new HttpGet(this.remote);
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			tmp = Client.location.createCache("content");

			final long max = Config.instance.contentMaxByte;
			final long size = entity.getContentLength();
			if (max>0&&(size<0||size>max)) {
				req.abort();
				throw new ContentCapacityOverException("size: "+size);
			}

			final Progress progress = this.state.getProgress();
			progress.setOverall(entity.getContentLength());
			input = entity.getContent();
			output = new CountingOutputStream(new FileOutputStream(tmp)) {
				@Override
				protected void afterWrite(final int n) throws IOException {
					if (ContentDownload.this.canceled) {
						req.abort();
						throw new CommunicateCanceledException();
					}
					final long bcount = getByteCount();
					if (max>0&&bcount>max) {
						req.abort();
						throw new ContentCapacityOverException();
					}
					progress.setDone(bcount);
				}
			};
			FileUtils.deleteQuietly(tmp);
			IOUtils.copyLarge(input, output);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(this.local);
			FileUtils.moveFile(tmp, this.local);
			onDone(new CommunicateResponse(true, null));
		} catch (final Exception e) {
			onDone(new CommunicateResponse(false, e));
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(tmp);
		}
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}
}
