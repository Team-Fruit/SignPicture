package com.kamesuta.mc.signpic.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.util.NotifyCollections.NotifyArrayDeque;

public class Communicator {
	public static Communicator instance = new Communicator();

	private final NotifyArrayDeque<Progressable> tasks = new NotifyArrayDeque<Progressable>();
	private final ExecutorService threadpool = Executors.newFixedThreadPool(Config.instance.communicateThreads,
			new ThreadFactoryBuilder().setNameFormat("signpic-http-%d").build());

	public NotifyArrayDeque<Progressable> getTasks() {
		return this.tasks;
	}

	public void communicate(final ICommunicate communicate) {
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
