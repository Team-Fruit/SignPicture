package com.kamesuta.mc.signpic.entry;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.handler.CoreEvent;

public class EntryManager implements ITickEntry {
	public static final EntryManager instance = new EntryManager();

	public final ExecutorService threadpool = Executors.newFixedThreadPool(3);
	private final Map<EntryId, EntrySlot<Entry>> registry = Maps.newHashMap();

	public Entry get(final EntryId id) {
		final EntrySlot<Entry> entries = this.registry.get(id);
		if (entries!=null)
			return entries.get();
		else {
			final Entry entry = new Entry(id);
			this.registry.put(id, new EntrySlot<Entry>(entry));
			return entry;
		}
	}

	@CoreEvent
	@Override
	public void onTick() {
		for (final Iterator<Map.Entry<EntryId, EntrySlot<Entry>>> itr = this.registry.entrySet().iterator(); itr.hasNext();) {
			final Map.Entry<EntryId, EntrySlot<Entry>> entry = itr.next();
			final EntrySlot<Entry> collectableSignEntry = entry.getValue();

			if (collectableSignEntry.shouldCollect()) {
				itr.remove();
			}
		}
	}
}
