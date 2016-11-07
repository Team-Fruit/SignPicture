package com.kamesuta.mc.signpic.http;

public class CommunicateResponse implements ICommunicateResponse {
	protected boolean isSuccess;
	protected Throwable error;

	public CommunicateResponse(final boolean isSuccess, final Throwable error) {
		this.isSuccess = isSuccess;
		this.error = error;
	}

	@Override
	public boolean isSuccess() {
		return this.isSuccess;
	}

	@Override
	public Throwable getError() {
		return this.error;
	}
}