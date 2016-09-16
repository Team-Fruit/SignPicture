package com.kamesuta.mc.signpic.entry;

public class SignEntry implements IInitableEntry, ICollectableEntry, ILoadEntry {
	public final String id;
	public EntryState state = EntryState.INIT;

	public SignEntry(final String id) {
		this.id = id;
	}

	@Override
	public void onInit() {
		// TODO 自動生成されたメソッド・スタブ
	
	}

	@Override
	public void onProcess() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onCollect() {
		// TODO 自動生成されたメソッド・スタブ
	
	}

	public EntryState getState() {
		return this.state;
	}
}
