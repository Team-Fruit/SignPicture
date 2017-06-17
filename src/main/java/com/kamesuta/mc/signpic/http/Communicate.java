package com.kamesuta.mc.signpic.http;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Communicate implements ICommunicate {
	protected @Nullable ICommunicateCallback callback;
	private @Nullable Thread currentThread;

	protected void onDone(final @Nonnull ICommunicateResponse response) {
		if (this.callback!=null)
			this.callback.onDone(response);
	}

	@Override
	public void setCallback(final @Nullable ICommunicateCallback callback) {
		this.callback = callback;
	}

	protected void setCurrent() {
		this.currentThread = Thread.currentThread();
	}

	protected void unsetCurrent() {
		this.currentThread = null;
	}

	@Override
	public void cancel() {
		if (this.currentThread!=null)
			this.currentThread.interrupt();
	}
}
