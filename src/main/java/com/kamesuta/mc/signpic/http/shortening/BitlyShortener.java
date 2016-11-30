package com.kamesuta.mc.signpic.http.shortening;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.Downloader;

public class BitlyShortener extends Communicate implements Progressable, IShortener {
	public static final Gson gson = new Gson();

	protected ShorteningRequest shortreq;
	protected String key;
	protected boolean canceled;
	protected BitlyResult result;

	public BitlyShortener(final ShorteningRequest shortreq, final String key) {
		this.shortreq = shortreq;
		this.key = key;
	}

	@Override
	public State getState() {
		return this.shortreq.getState("§dBitly: §r%s");
	}

	@Override
	public void communicate() {
		final String url = "https://api-ssl.bitly.com/v3/shorten?access_token=%s&longUrl=%s";

		final File tmp = null;
		InputStream resstream = null;
		JsonReader jsonReader1 = null;
		try {
			// create the get request.
			final HttpGet httpget = new HttpGet(String.format(url, this.key, this.shortreq.getLongURL()));

			// execute request
			final HttpResponse response = Downloader.downloader.client.execute(httpget);

			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				final HttpEntity resEntity = response.getEntity();
				if (resEntity!=null) {
					resstream = resEntity.getContent();
					this.result = gson.<BitlyResult> fromJson(jsonReader1 = new JsonReader(new InputStreamReader(resstream, Charsets.UTF_8)), BitlyResult.class);
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
	}

	public static class BitlyResult {
		public Data data;
		public int status_code;
		public String status_txt;

		public static class Data {
			public String long_url;
			public String url;
			public String hash;
			public String global_hash;
			public String new_hash;
		}
	}

	@Override
	public String getShortLink() {
		if (this.result!=null&&this.result.data!=null)
			return this.result.data.url;
		return null;
	}
}
