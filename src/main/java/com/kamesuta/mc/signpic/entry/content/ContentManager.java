package com.kamesuta.mc.signpic.entry.content;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.entry.EntrySlot;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.entry.ITickEntry;
import com.kamesuta.mc.signpic.util.ThreadUtils;

import net.minecraftforge.client.event.TextureStitchEvent;

public class ContentManager implements ITickEntry {
	public static @Nonnull ContentManager instance = new ContentManager();

	public final @Nonnull ExecutorService threadpool = ThreadUtils.newFixedCachedThreadPool(Config.getConfig().contentLoadThreads.get(), "signpic-content-%d");
	private final @Nonnull Map<ContentId, ContentSlot> registry = Maps.newConcurrentMap();
	private final @Nonnull Queue<ContentSlot> loadqueue = Queues.newConcurrentLinkedQueue();
	private final @Nonnull Queue<IDivisionProcessable> divisionqueue = Queues.newConcurrentLinkedQueue();
	private int loadtick = 0;
	private int divisiontick = 0;

	private ContentManager() {
	}

	public void enqueueAsync(final @Nonnull IAsyncProcessable asyncProcessable) {
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

	public void enqueueDivision(final @Nonnull IDivisionProcessable divisionProcessable) {
		this.divisionqueue.offer(divisionProcessable);
	}

	protected @Nonnull Content get(final @Nonnull ContentId id) {
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
		if (this.loadtick>Config.getConfig().contentLoadTick.get()) {
			this.loadtick = 0;
			final ContentSlot loadprogress = this.loadqueue.poll();
			if (loadprogress!=null)
				loadprogress.onInit();
		}

		this.divisiontick++;
		if (this.divisiontick>Config.getConfig().contentSyncTick.get()) {
			this.divisiontick = 0;
			IDivisionProcessable divisionprocess;
			if ((divisionprocess = this.divisionqueue.peek())!=null)
				try {
					if (divisionprocess.onDivisionProcess())
						this.divisionqueue.poll();
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

	@CoreEvent
	public void onResourceReloaded(final @Nonnull TextureStitchEvent.Post event) {
		reloadAll();
	}

	public static class ContentSlot extends EntrySlot<Content> implements IInitable, ICollectable {
		public ContentSlot(final @Nonnull Content entry) {
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
			return Config.getConfig().contentGCtick.get();
		}
	}
}
