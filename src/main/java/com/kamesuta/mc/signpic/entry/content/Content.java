package com.kamesuta.mc.signpic.entry.content;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.RemoteImage;
import com.kamesuta.mc.signpic.image.ResourceImage;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.state.StateType;

public class Content implements IInitable, ICollectable {
	public final ContentId id;
	public final ContentMeta meta;
	public final State state;
	public Image image;
	public String imagemeta;
	private boolean dirty;

	public Content(final ContentId id) {
		this.id = id;
		final String url = id.getURI();
		final String hash = ContentLocation.hash(url);
		this.meta = new ContentMeta(ContentLocation.metaLocation(hash));
		this.meta.setURL(url);
		this.meta.setMetaID(hash);
		if (StringUtils.isEmpty(this.meta.getCacheID()))
			this.meta.setCacheID(hash);
		this.state = new State().setName(id.id());
		if (id.isResource())
			this.image = new ResourceImage(this);
		else
			this.image = new RemoteImage(this);
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

	public boolean shouldCollect() {
		return this.dirty;
	}

	public void markDirty() {
		this.dirty = true;
	}

	public void markDirtyWithCache() {
		this.meta.setTryCount(0);
		this.meta.resetCache();
		markDirty();
	}
}
