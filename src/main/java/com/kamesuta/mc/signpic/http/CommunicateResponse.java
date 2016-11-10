package com.kamesuta.mc.signpic.http;

public class CommunicateResponse<T> implements ICommunicateResponse<T> {
	protected boolean isSuccess;
	protected T result;
	protected Throwable error;

	public CommunicateResponse(final T result) {
		setSuccess(true);
		setResult(result);
	}

	public CommunicateResponse(final Throwable error) {
		setError(error);
	}

	public CommunicateResponse(final boolean isSuccess) {
		setSuccess(isSuccess);
	}

	public CommunicateResponse() {
	}

	public CommunicateResponse<T> setSuccess(final boolean isSuccess) {
		this.isSuccess = isSuccess;
		return this;
	}

	public CommunicateResponse<T> setResult(final T result) {
		this.result = result;
		return this;
	}

	public CommunicateResponse<T> setError(final Throwable error) {
		this.error = error;
		return this;
	}

	@Override
	public boolean isSuccess() {
		return this.isSuccess;
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