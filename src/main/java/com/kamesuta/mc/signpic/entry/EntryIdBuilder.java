package com.kamesuta.mc.signpic.entry;

import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;

public class EntryIdBuilder {
	private ImageMeta meta;
	private String uri;

	public EntryIdBuilder(final EntryId source) {
		if (source!=null) {
			this.meta = source.getMeta();
			if (source.hasContentId())
				this.uri = source.getContentId().getURI();
		}
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

	public EntryId build() {
		return new EntryId("#"+new ContentId(getURI()).getID()+getMeta());
	}
}
