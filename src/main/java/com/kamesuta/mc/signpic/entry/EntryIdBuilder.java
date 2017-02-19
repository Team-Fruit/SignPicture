package com.kamesuta.mc.signpic.entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.attr.CompoundAttrBuilder;
import com.kamesuta.mc.signpic.entry.content.ContentId;

public class EntryIdBuilder {
	private @Nullable CompoundAttrBuilder meta;
	private @Nullable String uri;

	public EntryIdBuilder() {
	}

	public @Nonnull EntryIdBuilder load(final @Nullable EntryId source) {
		if (source!=null) {
			this.meta = source.getMetaBuilder();
			final ContentId id = source.getContentId();
			if (id!=null)
				this.uri = id.getURI();
		}
		return this;
	}

	public @Nonnull EntryIdBuilder setMeta(final @Nullable CompoundAttrBuilder meta) {
		this.meta = meta;
		return this;
	}

	public @Nonnull CompoundAttrBuilder getMeta() {
		if (this.meta!=null)
			return this.meta;
		else
			return this.meta = new CompoundAttrBuilder();
	}

	public @Nonnull EntryIdBuilder setURI(final @Nullable String uri) {
		this.uri = uri;
		return this;
	}

	public @Nonnull String getURI() {
		if (this.uri!=null)
			return this.uri;
		else
			return this.uri = "";
	}

	public @Nonnull EntryId build() {
		return EntryId.from("#"+ContentId.from(getURI()).getID()+getMeta().compose());
	}
}
