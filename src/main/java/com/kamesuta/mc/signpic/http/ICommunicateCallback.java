package com.kamesuta.mc.signpic.http;

import javax.annotation.Nonnull;

public interface ICommunicateCallback {
	void onDone(@Nonnull ICommunicateResponse response);
}
