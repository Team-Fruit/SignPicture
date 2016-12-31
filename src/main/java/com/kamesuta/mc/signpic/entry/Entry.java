package com.kamesuta.mc.signpic.entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.CompoundAttrBuilder;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.gui.GuiImage;

public class Entry {
	public final @Nonnull EntryId id;
	public final @Nullable ContentId contentId;
	public final @Nonnull GuiImage gui;

	private transient CompoundAttr meta;
	private String cmetacache;

	protected Entry(final @Nonnull EntryId id) {
		this.id = id;
		this.contentId = id.getContentId();
		this.gui = new GuiImage(this);
	}

	public @Nonnull Content content() {
		return this.contentId.content();
	}

	public boolean isNotSupported() {
		return getMeta().hasInvalidMeta()||this.id.hasPrePrefix();
	}

	public boolean isValid() {
		return this.contentId!=null&&getMeta()!=null;
	}

	public @Nullable CompoundAttr getMeta() {
		final String newmeta = content().imagemeta;
		if (this.contentId!=null&&newmeta!=null)
			if (!StringUtils.equals(this.cmetacache, newmeta)) {
				final String meta1 = this.id.getMetaSource();
				if (meta1!=null)
					this.meta = new CompoundAttr(meta1+newmeta);
				this.cmetacache = newmeta;
			}
		if (this.meta==null)
			this.meta = this.id.getMeta();
		return this.meta;
	}

	public CompoundAttrBuilder getMetaBuilder() {
		final String newmeta = content().imagemeta;
		if (this.contentId!=null&&newmeta!=null)
			return new CompoundAttrBuilder().parse(this.id.getMetaSource()+newmeta);
		return this.id.getMetaBuilder();
	}
}
