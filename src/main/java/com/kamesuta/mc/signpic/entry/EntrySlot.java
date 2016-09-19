package com.kamesuta.mc.signpic.entry;

public class EntrySlot<T> {
	public static int CollectTimes = 20 * 15;
	public static long times = 0;

	private final T entry;
	private long time = 0;
	private boolean init = true;

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

	public EntrySlot<T> init() {
		this.init = false;
		return this;
	}

	public boolean shouldInit() {
		return this.init;
	}

	public static void Tick() {
		times++;
	}
}