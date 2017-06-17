package com.kamesuta.mc.signpic.entry;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.Config;

public class EntrySlot<T> {
	protected static long times = 0;

	protected final @Nonnull T entry;
	private long time = 0;

	public EntrySlot(final @Nonnull T entry) {
		this.entry = entry;
		used();
	}

	public @Nonnull T get() {
		used();
		return this.entry;
	}

	public @Nonnull EntrySlot<T> used() {
		this.time = times;
		return this;
	}

	public boolean shouldCollect() {
		return times-this.time>getCollectTimes();
	}

	public static void Tick() {
		times++;
	}

	protected int getCollectTimes() {
		return Config.getConfig().entryGCtick.get();
	}
}
