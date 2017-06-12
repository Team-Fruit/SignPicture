package com.kamesuta.mc.signpic.http;

import javax.annotation.Nullable;

public class CommunicateResponse implements ICommunicateResponse {
	protected boolean isSuccess;
	protected @Nullable Throwable error;

	public CommunicateResponse(final boolean isSuccess, final @Nullable Throwable error) {
		this.isSuccess = isSuccess;
		this.error = error;
	}

	@Override
	public boolean isSuccess() {
		return this.isSuccess;
	}

	@Override
	public @Nullable Throwable getError() {
		return this.error;
	}
}