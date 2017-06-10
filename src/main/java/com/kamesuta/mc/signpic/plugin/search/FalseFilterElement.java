package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public class FalseFilterElement implements IFilterElement {
	public static final FalseFilterElement INSTANCE = new FalseFilterElement();

	@Override
	public boolean filter(final SignData data, final EntryId entry, final ContentId content) {
		return false;
	}

}
