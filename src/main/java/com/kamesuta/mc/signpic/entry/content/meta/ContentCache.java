package com.kamesuta.mc.signpic.entry.content.meta;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContentCache {
	private final @Nonnull MetaIO<CacheData> cacheio;

	public ContentCache(final @Nonnull File location) {
		this.cacheio = new MetaIO<CacheData>(location, CacheData.class) {
			@Override
			public @Nonnull CacheData createBlank() {
				return new CacheData();
			}
		};
	}

	public @Nullable String getURL() {
		return this.cacheio.get().url;
	}

	public @Nonnull ContentCache setURL(final @Nullable String url) {
		this.cacheio.get().url = url;
		this.cacheio.save();
		return this;
	}

	public @Nullable String getMime() {
		return this.cacheio.get().mime;
	}

	public @Nonnull ContentCache setMime(final @Nullable String mime) {
		this.cacheio.get().mime = mime;
		this.cacheio.save();
		return this;
	}

	public long getSize() {
		return this.cacheio.get().size;
	}

	public @Nonnull ContentCache setSize(final long size) {
		this.cacheio.get().size = size;
		this.cacheio.save();
		return this;
	}

	public boolean isDirty() {
		return this.cacheio.get().dirty;
	}

	public @Nonnull ContentCache setDirty(final boolean dirty) {
		this.cacheio.get().dirty = dirty;
		this.cacheio.save();
		return this;
	}

	public long getLastUpdated() {
		return this.cacheio.get().update;
	}

	public @Nonnull ContentCache setLastUpdated(final long update) {
		this.cacheio.get().update = update;
		this.cacheio.save();
		return this;
	}

	public boolean isAvailable() {
		return this.cacheio.get().available;
	}

	public @Nonnull ContentCache setAvailable(final boolean available) {
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
		public @Nullable String url;
		/**
		 * content MIME type
		 */
		public @Nullable String mime;
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
