package com.kamesuta.mc.signpic.http;

public interface ICommunicate {
	void communicate();

	void setCallback(ICommunicateCallback callback);

	void cancel();
}
