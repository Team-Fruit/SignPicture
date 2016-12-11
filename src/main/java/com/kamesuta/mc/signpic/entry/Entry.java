package com.kamesuta.mc.signpic.entry;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.gui.GuiImage;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;
import com.kamesuta.mc.signpic.image.meta.MetaBuilder;

public class Entry {
	public final EntryId id;
	public final ContentId contentId;
	public final GuiImage gui;

	private transient ImageMeta meta;
	private String cmetacache;

	protected Entry(final EntryId id) {
		this.id = id;
		this.contentId = id.getContentId();
		this.gui = new GuiImage(this);
	}

	public Content content() {
		return this.contentId.content();
	}

	public boolean isNotSupported() {
		return getMeta().hasInvalidMeta()||this.id.hasPrePrefix();
	}

	public boolean isValid() {
		return this.contentId!=null&&getMeta()!=null;
	}

	public ImageMeta getMeta() {
		final String newmeta = content().imagemeta;
		if (this.contentId!=null&&newmeta!=null)
			if (!StringUtils.equals(this.cmetacache, newmeta)) {
				final String meta1 = this.id.getMetaSource();
				if (meta1!=null)
					this.meta = new ImageMeta(meta1+newmeta);
				this.cmetacache = newmeta;
			}
		if (this.meta==null)
			this.meta = this.id.getMeta();
		return this.meta;
	}

	public MetaBuilder getMetaBuilder() {
		final String newmeta = content().imagemeta;
		if (this.contentId!=null&&newmeta!=null)
			return new MetaBuilder(this.id.getMetaSource()+newmeta);
		return this.id.getMetaBuilder();
	}
}
