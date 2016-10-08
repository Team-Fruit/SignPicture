package com.kamesuta.mc.signpic.entry.content;

import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.RemoteImage;
import com.kamesuta.mc.signpic.image.ResourceImage;
import com.kamesuta.mc.signpic.state.ContentState;
import com.kamesuta.mc.signpic.state.ContentStateType;

public class Content implements IInitable, ICollectable {
	public final ContentId id;
	public final ContentState state;
	public final ContentLocation location;
	public Image image;

	public Content(final ContentId id) {
		this.id = id;
		this.location = new ContentLocation(id);
		this.state = new ContentState();
		if (id.isResource())
			this.image = new ResourceImage(this);
		else
			this.image = new RemoteImage(this);
	}

	@Override
	public void onInit() {
		this.state.setType(ContentStateType.INITALIZED);
		this.image.onInit();
	}

	@Override
	public void onCollect() {
		this.image.onCollect();
	}
}
