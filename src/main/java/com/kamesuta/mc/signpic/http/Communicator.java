package com.kamesuta.mc.signpic.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kamesuta.mc.signpic.Config;

public class Communicator {
	public static Communicator instance = new Communicator();

	private final ExecutorService threadpool = Executors.newFixedThreadPool(Config.instance.contentLoadThreads);

	public <RES> void upload(final ICommunicate<RES> upload, final ICommunicateCallback<RES> callback) {
		this.threadpool.execute(new Runnable() {
			@Override
			public void run() {
				callback.onDone(upload.communicate());
			}
		});
	}
}
