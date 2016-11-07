package com.kamesuta.mc.signpic.http;

public interface CommunicateResult {
	boolean isSuccess();

	Throwable getError();
}
