package com.kamesuta.mc.signpic.http;

import javax.annotation.Nullable;

public interface ICommunicateResponse {
	boolean isSuccess();

	@Nullable
	Throwable getError();
}
