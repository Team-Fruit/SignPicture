package com.kamesuta.mc.signpic.entry.content;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kamesuta.mc.signpic.entry.EntrySlot;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.ITickEntry;
import com.kamesuta.mc.signpic.handler.CoreEvent;

public class ContentManager implements ITickEntry {
	public static Deque<IDivisionProcessable> divisionqueue = new ArrayDeque<IDivisionProcessable>();
	public static final ExecutorService threadpool = Executors.newFixedThreadPool(3);
	protected final HashMap<ContentId, EntrySlot<Content>> registry = new HashMap<ContentId, EntrySlot<Content>>();

	public ContentLocation location;

	public ContentManager(final ContentLocation location) {
		this.location = location;
	}

	public Content get(final ContentId id) {
		final EntrySlot<Content> entries = this.registry.get(id);
		if (entries!=null)
			return entries.get();
		else {
			final Content entry = new Content(id);
			this.registry.put(id, new EntrySlot<Content>(entry));
			return entry;
		}
	}

	@CoreEvent
	@Override
	public void onTick() {
		IDivisionProcessable divisionprocess;
		if ((divisionprocess = ContentManager.divisionqueue.peek()) != null) {
			if (divisionprocess.onProcess()) {
				ContentManager.divisionqueue.poll();
			}
		}

		for (final Iterator<Entry<ContentId, EntrySlot<Content>>> itr = this.registry.entrySet().iterator(); itr.hasNext();) {
			final Entry<ContentId, EntrySlot<Content>> entry = itr.next();
			final EntrySlot<Content> collectableSignEntry = entry.getValue();

			if (collectableSignEntry.shouldInit()) {
				collectableSignEntry.onInit();
				executeProcess(collectableSignEntry);
			}
			if (collectableSignEntry.shouldCollect()) {
				collectableSignEntry.onCollect();
				itr.remove();
			}
		}
	}

	private void executeProcess(final IAsyncProcessable entry) {
		ContentManager.threadpool.execute(new Runnable() {
			@Override
			public void run() {
				entry.onProcess();
			}
		});
	}
}
