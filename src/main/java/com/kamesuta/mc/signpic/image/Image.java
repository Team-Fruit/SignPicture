package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.entry.EntryPath;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

public abstract class Image {
	protected static final ImageSize DefaultSize = new ImageSize().defaultSize();
	protected final EntryPath path;
	protected ImageState state = ImageState.INIT;

	public Image(final EntryPath path) {
		this.path = path;
	}

	public EntryPath getPath() {
		return this.path;
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
			return DefaultSize;
	}

	public void draw() {
		if (this.state == ImageState.AVAILABLE) {
			final Tessellator t = Tessellator.instance;
			RenderHelper.startTexture();
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