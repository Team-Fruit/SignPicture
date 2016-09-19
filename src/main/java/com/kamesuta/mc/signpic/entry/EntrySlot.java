package com.kamesuta.mc.signpic.entry;

public class EntrySlot<T extends IInitableEntry & ICollectableEntry & IAsyncProcessable> implements IInitableEntry, ICollectableEntry, IAsyncProcessable {
	public static int CollectTimes = 20 * 15;
	public static long times = 0;

	private boolean init = true;
	private final T entry;
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

	public boolean shouldInit() {
		return this.init;
	}

	@Override
	public void onInit() {
		this.init = false;
		this.entry.onInit();
	}

	public boolean shouldCollect() {
		return times - this.time > CollectTimes;
	}

	@Override
	public void onCollect() {
		this.entry.onCollect();
	}

	@Override
	public void onProcess() {
		this.entry.onProcess();
	}

	public static void Tick() {
		times++;
	}
}