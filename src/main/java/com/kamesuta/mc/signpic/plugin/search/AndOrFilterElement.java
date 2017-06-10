package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public interface AndOrFilterElement extends FilterElement {

	boolean filter(final SignData data, final EntryId entry, final ContentId content, final AttrReaders attr);
}
