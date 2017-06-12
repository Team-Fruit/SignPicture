package com.kamesuta.mc.signpic.entry.content.meta;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContentMeta {
	private final MetaIO<MetaData> metaio;

	public ContentMeta(final @Nonnull File location) {
		this.metaio = new MetaIO<MetaData>(location, MetaData.class) {
			@Override
			public @Nonnull MetaData createBlank() {
				return new MetaData();
			}
		};
	}

	public @Nullable String getURL() {
		return this.metaio.get().url;
	}

	public @Nonnull ContentMeta setURL(final String url) {
		this.metaio.get().url = url;
		this.metaio.save();
		return this;
	}

	public @Nullable URIStacks getURLStack() {
		return this.metaio.get().urlstack;
	}

	public @Nonnull ContentMeta setURLStack(final URIStacks urlstack) {
		this.metaio.get().urlstack = urlstack;
		this.metaio.save();
		return this;
	}

	public @Nullable String getImageMeta() {
		return this.metaio.get().imagemeta;
	}

	public @Nonnull ContentMeta setImageMeta(final String imagemeta) {
		this.metaio.get().imagemeta = imagemeta;
		this.metaio.save();
		return this;
	}

	public @Nullable String getMetaID() {
		return this.metaio.get().metaid;
	}

	public @Nonnull ContentMeta setMetaID(final String metaid) {
		this.metaio.get().metaid = metaid;
		this.metaio.save();
		return this;
	}

	public @Nullable String getCacheID() {
		return this.metaio.get().cacheid;
	}

	public @Nonnull ContentMeta setCacheID(final String cacheid) {
		this.metaio.get().cacheid = cacheid;
		this.metaio.save();
		return this;
	}

	public int getTryCount() {
		return this.metaio.get().trycount;
	}

	public @Nonnull ContentMeta setTryCount(final int trycount) {
		this.metaio.get().trycount = trycount;
		this.metaio.save();
		return this;
	}

	public boolean isBlocked() {
		return this.metaio.get().blocked;
	}

	public @Nonnull ContentMeta setBlocked(final boolean blocked) {
		this.metaio.get().blocked = blocked;
		ContentMeta.this.metaio.save();
		return this;
	}

	/**
	 * content meta data
	 * @author b7n
	 */
	public static class MetaData implements IData {
		/**
		 * format version
		 */
		public int format = FormatVersion;
		/**
		 * content Base URL
		 */
		public @Nullable String url;
		/**
		 * content URL Stacks
		 */
		public @Nullable URIStacks urlstack;
		/**
		 * image meta data
		 */
		public @Nullable String imagemeta;
		/**
		 * meta ID
		 */
		public @Nullable String metaid;
		/**
		 * cache ID
		 */
		public @Nullable String cacheid;
		/**
		 * download try count
		 */
		public int trycount;
		/**
		 * user block
		 */
		public boolean blocked;

		@Override
		public int getFormat() {
			return this.format;
		}
	}
}
