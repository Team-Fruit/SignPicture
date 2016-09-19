package com.kamesuta.mc.signpic.entry;

import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.util.Sign;

public class SignEntry {
	public final EntryId id;
	public final Sign sign;
	public ContentId path;

	public SignEntry(final EntryId id) {
		this.id = id;
		this.sign = new Sign().parseText(id.id());
		this.path = new ContentId(this.sign.getURL());
	}
}
