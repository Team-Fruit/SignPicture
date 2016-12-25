package com.kamesuta.mc.signpic.entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.attr.CompoundAttrBuilder;
import com.kamesuta.mc.signpic.entry.content.ContentId;

public class EntryIdBuilder {
	private CompoundAttrBuilder meta;
	private String uri;

	public EntryIdBuilder() {
	}

	public EntryIdBuilder load(final @Nullable EntryId source) {
		if (source!=null) {
			this.meta = source.getMetaBuilder();
			final ContentId id = source.getContentId();
			if (id!=null)
				this.uri = id.getURI();
		}
		return this;
	}

	public void setMeta(final CompoundAttrBuilder meta) {
		this.meta = meta;
	}

	public CompoundAttrBuilder getMeta() {
		if (this.meta==null)
			return this.meta = new CompoundAttrBuilder();
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

	public @Nonnull EntryId build() {
		return new EntryId("#"+new ContentId(getURI()).getID()+getMeta().compose());
	}
}
