package net.teamfruit.signpic.http;

import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;

import net.teamfruit.bnnwidget.util.NotifyCollections.NotifyArrayDeque;
import net.teamfruit.signpic.Config;
import net.teamfruit.signpic.state.Progressable;
import net.teamfruit.signpic.util.ThreadUtils;

public class Communicator {
	public static @Nonnull Communicator instance = new Communicator();

	private final @Nonnull NotifyArrayDeque<Progressable> tasks = new NotifyArrayDeque<Progressable>();
	private final @Nonnull ExecutorService threadpool = ThreadUtils.newFixedCachedThreadPool(Config.getConfig().communicateThreads.get(), "signpic-http-%d");

	public @Nonnull NotifyArrayDeque<Progressable> getTasks() {
		return this.tasks;
	}

	public void communicate(final @Nonnull ICommunicate communicate) {
		if (communicate instanceof Progressable)
			synchronized (this.tasks) {
				this.tasks.offer((Progressable) communicate);
			}
		this.threadpool.execute(() -> {
			try {
				communicate.communicate();
			} finally {
				synchronized (Communicator.this.tasks) {
					Communicator.this.tasks.remove(communicate);
				}
			}
		});
	}
}
