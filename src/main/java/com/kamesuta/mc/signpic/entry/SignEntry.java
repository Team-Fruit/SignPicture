package com.kamesuta.mc.signpic.entry;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.util.Sign;

public class SignEntry {
	public final EntryId id;
	public final Sign sign;
	public final ContentId contentId;
	public final Content content;

	public SignEntry(final EntryId id) {
		this.id = id;
		this.sign = new Sign().parseText(id.id());
		this.contentId = new ContentId(this.sign.getURL());
		this.content = ContentManager.instance.get(contentId);
	}
}
