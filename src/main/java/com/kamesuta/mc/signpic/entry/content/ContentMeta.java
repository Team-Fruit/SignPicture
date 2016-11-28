package com.kamesuta.mc.signpic.entry.content;

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
import com.kamesuta.mc.signpic.entry.content.ContentMeta.Data.Cache;

public class ContentMeta {
	public static final int FormatVersion = 3;

	private final MetaIO io;

	public ContentMeta(final File location) {
		this.io = new MetaIO(location);
	}

	public String getURL() {
		return this.io.get().url;
	}

	public ContentMeta setURL(final String url) {
		this.io.get().url = url;
		this.io.save();
		return this;
	}

	public String getMetaID() {
		return this.io.get().metaid;
	}

	public ContentMeta setMetaID(final String metaid) {
		this.io.get().metaid = metaid;
		this.io.save();
		return this;
	}

	public String getCacheID() {
		return this.io.get().cacheid;
	}

	public ContentMeta setCacheID(final String cacheid) {
		this.io.get().cacheid = cacheid;
		this.io.save();
		return this;
	}

	public int getTryCount() {
		return this.io.get().trycount;
	}

	public ContentMeta setTryCount(final int trycount) {
		this.io.get().trycount = trycount;
		this.io.save();
		return this;
	}

	public boolean isBlocked() {
		return this.io.get().blocked;
	}

	public ContentMeta setBlocked(final boolean blocked) {
		this.io.get().blocked = blocked;
		ContentMeta.this.io.save();
		return this;
	}

	public ContentCacheMeta getCache() {
		if (this.io.get().cache==null)
			this.io.get().cache = new Cache();
		return new ContentCacheMeta(this.io.get().cache);
	}

	public ContentMeta resetCache() {
		this.io.get().cache = null;
		this.io.save();
		return this;
	}

	public class ContentCacheMeta {
		private final Cache cache;

		public ContentCacheMeta(final Cache cache) {
			this.cache = cache;
		}

		public String getMime() {
			return this.cache.mime;
		}

		public ContentCacheMeta setMime(final String mime) {
			this.cache.mime = mime;
			ContentMeta.this.io.save();
			return this;
		}

		public long getSize() {
			return this.cache.size;
		}

		public ContentCacheMeta setSize(final long size) {
			this.cache.size = size;
			ContentMeta.this.io.save();
			return this;
		}

		public boolean isDirty() {
			return this.cache.dirty;
		}

		public ContentCacheMeta setDirty(final boolean dirty) {
			this.cache.dirty = dirty;
			ContentMeta.this.io.save();
			return this;
		}

		public long getLastUpdated() {
			return this.cache.update;
		}

		public ContentCacheMeta setLastUpdated(final long update) {
			this.cache.update = update;
			ContentMeta.this.io.save();
			return this;
		}

		public boolean isAvailable() {
			return this.cache.available;
		}

		public ContentCacheMeta setAvailable(final boolean available) {
			this.cache.available = available;
			ContentMeta.this.io.save();
			return this;
		}
	}

	/**
	 * content meta data
	 * @author b7n
	 */
	public static class Data {
		/**
		 * format version
		 */
		public int format = FormatVersion;
		/**
		 * content URL
		 */
		public String url;
		/**
		 * meta ID
		 */
		public String metaid;
		/**
		 * cache ID
		 */
		public String cacheid;
		/**
		 * download try count
		 */
		public int trycount;
		/**
		 * cache meta data
		 */
		public Cache cache;
		/**
		 * user block
		 */
		public boolean blocked;

		/**
		 * cache meta data
		 * @author Kamesuta
		 */
		public static class Cache {
			/**
			 * content MIME type
			 */
			public String mime;
			/**
			 * content length
			 */
			public long size;
			/**
			 * cache dirty mark
			 */
			public boolean dirty;
			/**
			 * last cache updated
			 */
			public long update;
			/**
			 * has content ever been available
			 */
			public boolean available;
		}
	}

	public static class MetaIO {
		private static final Gson gson = new Gson();

		public final File location;
		private transient Data data;

		public MetaIO(final File location) {
			this.location = location;
		}

		public Data get() {
			if (this.data==null)
				this.data = loadData();
			return this.data;
		}

		private Data loadData() {
			JsonReader reader = null;
			try {
				if (this.location.exists()) {
					reader = new JsonReader(new InputStreamReader(new FileInputStream(this.location), Charsets.UTF_8));
					final Data data = gson.fromJson(reader, Data.class);
					IOUtils.closeQuietly(reader);
					if (data!=null&&data.format==FormatVersion)
						return data;
				}
			} catch (final Exception e) {
				Reference.logger.info("content meta data is broken. aborted. ["+this.location.getName()+"]");
			} finally {
				IOUtils.closeQuietly(reader);
			}
			return new Data();
		}

		public void save() {
			saveData(this.data);
		}

		private void saveData(final Data data) {
			JsonWriter writer = null;
			try {
				writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(this.location), Charsets.UTF_8));
				gson.toJson(data, Data.class, writer);
			} catch (final Exception e) {
			} finally {
				IOUtils.closeQuietly(writer);
			}
		}
	}
}
