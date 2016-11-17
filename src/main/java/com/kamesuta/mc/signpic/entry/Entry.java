package com.kamesuta.mc.signpic.entry;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.gui.GuiImage;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;

public class Entry {
	public final EntryId id;
	public final ContentId contentId;
	public final ImageMeta meta;
	public final GuiImage gui;

	protected Entry(final EntryId id) {
		this.id = id;
		this.contentId = id.getContentId();
		this.meta = id.getMeta();
		this.gui = new GuiImage(this);
	}

	public Content content() {
		return this.contentId.content();
	}

	public boolean isNotSupported() {
		return this.meta.hasInvalidMeta()||this.id.hasPrePrefix();
	}

	public boolean isValid() {
		return this.contentId!=null&&this.meta!=null;
	}
}
