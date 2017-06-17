package com.kamesuta.mc.signpic.http.shortening;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;

public class GooglShortener extends Communicate implements Progressable, IShortener {
	protected @Nonnull ShorteningRequest shortreq;
	protected @Nonnull String key;
	protected @Nullable GooglResult result;

	public GooglShortener(final @Nonnull ShorteningRequest shortreq, final @Nonnull String key) {
		this.shortreq = shortreq;
		this.key = key;
	}

	@Override
	public @Nonnull State getState() {
		return this.shortreq.getState("§dgoo.gl: §r%s");
	}

	@Override
	public void communicate() {
		final String url = "https://www.googleapis.com/urlshortener/v1/url?key=%s";

		InputStream resstream = null;
		JsonReader jsonReader1 = null;
		try {
			setCurrent();
			// create the get request.
			final HttpPost httppost = new HttpPost(String.format(url, this.key));
			final EntityBuilder builder = EntityBuilder.create();

			final String reqjson = Client.gson.toJson(new GooglRequest(this.shortreq.getLongURL()));
			builder.setContentType(ContentType.APPLICATION_JSON);
			builder.setText(reqjson);
			httppost.setEntity(builder.build());

			// execute request
			final HttpResponse response = Downloader.downloader.client.execute(httppost);

			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				final HttpEntity resEntity = response.getEntity();
				if (resEntity!=null) {
					resstream = resEntity.getContent();
					this.result = Client.gson.<GooglResult> fromJson(jsonReader1 = new JsonReader(new InputStreamReader(resstream, Charsets.UTF_8)), GooglResult.class);
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
			IOUtils.closeQuietly(resstream);
			IOUtils.closeQuietly(jsonReader1);
		}
		onDone(new CommunicateResponse(false, null));
		return;
	}

	public static class GooglRequest {
		public @Nonnull String longUrl;

		public GooglRequest(final @Nonnull String longUrl) {
			this.longUrl = longUrl;
		}
	}

	public static class GooglResult {
		public @Nullable String kind;
		public @Nullable String id;
		public @Nullable String longUrl;
	}

	@Override
	public @Nullable String getShortLink() {
		if (this.result!=null)
			return this.result.id;
		return null;
	}
}
