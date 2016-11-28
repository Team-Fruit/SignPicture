package com.kamesuta.mc.signpic.entry.content;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.entry.EntrySlot;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.entry.ITickEntry;

public class ContentManager implements ITickEntry {
	public static ContentManager instance = new ContentManager();

	public final ExecutorService threadpool = Executors.newFixedThreadPool(Config.instance.contentLoadThreads,
			new ThreadFactoryBuilder().setNameFormat("signpic-content-%d").build());
	private final HashMap<ContentId, ContentSlot> registry = new HashMap<ContentId, ContentSlot>();
	private final Deque<ContentSlot> loadqueue = new ArrayDeque<ContentSlot>();
	private final Deque<IDivisionProcessable> divisionqueue = new ArrayDeque<IDivisionProcessable>();
	private int loadtick = 0;
	private int divisiontick = 0;

	private ContentManager() {
	}

	public void enqueueAsync(final IAsyncProcessable asyncProcessable) {
		this.threadpool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					asyncProcessable.onAsyncProcess();
				} catch (final Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void enqueueDivision(final IDivisionProcessable divisionProcessable) {
		synchronized (this.divisionqueue) {
			this.divisionqueue.offer(divisionProcessable);
		}
	}

	protected Content get(final ContentId id) {
		final ContentSlot entries = this.registry.get(id);
		if (entries!=null)
			return entries.get();
		else {
			final Content entry = new Content(id);
			final ContentSlot slot = new ContentSlot(entry);
			this.registry.put(id, slot);
			this.loadqueue.offer(slot);
			return entry;
		}
	}

	@CoreEvent
	@Override
	public void onTick() {
		this.loadtick++;
		if (this.loadtick>Config.instance.contentLoadTick) {
			this.loadtick = 0;
			final ContentSlot loadprogress = this.loadqueue.poll();
			if (loadprogress!=null)
				loadprogress.onInit();
		}

		this.divisiontick++;
		if (this.divisiontick>Config.instance.contentSyncTick) {
			this.divisiontick = 0;
			IDivisionProcessable divisionprocess;
			if ((divisionprocess = this.divisionqueue.peek())!=null)
				try {
					if (divisionprocess.onDivisionProcess())
						synchronized (this.divisionqueue) {
							this.divisionqueue.poll();
						}
				} catch (final Exception e) {
					e.printStackTrace();
				}
		}

		for (final Iterator<Entry<ContentId, ContentSlot>> itr = this.registry.entrySet().iterator(); itr.hasNext();) {
			final Entry<ContentId, ContentSlot> entry = itr.next();
			final ContentSlot collectableSignEntry = entry.getValue();

			if (collectableSignEntry.shouldCollect()) {
				this.loadqueue.remove(collectableSignEntry);
				collectableSignEntry.get().onCollect();
				itr.remove();
			}
		}
	}

	public void reloadAll() {
		for (final Iterator<Entry<ContentId, ContentSlot>> itr = this.registry.entrySet().iterator(); itr.hasNext();) {
			final Entry<ContentId, ContentSlot> entry = itr.next();
			entry.getValue().get().markDirty();
		}
	}

	public void redownloadAll() {
		for (final Iterator<Entry<ContentId, ContentSlot>> itr = this.registry.entrySet().iterator(); itr.hasNext();) {
			final Entry<ContentId, ContentSlot> entry = itr.next();
			entry.getValue().get().markDirtyWithCache();
		}
	}

	public static class ContentSlot extends EntrySlot<Content> implements IInitable, ICollectable {
		public ContentSlot(final Content entry) {
			super(entry);
		}

		@Override
		public void onInit() {
			this.entry.onInit();
		}

		@Override
		public void onCollect() {
			this.entry.onCollect();
		}

		@Override
		public boolean shouldCollect() {
			return this.entry.shouldCollect()||super.shouldCollect();
		}

		@Override
		protected int getCollectTimes() {
			return Config.instance.contentGCtick;
		}
	}
}
