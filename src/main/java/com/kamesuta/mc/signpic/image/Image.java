package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.renderer.Tessellator;

public abstract class Image {
	protected String id;
	protected ImageTextures texture;
	protected ImageState state = ImageState.INIT;
	protected ImageLocation location;
	protected String advmsg;

	public Image(final String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public ImageTextures getTexture() {
		if (this.state == ImageState.AVAILABLE)
			return this.texture;
		else
			throw new IllegalStateException("Not Available");
	}

	public ImageState getState() {
		return this.state;
	}

	public abstract float getProgress();

	public abstract String getStatusMessage();

	public abstract String getLocal();

	public abstract String advMessage();

	public abstract void process();

	public boolean processTexture() {
		return true;
	}

	public void draw() {
		if (this.state == ImageState.AVAILABLE) {
			final Tessellator t = Tessellator.instance;
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.texture.get().bind();
			t.startDrawingQuads();
			t.addVertexWithUV(0, 0, 0, 0, 0);
			t.addVertexWithUV(0, 1, 0, 0, 1);
			t.addVertexWithUV(1, 1, 0, 1, 1);
			t.addVertexWithUV(1, 0, 0, 1, 0);
			t.draw();
		}
	}
}