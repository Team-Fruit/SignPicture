package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;

public abstract class Image {
	protected static final ImageSize DefaultSize = new ImageSize().defaultSize();
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
			return DefaultSize;
	}

	public void draw() {
		if (this.state == ImageState.AVAILABLE) {
			final VertexBuffer t = RenderHelper.w;
			RenderHelper.startTexture();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			getTexture().bind();
			t.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			t.pos(0, 0, 0).tex(0, 0).endVertex();
			t.pos(0, 1, 0).tex(0, 1).endVertex();
			t.pos(1, 1, 0).tex(1, 1).endVertex();
			t.pos(1, 0, 0).tex(1, 0).endVertex();
			RenderHelper.t.draw();
		}
	}
}