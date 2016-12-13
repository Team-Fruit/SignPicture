package com.kamesuta.mc.signpic.http;

public abstract class Communicate implements ICommunicate {
	protected ICommunicateCallback callback;
	private Thread currentThread;

	protected void onDone(final ICommunicateResponse response) {
		if (this.callback!=null)
			this.callback.onDone(response);
	}

	@Override
	public void setCallback(final ICommunicateCallback callback) {
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
