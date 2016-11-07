package com.kamesuta.mc.signpic.http;

public abstract class Communicate implements ICommunicate {
	protected ICommunicateCallback callback;

	protected void onDone(final ICommunicateResponse response) {
		if (this.callback!=null)
			this.callback.onDone(response);
	}

	@Override
	public void setCallback(final ICommunicateCallback callback) {
		this.callback = callback;
	}
}
