package com.kamesuta.mc.signpic.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ThreadUtils {
	public static @Nonnull ExecutorService newFixedCachedThreadPool(final int nThreads, final String threadnameformat) {
		return new ThreadPoolExecutor(nThreads, nThreads,
				60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				new ThreadFactoryBuilder().setNameFormat(threadnameformat).build());
	}
}
