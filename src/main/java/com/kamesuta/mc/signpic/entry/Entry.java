package com.kamesuta.mc.signpic.entry;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.gui.GuiImage;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;

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
		if (this.contentId!=null) {
			final String newmeta = content().meta;
			if (!StringUtils.equals(this.cmetacache, newmeta)) {
				this.meta = this.id.getMeta().parse(newmeta);
				this.cmetacache = newmeta;
			}
		}
		if (this.meta==null)
			this.meta = this.id.getMeta();
		return this.meta;
	}
}
