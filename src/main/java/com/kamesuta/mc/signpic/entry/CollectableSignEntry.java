package com.kamesuta.mc.signpic.entry;

class CollectableSignEntry<T extends ICollectableEntry> implements ICollectableEntry {
	public static int CollectTimes = 20 * 15;
	public static long times = 0;

	private final T entry;
	private long time = 0;

	public CollectableSignEntry(final T entry) {
		this.entry = entry;
		used();
	}

	public T get() {
		used();
		return this.entry;
	}

	public CollectableSignEntry<T> used() {
		this.time = times;
		return this;
	}

	public boolean shouldCollect() {
		return times - this.time > CollectTimes;
	}

	@Override
	public void onCollect() {
		this.entry.onCollect();
	}

	public static void Tick() {
		times++;
	}
}