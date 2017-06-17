package com.kamesuta.mc.signpic.entry;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.CoreEvent;

public class EntryManager implements ITickEntry {
	public static final @Nonnull EntryManager instance = new EntryManager();

	private final @Nonnull Map<EntryId, EntrySlot<Entry>> registry = Maps.newConcurrentMap();

	protected @Nonnull Entry get(final @Nonnull EntryId id) {
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

			if (collectableSignEntry.shouldCollect())
				itr.remove();
		}
	}
}
