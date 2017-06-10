package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

@Deprecated
public class AndFilterElement implements AndOrFilterElement {

	public final @Nonnull DataFilterElement filter1;
	public final @Nonnull DataFilterElement filter2;

	public AndFilterElement(final DataFilterElement filter1, final DataFilterElement filter2) {
		this.filter1 = filter1;
		this.filter2 = filter2;
	}

	@Override
	public boolean filter(final SignData data, final EntryId entry, final ContentId content, final AttrReaders attr) {
		return get(this.filter1, data, entry, content, attr)&&get(this.filter2, data, entry, content, attr);
	}

	protected boolean get(final FilterElement element, final SignData data, final EntryId entry, final ContentId content, final AttrReaders attr) {
		if (element instanceof DataFilterElement)
			return ((DataFilterElement) element).filter(data, entry, content);
		else if (element instanceof AttrFilterElement)
			return ((AttrFilterElement) element).filter(attr);
		return false;
	}

}
