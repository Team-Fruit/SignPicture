package com.kamesuta.mc.signpic.upload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kamesuta.mc.signpic.Config;

public class Uploader {
	public static Uploader instance = new Uploader();

	private final ExecutorService threadpool = Executors.newFixedThreadPool(Config.instance.contentLoadThreads);

	public <RES extends IUploadResponse<? extends IUploadResult>> void upload(final IUpload<RES> upload, final IUploadCallback<RES> callback) {
		this.threadpool.execute(new Runnable() {
			@Override
			public void run() {
				callback.onDone(upload.upload());
			}
		});
	}
}
