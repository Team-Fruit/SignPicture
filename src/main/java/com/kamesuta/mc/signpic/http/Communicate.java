package com.kamesuta.mc.signpic.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.util.Downloader;

public abstract class Communicate<T> implements ICommunicate<T> {
	protected abstract HttpUriRequest request() throws Exception;

	protected abstract ICommunicateResponse<T> response(HttpResponse response) throws Throwable;

	protected void before() {
	}

	protected void after() {
	}

	@Override
	public ICommunicateResponse<T> communicate() {
		before();
		try {
			return response(Downloader.downloader.client.execute(request()));
		} catch (final Throwable e) {
			return new CommunicateResponse<T>().setError(e);
		} finally {
			after();
		}
	}
}
