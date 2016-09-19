package com.kamesuta.mc.signpic.entry.content;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectableEntry;
import com.kamesuta.mc.signpic.entry.IInitableEntry;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.RemoteImage;
import com.kamesuta.mc.signpic.image.ResourceImage;

public class Content implements IInitableEntry, ICollectableEntry, IAsyncProcessable {
	public static final ContentLocation location = new ContentLocation(Client.signpicCacheDir);

	public final ContentId id;
	public ContentState state;
	public Image image;


	public Content(final ContentId id) {
		this.id = id;
		this.state = new ContentState();
		if (id.isResource())
			this.image = new ResourceImage(id);
		else
			this.image = new RemoteImage(location, id, this.state);
	}

	@Override
	public void onInit() {
		this.state.setType(ContentStateType.INITALIZED);
	}

	@Override
	public void onAsyncProcess() {
		new ContentDownloader(location, this.id, this.state).onAsyncProcess();
	}

	@Override
	public void onCollect() {}
}
