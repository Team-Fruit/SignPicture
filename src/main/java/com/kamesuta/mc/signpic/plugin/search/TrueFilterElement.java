package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public class TrueFilterElement implements DataFilterElement {
	public static final TrueFilterElement INSTANCE = new TrueFilterElement();

	@Override
	public boolean filter(final SignData data, final EntryId entry, final ContentId content) {
		return true;
	}

}
