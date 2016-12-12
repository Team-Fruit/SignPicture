package com.kamesuta.mc.signpic.entry.content.meta;

import java.io.File;

public class ContentCache {
	private final MetaIO<CacheData> cacheio;

	public ContentCache(final File location) {
		this.cacheio = new MetaIO<CacheData>(location, CacheData.class) {
			@Override
			public CacheData createBlank() {
				return new CacheData();
			}
		};
	}

	public String getURL() {
		return this.cacheio.get().url;
	}

	public ContentCache setURL(final String url) {
		this.cacheio.get().url = url;
		this.cacheio.save();
		return this;
	}

	public String getMime() {
		return this.cacheio.get().mime;
	}

	public ContentCache setMime(final String mime) {
		this.cacheio.get().mime = mime;
		this.cacheio.save();
		return this;
	}

	public long getSize() {
		return this.cacheio.get().size;
	}

	public ContentCache setSize(final long size) {
		this.cacheio.get().size = size;
		this.cacheio.save();
		return this;
	}

	public boolean isDirty() {
		return this.cacheio.get().dirty;
	}

	public ContentCache setDirty(final boolean dirty) {
		this.cacheio.get().dirty = dirty;
		this.cacheio.save();
		return this;
	}

	public long getLastUpdated() {
		return this.cacheio.get().update;
	}

	public ContentCache setLastUpdated(final long update) {
		this.cacheio.get().update = update;
		this.cacheio.save();
		return this;
	}

	public boolean isAvailable() {
		return this.cacheio.get().available;
	}

	public ContentCache setAvailable(final boolean available) {
		this.cacheio.get().available = available;
		this.cacheio.save();
		return this;
	}

	/**
	 * cache meta data
	 * @author Kamesuta
	 */
	public static class CacheData implements IData {
		/**
		 * format version
		 */
		public int format = FormatVersion;
		/**
		 * content End URL
		 */
		public String url;
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

		@Override
		public int getFormat() {
			return this.format;
		}
	}
}
