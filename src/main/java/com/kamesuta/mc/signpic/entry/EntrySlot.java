package com.kamesuta.mc.signpic.entry;

class EntrySlot<T extends IInitableEntry & ICollectableEntry & ILoadEntry> implements IInitableEntry, ICollectableEntry, ILoadEntry {
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

	@Override
	public float getProgress() {
		return this.entry.getProgress();
	}

	@Override
	public EntryState getState() {
		return this.entry.getState();
	}

	@Override
	public String getStateMessage() {
		return this.entry.getStateMessage();
	}

	public static void Tick() {
		times++;
	}
}