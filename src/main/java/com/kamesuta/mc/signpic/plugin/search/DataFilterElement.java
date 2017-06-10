package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public interface DataFilterElement extends FilterElement {
	boolean filter(final SignData data, EntryId entry, ContentId content);
}
