package com.kamesuta.mc.signpic.http;

public interface ICommunicateCallback<T> {
	void onDone(ICommunicateResponse<T> iCommunicateResponse);
}
