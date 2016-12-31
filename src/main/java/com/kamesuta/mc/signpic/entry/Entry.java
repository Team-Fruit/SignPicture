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

	private transient @Nullable CompoundAttr meta;
	private @Nullable String cmetacache;

	protected Entry(final @Nonnull EntryId id) {
		this.id = id;
		this.contentId = id.getContentId();
		this.gui = new GuiImage(this);
	}

	public @Nullable Content getContent() {
		if (this.contentId!=null)
			return this.contentId.content();
		else
			return null;
	}

	public boolean isNotSupported() {
		final CompoundAttr meta = getMeta();
		return meta.hasInvalidMeta()||this.id.getPrePrefix()!=null;
	}

	public boolean isValid() {
		return this.contentId!=null;
	}

	public @Nonnull CompoundAttr getMeta() {
		final Content cntnt = getContent();
		final String newmeta = cntnt!=null ? cntnt.imagemeta : null;
		if (this.contentId!=null&&newmeta!=null)
			if (!StringUtils.equals(this.cmetacache, newmeta)) {
				final String meta1 = this.id.getMetaSource();
				if (meta1!=null)
					this.meta = new CompoundAttr(meta1+newmeta);
				this.cmetacache = newmeta;
			}
		if (this.meta==null)
			this.meta = this.id.getMeta();
		if (this.meta!=null)
			return this.meta;
		return this.meta = CompoundAttr.Blank;
	}

	public @Nullable CompoundAttrBuilder getMetaBuilder() {
		final Content cntnt = getContent();
		final String newmeta = cntnt!=null ? cntnt.imagemeta : null;
		if (this.contentId!=null&&newmeta!=null)
			return new CompoundAttrBuilder().parse(this.id.getMetaSource()+newmeta);
		return this.id.getMetaBuilder();
	}
}
