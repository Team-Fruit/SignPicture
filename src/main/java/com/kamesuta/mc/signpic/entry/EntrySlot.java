package com.kamesuta.mc.signpic.entry;

import com.kamesuta.mc.signpic.Config;

public class EntrySlot<T> {
	protected static long times = 0;

	protected final T entry;
	private long time = 0;

	public EntrySlot(final T entry) {
		this.entry = entry;
		used();
	}

	public T get() {
		used();
		return this.entry;
	}

	public EntrySlot<T> used() {
		this.time = times;
		return this;
	}

	public boolean shouldCollect() {
		return times - this.time > getCollectTimes();
	}

	public static void Tick() {
		times++;
	}

	protected int getCollectTimes() {
		return Config.instance.entryGCtick;
	}
}
