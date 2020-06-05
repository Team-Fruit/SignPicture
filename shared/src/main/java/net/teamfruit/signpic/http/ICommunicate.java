package net.teamfruit.signpic.http;

import javax.annotation.Nonnull;

import net.teamfruit.signpic.ILoadCancelable;

public interface ICommunicate extends ILoadCancelable {
	void communicate();

	void setCallback(@Nonnull ICommunicateCallback callback);
}
