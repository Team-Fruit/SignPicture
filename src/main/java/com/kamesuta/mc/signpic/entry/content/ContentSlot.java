package com.kamesuta.mc.signpic.entry.content;

import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.EntrySlot;
import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IInitable;

public class ContentSlot<T extends IInitable & ICollectable> extends EntrySlot<T> implements IInitable, ICollectable {
	public ContentSlot(final T entry) {
		super(entry);
	}

	@Override
	public void onInit() {
		this.entry.onInit();
	}

	@Override
	public void onCollect() {
		this.entry.onCollect();
	}

	@Override
	protected int getCollectTimes() {
		return Config.instance.contentGCtick;
	}
}