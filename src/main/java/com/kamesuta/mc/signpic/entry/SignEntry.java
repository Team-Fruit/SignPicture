package com.kamesuta.mc.signpic.entry;

import net.minecraft.client.resources.I18n;

public class SignEntry implements IInitableEntry, ICollectableEntry, ILoadEntry {
	public final EntryId id;
	public EntryState state = EntryState.INIT;

	public SignEntry(final EntryId id) {
		this.id = id;
	}

	@Override
	public void onInit() {
		this.state = EntryState.INITALIZED;
	}

	@Override
	public void onProcess() {

	}

	@Override
	public void onCollect() {}

	@Override
	public EntryState getState() {
		return this.state;
	}

	@Override
	public float getProgress() {
		return 0;
	}

	@Override
	public String getStateMessage() {
		return I18n.format(this.state.msg, (int) (getProgress()*100));
	}
}
