package com.kamesuta.mc.signpic.http.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentCapacityOverException;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateCanceledException;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progress;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;
import com.kamesuta.mc.signpic.util.FileUtilitiy;

public class ContentDownload extends Communicate implements Progressable {
	protected final Content content;
	protected boolean canceled;

	public ContentDownload(final Content content) {
		this.content = content;
	}

	@Override
	public State getState() {
		return this.content.state;
	}

	@Override
	public void communicate() {
		File tmp = null;
		InputStream input = null;
		OutputStream output = null;
		try {
			final HttpUriRequest req = new HttpGet(ContentLocation.remoteLocation(this.content.meta.getData().url));
			final HttpResponse response = Downloader.downloader.client.execute(req);

			final HttpEntity entity = response.getEntity();
			this.content.meta.getData().mime = ContentType.getOrDefault(entity).getMimeType();

			tmp = Client.location.createCache("content");

			final long max = Config.instance.contentMaxByte;
			final long size = entity.getContentLength();
			if (max>0&&(size<0||size>max)) {
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
			final File local = ContentLocation.cacheLocation(this.content.meta.getData().cache);
			if (!FileUtils.deleteQuietly(local))
				Reference.logger.info("could not delete");
			FileUtils.moveFile(tmp, local);
			this.content.meta.getData().update = System.currentTimeMillis();
			this.content.meta.getData().cachemd5 = FileUtilitiy.createHash(local);
			this.content.meta.getData().size = local.length();
			onDone(new CommunicateResponse(true, null));
		} catch (final FileExistsException e) {
			this.content.meta.getData().dltry++;
			Reference.logger.error(e.getMessage(), e);
			onDone(new CommunicateResponse(false, e));
		} catch (final Exception e) {
			this.content.meta.getData().dltry++;
			onDone(new CommunicateResponse(false, e));
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(tmp);
			this.content.meta.save();
		}
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}
}
