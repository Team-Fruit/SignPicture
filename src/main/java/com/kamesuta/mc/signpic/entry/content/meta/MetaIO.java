package com.kamesuta.mc.signpic.entry.content.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kamesuta.mc.signpic.Reference;

public abstract class MetaIO<E extends IData> {
	private static final Gson gson = new Gson();

	public final File location;
	public final Class<E> clazz;
	private transient E data;

	public MetaIO(final File location, final Class<E> clazz) {
		this.location = location;
		this.clazz = clazz;
	}

	public E get() {
		if (this.data==null)
			this.data = loadData();
		return this.data;
	}

	private E loadData() {
		JsonReader reader = null;
		try {
			if (this.location.exists()) {
				reader = new JsonReader(new InputStreamReader(new FileInputStream(this.location), Charsets.UTF_8));
				final E data = gson.fromJson(reader, this.clazz);
				IOUtils.closeQuietly(reader);
				if (data!=null&&data.getFormat()==IData.FormatVersion)
					return data;
			}
		} catch (final Exception e) {
			Reference.logger.info("content meta data is broken. aborted. ["+this.location.getName()+"]");
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return createBlank();
	}

	public abstract E createBlank();

	public void save() {
		saveData(this.data);
	}

	private void saveData(final E data) {
		JsonWriter writer = null;
		try {
			writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(this.location), Charsets.UTF_8));
			gson.toJson(data, this.clazz, writer);
		} catch (final Exception e) {
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
}
