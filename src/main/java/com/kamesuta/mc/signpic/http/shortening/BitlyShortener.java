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
import org.apache.http.client.methods.HttpGet;

import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;

public class BitlyShortener extends Communicate implements Progressable, IShortener {
	protected @Nonnull ShorteningRequest shortreq;
	protected @Nonnull String key;
	protected @Nullable BitlyResult result;

	public BitlyShortener(final @Nonnull ShorteningRequest shortreq, final @Nonnull String key) {
		this.shortreq = shortreq;
		this.key = key;
	}

	@Override
	public @Nonnull State getState() {
		return this.shortreq.getState("§dbit.ly: §r%s");
	}

	@Override
	public void communicate() {
		final String url = "https://api-ssl.bitly.com/v3/shorten?access_token=%s&longUrl=%s";

		InputStream resstream = null;
		JsonReader jsonReader1 = null;
		try {
			setCurrent();
			// create the get request.
			final HttpGet httpget = new HttpGet(String.format(url, this.key, this.shortreq.getLongURL()));

			// execute request
			final HttpResponse response = Downloader.downloader.client.execute(httpget);

			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				final HttpEntity resEntity = response.getEntity();
				if (resEntity!=null) {
					resstream = resEntity.getContent();
					this.result = Client.gson.<BitlyResult> fromJson(jsonReader1 = new JsonReader(new InputStreamReader(resstream, Charsets.UTF_8)), BitlyResult.class);
					onDone(new CommunicateResponse(this.result!=null&&this.result.status_code==HttpStatus.SC_OK, null));
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

	public static class BitlyResult {
		public @Nullable Data data;
		public int status_code;
		public @Nullable String status_txt;

		public static class Data {
			public @Nullable String long_url;
			public @Nullable String url;
			public @Nullable String hash;
			public @Nullable String global_hash;
			public @Nullable String new_hash;
		}
	}

	@Override
	public @Nullable String getShortLink() {
		if (this.result!=null&&this.result.data!=null)
			return this.result.data.url;
		return null;
	}
}
