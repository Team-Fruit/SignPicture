package com.kamesuta.mc.signpic.http.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateCanceledException;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;

public class ImgurUpload extends Communicate implements Progressable, IUploader {
	public static final Gson gson = new Gson();

	private final String name;
	private final InputStream upstream;
	private final State state;
	private final String key;
	protected boolean canceled;
	protected ImgurResult result;

	protected ImgurUpload(final String name, final InputStream stream, final State state, final String key) {
		this.name = name;
		this.upstream = stream;
		this.state = state;
		this.key = key;
	}

	public ImgurUpload(final File file, final State state, final String key) throws FileNotFoundException {
		this.name = file.getName();
		this.upstream = new FileInputStream(file);
		state.setName("§3Imgur: §r"+this.name);
		state.getProgress().setOverall(file.length());
		this.state = state;
		this.key = key;
	}

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public void communicate() {
		final String url = "https://api.imgur.com/3/image";

		InputStream resstream = null;
		InputStream countupstream = null;
		try {
			// create the post request.
			final HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Authorization", "Client-ID "+this.key);
			final MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			countupstream = new CountingInputStream(this.upstream) {
				@Override
				protected void beforeRead(final int n) throws IOException {
					if (ImgurUpload.this.canceled) {
						httppost.abort();
						throw new CommunicateCanceledException();
					}
				}

				@Override
				protected void afterRead(final int n) {
					super.afterRead(n);
					ImgurUpload.this.state.getProgress().done = getByteCount();
				}
			};

			builder.addBinaryBody("image", countupstream, ContentType.DEFAULT_BINARY, this.name);
			httppost.setEntity(builder.build());

			// execute request
			final HttpResponse response = Downloader.downloader.client.execute(httppost);

			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				final HttpEntity resEntity = response.getEntity();
				if (this.upstream!=null) {
					resstream = resEntity.getContent();
					this.result = gson.<ImgurResult> fromJson(new JsonReader(new InputStreamReader(resstream, Charsets.UTF_8)), ImgurResult.class);
					onDone(new CommunicateResponse(this.result.success, null));
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
			IOUtils.closeQuietly(countupstream);
			IOUtils.closeQuietly(resstream);
		}
		onDone(new CommunicateResponse(false, null));
		return;
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}

	public static class ImgurResult {
		public Data data;
		public boolean success;
		public int status;

		public static class Data {
			public String id;
			public String title;
			public String description;
			public int datetime;
			public String type;
			public boolean animated;
			public int width;
			public int height;
			public int size;
			public int views;
			public int bandwidth;
			public String vote;
			public boolean favorite;
			public String nsfw;
			public String section;
			public String account_url;
			public int account_id;
			public boolean is_ad;
			public boolean in_gallery;
			public String deletehash;
			public String name;
			public String link;
			public String gifv;
			public String mp4;
			public int mp4_size;
			public boolean looping;
		}
	}

	@Override
	public String getLink() {
		if (this.result!=null&&this.result.data!=null)
			return this.result.data.link;
		return null;
	}
}
