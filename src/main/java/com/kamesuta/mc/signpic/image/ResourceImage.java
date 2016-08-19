package com.kamesuta.mc.signpic.image;

import com.kamesuta.mc.signpic.Reference;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ResourceImage extends Image {
	protected ImageLoader loading;
	protected Thread loadingprocess;
	protected ResourceLocation location;

	public ResourceImage(final ResourceLocation location) {
		super(location.toString());
		this.location = location;
	}

	public void init() {
		this.state = ImageState.INITALIZED;
	}

	public void ioload() {
		this.state = ImageState.IOLOADING;
		try {
			final IResourceManager manager = FMLClientHandler.instance().getClient().getResourceManager();
			if (this.loading == null)
				this.loading = new ImageLoader(this, manager, this.location);
			if (this.loadingprocess == null) {
				this.loadingprocess = new Thread(this.loading);
				this.loadingprocess.start();
			}
		} catch (final Exception e) {
			this.state = ImageState.ERROR;
			this.advmsg = I18n.format("signpic.advmsg.unknown", e);
			Reference.logger.error("UnknownError", e);
		}
	}

	public void complete() {
		this.state = ImageState.AVAILABLE;
	}

	@Override
	public void process() {
		switch(this.state) {
		case INIT:
			init();
			break;
		case INITALIZED:
			ioload();
			break;
		case IOLOADED:
			complete();
			break;
		default:
			break;
		}
	}

	@Override
	public float getProgress() {
		switch(this.state) {
		case AVAILABLE:
			return 1f;
		default:
			return 0;
		}
	}

	@Override
	public String getStatusMessage() {
		return I18n.format(this.state.msg, (int)getProgress()*100);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RemoteImage))
			return false;
		final Image other = (Image) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("ResourceImage[%s]", this.id);
	}

	@Override
	public ImageState getState() {
		return this.state;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getLocal() {
		return "Resource:"+this.location;
	}

	@Override
	public String advMessage() {
		return null;
	}
}
