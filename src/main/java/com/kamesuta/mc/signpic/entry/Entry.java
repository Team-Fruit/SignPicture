package com.kamesuta.mc.signpic.entry;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;

public class Entry {
	public final EntryId id;
	public final ContentId contentId;
	public final ImageMeta meta;

	protected Entry(final EntryId id) {
		this.id = id;
		this.contentId = id.getContentId();
		this.meta = id.getMeta();
	}

	public Content content() {
		return ContentManager.instance.get(this.contentId);
	}

	public boolean isValid() {
		return this.contentId!=null && this.meta!=null;
	}
}
