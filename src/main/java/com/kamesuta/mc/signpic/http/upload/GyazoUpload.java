package com.kamesuta.mc.signpic.http.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.LoadCanceledException;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;

public class GyazoUpload extends Communicate implements Progressable, IUploader {
	protected @Nonnull UploadRequest upreq;
	protected @Nonnull String key;
	protected boolean canceled;
	protected @Nullable GyazoResult result;

	public GyazoUpload(final @Nonnull UploadRequest upload, final @Nonnull String key) {
		this.upreq = upload;
		this.key = key;
	}

	@Override
	public @Nonnull State getState() {
		return this.upreq.getState("§3Gyazo: §r%s");
	}

	@Override
	public void communicate() {
		final String url = "https://upload.gyazo.com/api/upload";

		File tmp = null;
		InputStream resstream = null;
		InputStream countupstream = null;
		JsonReader jsonReader1 = null;
		try {
			setCurrent();
			tmp = Client.getLocation().createCache("gyazo");
			FileUtils.copyInputStreamToFile(this.upreq.getStream(), tmp);

			// create the post request.
			final HttpPost httppost = new HttpPost(url);
			final MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			final State state = getState();
			countupstream = new CountingInputStream(new FileInputStream(tmp)) {
				@Override
				protected void beforeRead(final int n) throws IOException {
					if (GyazoUpload.this.canceled) {
						httppost.abort();
						throw new LoadCanceledException();
					}
				}

				@Override
				protected void afterRead(final int n) {
					super.afterRead(n);
					state.getProgress().done = getByteCount();
				}
			};

			builder.addTextBody("access_token", this.key);
			builder.addBinaryBody("imagedata", countupstream, ContentType.DEFAULT_BINARY, this.upreq.getName());
			httppost.setEntity(builder.build());

			// execute request
			final HttpResponse response = Downloader.downloader.client.execute(httppost);

			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				final HttpEntity resEntity = response.getEntity();
				if (resEntity!=null) {
					resstream = resEntity.getContent();
					this.result = Client.gson.<GyazoResult> fromJson(jsonReader1 = new JsonReader(new InputStreamReader(resstream, Charsets.UTF_8)), GyazoResult.class);
					final String link = getLink();
					if (link!=null)
						FileUtils.moveFile(tmp, ContentLocation.cacheLocation(ContentLocation.hash(link)));
					onDone(new CommunicateResponse(true, null));
					return;
				}
			} else {
				onDone(new CommunicateResponse(false, new IOException("Bad Response")));
				return;
			}
		} catch (final Exception e) {
			onDone(new CommunicateResponse(false, e));
			return;
		} finally {
			unsetCurrent();
			IOUtils.closeQuietly(countupstream);
			IOUtils.closeQuietly(resstream);
			IOUtils.closeQuietly(jsonReader1);
			FileUtils.deleteQuietly(tmp);
		}
		onDone(new CommunicateResponse(false, null));
		return;
	}

	@Override
	public void cancel() {
		this.canceled = true;
		super.cancel();
	}

	public static class GyazoResult {
		public @Nullable String created_at;
		public @Nullable String image_id;
		public @Nullable String permalink_url;
		public @Nullable String thumb_url;
		public @Nullable String type;
		public @Nullable String url;
	}

	@Override
	public @Nullable String getLink() {
		if (this.result!=null)
			return this.result.url;
		return null;
	}
}
