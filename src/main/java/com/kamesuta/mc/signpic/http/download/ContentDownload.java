package com.kamesuta.mc.signpic.http.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.LoadCanceledException;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentCapacityOverException;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.entry.content.meta.ContentCache;
import com.kamesuta.mc.signpic.entry.content.meta.URIStacks;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;

public class ContentDownload extends Communicate implements Progressable {
	private final @Nonnull Content content;
	private boolean canceled;

	public ContentDownload(final @Nonnull Content content) {
		this.content = content;
	}

	@Override
	public @Nonnull State getState() {
		return this.content.state;
	}

	@Override
	public void communicate() {
		File tmp = null;
		InputStream input = null;
		OutputStream output = null;
		try {
			setCurrent();
			final String url = this.content.meta.getURL();
			if (url==null)
				throw new URISyntaxException("", "URL input is null");
			final URI base = ContentLocation.remoteLocation(url);
			final HttpUriRequest req = new HttpGet(base);
			final HttpClientContext context = HttpClientContext.create();
			final HttpResponse response = Downloader.downloader.client.execute(req, context);

			final List<URI> redirects = context.getRedirectLocations();
			final URIStacks uristacks = URIStacks.from(base, redirects);
			this.content.meta.setURLStack(uristacks);
			this.content.meta.setImageMeta(uristacks.getMetaString());

			final String end = uristacks.getEndPoint().toString();
			final String endid = ContentLocation.hash(end);
			this.content.meta.setCacheID(endid);
			final ContentCache cachemeta = new ContentCache(ContentLocation.cachemetaLocation(endid));
			cachemeta.setURL(end);

			final File cachefile = ContentLocation.cacheLocation(endid);
			if (cachemeta.isAvailable()&&!cachemeta.isDirty()&&cachefile.exists()) {
				req.abort();
				onDone(new CommunicateResponse(true, null));
				return;
			}

			cachemeta.setDirty(false);

			final HttpEntity entity = response.getEntity();
			cachemeta.setMime(ContentType.getOrDefault(entity).getMimeType());

			tmp = Client.getLocation().createCache("content");

			final long max = Config.getConfig().contentMaxByte.get();
			final long size = entity.getContentLength();
			if (max>0&&size>max) {
				req.abort();
				throw new ContentCapacityOverException("size: "+size);
			}

			final Progress progress = this.content.state.getProgress();
			progress.setOverall(entity.getContentLength());
			input = entity.getContent();
			output = new CountingOutputStream(new FileOutputStream(tmp)) {
				@Override
				protected void afterWrite(final int n) throws IOException {
					if (ContentDownload.this.canceled) {
						req.abort();
						throw new LoadCanceledException();
					}
					final long bcount = getByteCount();
					if (max>0&&bcount>max) {
						req.abort();
						throw new ContentCapacityOverException();
					}
					progress.setDone(bcount);
				}
			};
			IOUtils.copyLarge(input, output);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(cachefile);
			FileUtils.moveFile(tmp, cachefile);
			cachemeta.setLastUpdated(System.currentTimeMillis());
			cachemeta.setSize(cachefile.length());
			onDone(new CommunicateResponse(true, null));
		} catch (final Exception e) {
			onDone(new CommunicateResponse(false, e));
		} finally {
			unsetCurrent();
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(tmp);
		}
	}

	@Override
	public void cancel() {
		this.canceled = true;
		super.cancel();
	}
}
