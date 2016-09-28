package com.kamesuta.mc.signpic.entry;

public class EntrySlot<T> {
	public static final int CollectTimes = 20 * 15;
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
		return times - this.time > CollectTimes;
	}

	public static void Tick() {
		times++;
	}
}
