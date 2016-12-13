package com.kamesuta.mc.signpic.http;

import com.kamesuta.mc.signpic.ILoadCancelable;

public interface ICommunicate extends ILoadCancelable {
	void communicate();

	void setCallback(ICommunicateCallback callback);
}
