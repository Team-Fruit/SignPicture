package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

@Deprecated
public class AndFilterElement implements IFilterElement {

	public final @Nonnull IFilterElement filter1;
	public final @Nonnull IFilterElement filter2;

	public AndFilterElement(final IFilterElement filter1, final IFilterElement filter2) {
		this.filter1 = filter1;
		this.filter2 = filter2;
	}

	@Override
	public boolean filter(final SignData data, final EntryId entry, final ContentId content) {
		return this.filter1.filter(data, entry, content)&&this.filter2.filter(data, entry, content);
	}

}
