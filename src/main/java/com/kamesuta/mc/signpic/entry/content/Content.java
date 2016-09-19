package com.kamesuta.mc.signpic.entry.content;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.EntryState;
import com.kamesuta.mc.signpic.entry.EntryStateType;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectableEntry;
import com.kamesuta.mc.signpic.entry.IInitableEntry;
import com.kamesuta.mc.signpic.image.Image;

public class Content implements IInitableEntry, ICollectableEntry, IAsyncProcessable {
	public static final ContentLocation location = new ContentLocation(Client.signpicCacheDir);

	public final ContentId id;
	public EntryState state;
	public Image image;

	public Content(final ContentId id) {
		this.id = id;
		this.state = new EntryState();
		//this.image = Client.manager.get(id);
	}

	@Override
	public void onInit() {
		this.state.setType(EntryStateType.INITALIZED);
	}

	@Override
	public void onProcess() {
		new ContentDownloader(location, this.id, this.state).onProcess();
	}

	@Override
	public void onCollect() {}

	public void a(final ContentId id) {
		//		if (id.isResource())
		//			content = new ResourceImage(path);
		//		else
		//			content = new RemoteImage(path, this.location);
		//		this.registry.put(path, content);
	}
}
