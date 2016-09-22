package com.kamesuta.mc.signpic.entry.content;

import com.kamesuta.mc.signpic.entry.EntrySlot;
import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IInitable;

public class ContentSlot<T extends IInitable & ICollectable> extends EntrySlot<T> implements IInitable, ICollectable {
	public static int CollectTimes = 20 * 15;
	public static long times = 0;

	private boolean init = true;

	public ContentSlot(final T entry) {
		super(entry);
	}

	@Override
	public void onInit() {
		this.init = false;
		this.entry.onInit();
	}

	public boolean shouldInit() {
		return this.init;
	}

	@Override
	public void onCollect() {
		this.entry.onCollect();
	}
}