package com.kamesuta.mc.signpic.entry;

public interface ILoadEntry {
	void onProcess();

	float getProgress();

	EntryState getState();

	String getStateMessage();
}
