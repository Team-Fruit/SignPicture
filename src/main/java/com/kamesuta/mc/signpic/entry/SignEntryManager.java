package com.kamesuta.mc.signpic.entry;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.handler.CoreEvent;

public class SignEntryManager implements ITickEntry {
	public static final SignEntryManager instance = new SignEntryManager();

	public final ExecutorService threadpool = Executors.newFixedThreadPool(3);
	private final Map<EntryId, EntrySlot<SignEntry>> registry = Maps.newHashMap();

	public SignEntry get(final EntryId id) {
		final EntrySlot<SignEntry> entries = this.registry.get(id);
		if (entries!=null)
			return entries.get();
		else {
			final SignEntry entry = new SignEntry(id);
			this.registry.put(id, new EntrySlot<SignEntry>(entry));
			return entry;
		}
	}

	@CoreEvent
	@Override
	public void onTick() {
		for (final Iterator<Entry<EntryId, EntrySlot<SignEntry>>> itr = this.registry.entrySet().iterator(); itr.hasNext();) {
			final Entry<EntryId, EntrySlot<SignEntry>> entry = itr.next();
			final EntrySlot<SignEntry> collectableSignEntry = entry.getValue();

			if (collectableSignEntry.shouldCollect()) {
				itr.remove();
			}
		}
	}
}
