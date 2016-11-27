package com.kamesuta.mc.signpic.entry.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kamesuta.mc.signpic.Reference;

public class ContentMeta {
	private static final Gson gson = new Gson();
	public static final int FormatVersion = 1;

	public final File location;
	private transient ContentData data;

	public ContentMeta(final File location) {
		this.location = location;
	}

	public ContentData getData() {
		if (this.data==null)
			this.data = loadData();
		return this.data;
	}

	private ContentData loadData() {
		JsonReader reader = null;
		try {
			if (this.location.exists()) {
				reader = new JsonReader(new InputStreamReader(new FileInputStream(this.location), Charsets.UTF_8));
				final ContentData data = gson.fromJson(reader, ContentData.class);
				IOUtils.closeQuietly(reader);
				if (data!=null&&data.format==FormatVersion)
					return data;
			}
		} catch (final IOException e) {
			Reference.logger.info("content meta data is broken. aborted.");
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return new ContentData();
	}

	public ContentMeta save() {
		saveData(this.data);
		return this;
	}

	private void saveData(final ContentData data) {
		JsonWriter writer = null;
		try {
			writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(this.location), Charsets.UTF_8));
			gson.toJson(data, ContentData.class, writer);
		} catch (final IOException e) {
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	public static class ContentData {
		public int format = FormatVersion;
		public String url;
		public String cache;
		public String meta;
		public String mime;
		public String cachemd5;
		public long size;
		public boolean fresh;
		public int countdltry;
		public boolean contentavailable;
		public long lastupdate;
	}
}
