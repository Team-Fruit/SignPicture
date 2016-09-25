package com.kamesuta.mc.signpic.entry.content;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.ITickEntry;
import com.kamesuta.mc.signpic.handler.CoreEvent;

public class ContentManager implements ITickEntry {
	public static ContentManager instance = new ContentManager();

	public final ExecutorService threadpool = Executors.newFixedThreadPool(3);
	protected final HashMap<ContentId, ContentSlot<Content>> registry = new HashMap<ContentId, ContentSlot<Content>>();
	public Deque<IAsyncProcessable> asyncqueue = new ArrayDeque<IAsyncProcessable>();
	public Deque<IDivisionProcessable> divisionqueue = new ArrayDeque<IDivisionProcessable>();

	public ContentManager() {
	}

	public Content get(final ContentId id) {
		final ContentSlot<Content> entries = this.registry.get(id);
		if (entries!=null)
			return entries.get();
		else {
			final Content entry = new Content(id);
			this.registry.put(id, new ContentSlot<Content>(entry));
			return entry;
		}
	}

	@CoreEvent
	@Override
	public void onTick() {
		IAsyncProcessable asyncprocess;
		if ((asyncprocess = this.asyncqueue.poll()) != null) {
			final IAsyncProcessable asyncprocessexec = asyncprocess;
			this.threadpool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						asyncprocessexec.onAsyncProcess();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		IDivisionProcessable divisionprocess;
		if ((divisionprocess = this.divisionqueue.peek()) != null) {
			try {
				if (divisionprocess.onDivisionProcess()) {
					this.divisionqueue.poll();
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		for (final Iterator<Entry<ContentId, ContentSlot<Content>>> itr = this.registry.entrySet().iterator(); itr.hasNext();) {
			final Entry<ContentId, ContentSlot<Content>> entry = itr.next();
			final ContentSlot<Content> collectableSignEntry = entry.getValue();

			if (collectableSignEntry.shouldInit()) {
				collectableSignEntry.onInit();
			}
			if (collectableSignEntry.shouldCollect()) {
				collectableSignEntry.get().onCollect();
				itr.remove();
			}
		}
	}
}
