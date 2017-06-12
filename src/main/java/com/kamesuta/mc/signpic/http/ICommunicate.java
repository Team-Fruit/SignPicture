package com.kamesuta.mc.signpic.http;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.ILoadCancelable;

public interface ICommunicate extends ILoadCancelable {
	void communicate();

	void setCallback(@Nonnull ICommunicateCallback callback);
}
