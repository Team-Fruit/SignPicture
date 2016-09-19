package com.kamesuta.mc.signpic.entry;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.util.Sign;

public class SignEntry implements IInitableEntry, ICollectableEntry, ILoadEntry {
	public static final EntryLocation location = new EntryLocation(Client.signpicCacheDir);
	public final EntryId id;
	public final Sign sign;
	public EntryState state;
	public Image image;

	public SignEntry(final EntryId id) {
		this.id = id;
		this.sign = new Sign().parseText(id.id());
		this.state = new EntryState();
		this.image = Client.manager.get(this.sign.getID());
	}

	@Override
	public void onInit() {
		this.state.setType(EntryStateType.INITALIZED);
	}

	@Override
	public void onProcess() {
		new EntryDownloader(location, this.id, this.state).onProcess();
	}

	@Override
	public void onCollect() {}
}
