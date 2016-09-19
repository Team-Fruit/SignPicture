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

import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.util.Downloader;

import net.minecraft.client.resources.I18n;

public class ContentDownloader implements IAsyncProcessable {
	protected final ContentLocation location;
	protected final ContentId path;
	protected final ContentState state;

	public ContentDownloader(final ContentLocation location, final ContentId path, final ContentState state) {
		this.location = location;
		this.path = path;
		this.state = state;
	}

	@Override
	public void onAsyncProcess() {
		InputStream input = null;
		CountingOutputStream countoutput = null;
		this.state.setType(ContentStateType.LOADING);
		try {
			final File local = this.location.localLocation(this.path);
			if (!local.exists()) {
				final HttpUriRequest req = new HttpGet(this.location.remoteLocation(this.path));
				final HttpResponse response = Downloader.downloader.client.execute(req);
				final HttpEntity entity = response.getEntity();

				this.state.progress.overall = entity.getContentLength();
				input = entity.getContent();
				countoutput = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(local))) {
					@Override
					protected void afterWrite(final int n) throws IOException {
						ContentDownloader.this.state.progress.done = getByteCount();
					}
				};
				IOUtils.copy(input, countoutput);
			}
			this.state.setType(ContentStateType.LOADED);
		} catch (final URISyntaxException e) {
			this.state.setType(ContentStateType.ERROR);
			this.state.setMessage(I18n.format("signpic.advmsg.invaildurl"));
		} catch (final Exception e) {
			this.state.setType(ContentStateType.ERROR);
			this.state.setMessage(I18n.format("signpic.advmsg.dlerror", e));
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(countoutput);
		}
	}
}
