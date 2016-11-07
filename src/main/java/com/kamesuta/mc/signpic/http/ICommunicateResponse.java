package com.kamesuta.mc.signpic.http;

public interface ICommunicateResponse {
	boolean isSuccess();

	Throwable getError();
}
