package com.kamesuta.mc.signpic.entry.content.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Log;

public abstract class MetaIO<E extends IData> {
	public final @Nonnull File location;
	public final @Nonnull Class<E> clazz;
	private transient @Nullable E data;

	public MetaIO(final @Nonnull File location, final @Nonnull Class<E> clazz) {
		this.location = location;
		this.clazz = clazz;
	}

	public @Nonnull E get() {
		if (this.data!=null)
			return this.data;
		return this.data = loadData();
	}

	private @Nonnull E loadData() {
		JsonReader reader = null;
		try {
			if (this.location.exists()) {
				reader = new JsonReader(new InputStreamReader(new FileInputStream(this.location), Charsets.UTF_8));
				final E data = Client.gson.fromJson(reader, this.clazz);
				IOUtils.closeQuietly(reader);
				if (data!=null&&data.getFormat()==IData.FormatVersion)
					return data;
			}
		} catch (final Exception e) {
			Log.log.warn("content meta data is broken. aborted. ["+this.location.getName()+"]");
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return createBlank();
	}

	public abstract @Nonnull E createBlank();

	public void save() {
		saveData(this.data);
	}

	private void saveData(final @Nullable E data) {
		JsonWriter writer = null;
		try {
			writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(this.location), Charsets.UTF_8));
			Client.gson.toJson(data, this.clazz, writer);
		} catch (final Exception e) {
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
}
