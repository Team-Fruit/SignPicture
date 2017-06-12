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
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;

public class ImgurUpload extends Communicate implements Progressable, IUploader {
	protected @Nonnull UploadRequest upreq;
	protected @Nonnull String key;
	protected boolean canceled;
	protected @Nullable ImgurResult result;

	public ImgurUpload(final @Nonnull UploadRequest upload, final @Nonnull String key) {
		this.upreq = upload;
		this.key = key;
	}

	@Override
	public @Nonnull State getState() {
		return this.upreq.getState("§3Imgur: §r%s");
	}

	@Override
	public void communicate() {
		final String url = "https://api.imgur.com/3/image";

		File tmp = null;
		InputStream resstream = null;
		InputStream countupstream = null;
		JsonReader jsonReader1 = null;
		try {
			setCurrent();
			tmp = Client.getLocation().createCache("imgur");
			FileUtils.copyInputStreamToFile(this.upreq.getStream(), tmp);

			// create the post request.
			final HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Authorization", "Client-ID "+this.key);
			final MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			final State state = getState();
			countupstream = new CountingInputStream(new FileInputStream(tmp)) {
				@Override
				protected void beforeRead(final int n) throws IOException {
					if (ImgurUpload.this.canceled) {
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

			builder.addBinaryBody("image", countupstream, ContentType.DEFAULT_BINARY, this.upreq.getName());
			httppost.setEntity(builder.build());

			// execute request
			final HttpResponse response = Downloader.downloader.client.execute(httppost);

			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				final HttpEntity resEntity = response.getEntity();
				if (resEntity!=null) {
					resstream = resEntity.getContent();
					this.result = Client.gson.<ImgurResult> fromJson(jsonReader1 = new JsonReader(new InputStreamReader(resstream, Charsets.UTF_8)), ImgurResult.class);
					final String link = getLink();
					if (link!=null) {
						final Content content = ContentId.from(link).content();
						final String id = content.meta.getCacheID();
						if (id!=null)
							FileUtils.moveFile(tmp, ContentLocation.cacheLocation(id));
					}
					onDone(new CommunicateResponse(this.result!=null&&this.result.success, null));
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

	public static class ImgurResult {
		public @Nullable Data data;
		public boolean success;
		public int status;

		public static class Data {
			public @Nullable String id;
			public @Nullable String title;
			public @Nullable String description;
			public int datetime;
			public @Nullable String type;
			public boolean animated;
			public int width;
			public int height;
			public int size;
			public int views;
			public int bandwidth;
			public @Nullable String vote;
			public boolean favorite;
			public @Nullable String nsfw;
			public @Nullable String section;
			public @Nullable String account_url;
			public int account_id;
			public boolean is_ad;
			public boolean in_gallery;
			public @Nullable String deletehash;
			public @Nullable String name;
			public @Nullable String link;
			public @Nullable String gifv;
			public @Nullable String mp4;
			public int mp4_size;
			public boolean looping;
		}
	}

	@Override
	public @Nullable String getLink() {
		if (this.result!=null&&this.result.data!=null)
			return this.result.data.link;
		return null;
	}
}
