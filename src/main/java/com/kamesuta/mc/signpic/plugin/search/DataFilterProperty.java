package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public interface DataFilterProperty<E> {

	@Nullable
	E get(SignData data, EntryId entry, ContentId content);
}
