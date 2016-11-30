package com.kamesuta.mc.signpic.entry.content;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.ITickEntry;

public class ContentManager implements ITickEntry {
	public static ContentManager instance = new ContentManager();

	public final ExecutorService threadpool = Executors.newFixedThreadPool(Config.instance.contentLoadThreads,
			new ThreadFactoryBuilder().setNameFormat("signpic-content-%d").build());
	private final Map<ContentId, ContentSlot<Content>> registry = Maps.newConcurrentMap();
	private final Queue<ContentSlot<Content>> loadqueue = Queues.newConcurrentLinkedQueue();
	private final Queue<IDivisionProcessable> divisionqueue = Queues.newConcurrentLinkedQueue();
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
		this.divisionqueue.offer(divisionProcessable);
	}

	protected Content get(final ContentId id) {
		final ContentSlot<Content> entries = this.registry.get(id);
		if (entries!=null)
			return entries.get();
		else {
			final Content entry = new Content(id);
			final ContentSlot<Content> slot = new ContentSlot<Content>(entry);
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
			final ContentSlot<Content> loadprogress = this.loadqueue.poll();
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
						this.divisionqueue.poll();
				} catch (final Exception e) {
					e.printStackTrace();
				}
		}

		for (final Iterator<Entry<ContentId, ContentSlot<Content>>> itr = this.registry.entrySet().iterator(); itr.hasNext();) {
			final Entry<ContentId, ContentSlot<Content>> entry = itr.next();
			final ContentSlot<Content> collectableSignEntry = entry.getValue();

			if (collectableSignEntry.shouldCollect()) {
				this.loadqueue.remove(collectableSignEntry);
				collectableSignEntry.get().onCollect();
				itr.remove();
			}
		}
	}
}
