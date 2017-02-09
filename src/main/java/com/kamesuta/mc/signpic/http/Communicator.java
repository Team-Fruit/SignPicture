package com.kamesuta.mc.signpic.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.NotifyArrayDeque;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.state.Progressable;

public class Communicator {
	public static @Nonnull Communicator instance = new Communicator();

	private final @Nonnull NotifyArrayDeque<Progressable> tasks = new NotifyArrayDeque<Progressable>();
	private final @Nonnull ExecutorService threadpool = Executors.newFixedThreadPool(Config.getConfig().communicateThreads.get(),
			new ThreadFactoryBuilder().setNameFormat("signpic-http-%d").build());

	public @Nonnull NotifyArrayDeque<Progressable> getTasks() {
		return this.tasks;
	}

	public void communicate(final @Nonnull ICommunicate communicate) {
		if (communicate instanceof Progressable)
			synchronized (this.tasks) {
				this.tasks.offer((Progressable) communicate);
			}
		this.threadpool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					communicate.communicate();
				} finally {
					synchronized (Communicator.this.tasks) {
						Communicator.this.tasks.remove(communicate);
					}
				}
			}
		});
	}
}
