package com.kamesuta.mc.signpic.entry.content.meta;

import java.io.File;

public class ContentMeta {
	private final MetaIO<MetaData> metaio;

	public ContentMeta(final File location) {
		this.metaio = new MetaIO<MetaData>(location, MetaData.class) {
			@Override
			public MetaData createBlank() {
				return new MetaData();
			}
		};
	}

	public String getURL() {
		return this.metaio.get().url;
	}

	public ContentMeta setURL(final String url) {
		this.metaio.get().url = url;
		this.metaio.save();
		return this;
	}

	public URIStacks getURLStack() {
		return this.metaio.get().urlstack;
	}

	public ContentMeta setURLStack(final URIStacks urlstack) {
		this.metaio.get().urlstack = urlstack;
		this.metaio.save();
		return this;
	}

	public String getMetaID() {
		return this.metaio.get().metaid;
	}

	public ContentMeta setMetaID(final String metaid) {
		this.metaio.get().metaid = metaid;
		this.metaio.save();
		return this;
	}

	public String getCacheID() {
		return this.metaio.get().cacheid;
	}

	public ContentMeta setCacheID(final String cacheid) {
		this.metaio.get().cacheid = cacheid;
		this.metaio.save();
		return this;
	}

	public int getTryCount() {
		return this.metaio.get().trycount;
	}

	public ContentMeta setTryCount(final int trycount) {
		this.metaio.get().trycount = trycount;
		this.metaio.save();
		return this;
	}

	public boolean isBlocked() {
		return this.metaio.get().blocked;
	}

	public ContentMeta setBlocked(final boolean blocked) {
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
		public String url;
		/**
		 * content URL Stacks
		 */
		public URIStacks urlstack;
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
		 * user block
		 */
		public boolean blocked;

		@Override
		public int getFormat() {
			return this.format;
		}
	}
}
