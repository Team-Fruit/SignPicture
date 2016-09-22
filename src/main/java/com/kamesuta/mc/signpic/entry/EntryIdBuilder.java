package com.kamesuta.mc.signpic.entry;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.image.meta.ImageMeta;

public class EntryIdBuilder {
	private ImageMeta meta;
	private String uri;

	public EntryIdBuilder(final EntryId source) {
		this.meta = source.getMeta();
		this.uri = source.getContentId().getURI();
	}

	public EntryIdBuilder() {
	}

	public void setMeta(final ImageMeta meta) {
		this.meta = meta;
	}

	public ImageMeta getMeta() {
		if (this.meta==null)
			return this.meta = new ImageMeta();
		else
			return this.meta;
	}

	public void setURI(final String uri) {
		this.uri = uri;
	}

	public String getURI() {
		if (this.uri==null)
			return this.uri = "";
		else
			return this.uri;
	}

	public String getID() {
		String uri = getURI();
		if (StringUtils.contains(uri, "http://"))
			uri = StringUtils.substring(uri, 7, StringUtils.length(uri));
		else if (StringUtils.contains(uri, "https://"))
			uri = "$" + StringUtils.substring(uri, 8, StringUtils.length(uri));
		return uri;
	}

	public EntryId build() {
		return new EntryId(getID() + getMeta());
	}
}
