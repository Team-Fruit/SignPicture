package com.kamesuta.mc.signpic.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.util.Downloader;

public class GyazoUpload<T extends IUploadResult> implements IUpload<GyazoUpload.GyazoResponse> {
	public static final Gson gson = new Gson();

	private String name;
	private InputStream upstream;

	public GyazoUpload(final String name, final InputStream stream) {
		this.name = name;
		this.upstream = stream;
	}

	public GyazoUpload(final File file) throws FileNotFoundException {
		this(file.getName(), new FileInputStream(file));
	}

	@Override
	public GyazoResponse upload() {
		Reference.logger.info("upload start");
		final String url = "https://upload.gyazo.com/api/upload";

		// create the post request.
		final HttpPost httppost = new HttpPost(url);
		final MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		builder.addTextBody("access_token", "4d080e95be741beba0b74653a872668326a79526784d2daed9190dc584bffad7");
		builder.addBinaryBody("imagedata", this.upstream, ContentType.DEFAULT_BINARY, this.name);
		httppost.setEntity(builder.build());

		InputStream resstream = null;
		try {
			// execute request
			final HttpResponse response = Downloader.downloader.client.execute(httppost);

			if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
				final HttpEntity resEntity = response.getEntity();
				if (this.upstream!=null) {
					resstream = resEntity.getContent();
					return new GyazoResponse(gson.<GyazoResult>fromJson(new JsonReader(new InputStreamReader(resstream, Charsets.UTF_8)), GyazoResult.class));
				}
			}
		} catch (final Exception e) {
			return new GyazoResponse(e);
		} finally {
			IOUtils.closeQuietly(this.upstream);
			IOUtils.closeQuietly(resstream);
			Reference.logger.info("upload finish");
		}
		return new GyazoResponse();
	}

	public static class GyazoResponse implements IUploadResponse<GyazoResult> {
		private GyazoResult result;
		private Throwable error;

		public GyazoResponse(final GyazoResult result) {
			this.result = result;
		}

		public GyazoResponse(final Throwable error) {
			this.error = error;
		}

		public GyazoResponse() {
		}

		@Override
		public boolean isSuccess() {
			return this.result!=null;
		}

		@Override
		public GyazoResult getResult() {
			return this.result;
		}

		@Override
		public Throwable getError() {
			return this.error;
		}

	}

	public static class GyazoResult implements IUploadResult {
		public String created_at;
		public String image_id;
		public String permalink_url;
		public String thumb_url;
		public String type;
		public String url;

		@Override
		public String getLink() {
			return this.url;
		}
	}
}
