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
	protected final File temp;
	protected final File local;
	protected final State state;
	protected boolean canceled;

	public ContentDownload(final String name, final URI remote, final File temp, final File local, final State state) {
		this.name = name;
		this.remote = remote;
		this.temp = temp;
		this.local = local;
		this.state = state;
	}

	public ContentDownload(final ContentLocation location, final State state) throws URISyntaxException {
		this(location.id.id(), location.remoteLocation(), location.tempLocation(), location.cacheLocation(), state);
	}

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public void communicate() {
		InputStream input = null;
		OutputStream output = null;
		try {
			final HttpUriRequest req = new HttpGet(this.remote);
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			final long max = Config.instance.contentMaxByte;
			final long size = entity.getContentLength();
			if (max>0&&(size<0||size>max))
				throw new ContentCapacityOverException();

			final Progress progress = this.state.getProgress();
			progress.setOverall(entity.getContentLength());
			input = entity.getContent();
			output = new CountingOutputStream(new FileOutputStream(this.temp)) {
				@Override
				protected void afterWrite(final int n) throws IOException {
					if (ContentDownload.this.canceled) {
						req.abort();
						throw new CommunicateCanceledException();
					}
					progress.setDone(getByteCount());
				}
			};
			FileUtils.deleteQuietly(this.temp);
			IOUtils.copyLarge(input, output);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(this.local);
			FileUtils.moveFile(this.temp, this.local);
			onDone(new CommunicateResponse(true, null));
		} catch (final Exception e) {
			onDone(new CommunicateResponse(false, e));
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(this.temp);
		}
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}
}
