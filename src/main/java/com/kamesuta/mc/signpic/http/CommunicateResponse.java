package com.kamesuta.mc.signpic.http;

public class CommunicateResponse<T> implements ICommunicateResponse<T> {
	private T result;
	private Throwable error;

	public CommunicateResponse(final T result) {
		this.result = result;
	}

	public CommunicateResponse(final Throwable error) {
		this.error = error;
	}

	public CommunicateResponse() {
	}

	@Override
	public boolean isSuccess() {
		return this.result!=null;
	}

	@Override
	public T getResult() {
		return this.result;
	}

	@Override
	public Throwable getError() {
		return this.error;
	}

}