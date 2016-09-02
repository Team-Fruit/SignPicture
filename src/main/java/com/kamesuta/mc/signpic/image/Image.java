package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

public abstract class Image {
	protected final String id;
	protected ImageState state = ImageState.INIT;

	public Image(final String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public abstract IImageTexture getTexture() throws IllegalStateException;

	public ImageState getState() {
		return this.state;
	}

	public abstract float getProgress();

	public String getStatusMessage() {
		return I18n.format(this.state.msg, (int) (getProgress()*100));
	}

	public abstract String getLocal();

	public String advMessage() {
		return null;
	};

	public void onImageUsed() {}

	public boolean shouldCollect() {
		return false;
	}

	public void delete() {}

	public abstract void process();

	public boolean processTexture() {
		return true;
	}

	public ImageSize getSize() {
		if (this.state == ImageState.AVAILABLE)
			return getTexture().getSize();
		else
			return ImageSize.DefaultSize;
	}

	public void draw() {
		if (this.state == ImageState.AVAILABLE) {
			final Tessellator t = Tessellator.instance;
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			getTexture().bind();
			t.startDrawingQuads();
			t.addVertexWithUV(0, 0, 0, 0, 0);
			t.addVertexWithUV(0, 1, 0, 0, 1);
			t.addVertexWithUV(1, 1, 0, 1, 1);
			t.addVertexWithUV(1, 0, 0, 1, 0);
			t.draw();
		}
	}
}