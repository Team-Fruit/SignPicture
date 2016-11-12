package com.kamesuta.mc.signpic.entry.content;

import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.gui.GuiImage;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.RemoteImage;
import com.kamesuta.mc.signpic.image.ResourceImage;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.state.StateType;

public class Content implements IInitable, ICollectable {
	public final ContentId id;
	public final State state;
	public final ContentLocation location;
	public Image image;
	public final GuiImage gui;

	public Content(final ContentId id) {
		this.id = id;
		this.location = new ContentLocation(id);
		this.state = new State(id.id());
		if (id.isResource())
			this.image = new ResourceImage(this);
		else
			this.image = new RemoteImage(this);
		this.gui = new GuiImage(this);
	}

	@Override
	public void onInit() {
		this.state.setType(StateType.INITALIZED);
		this.image.onInit();
	}

	@Override
	public void onCollect() {
		this.image.onCollect();
	}
}
