package com.kamesuta.mc.signpic.http;

public interface ICommunicateResponse<T> {
	boolean isSuccess();

	Throwable getError();

	T getResult();
}
