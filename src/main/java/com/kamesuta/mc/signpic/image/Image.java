package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class Image implements IInitable, IAsyncProcessable, IDivisionProcessable, ICollectable {
	protected static final ImageSize DefaultSize = new ImageSize().defaultSize();
	protected final Content content;

	public Image(final Content content) {
		this.content = content;
	}

	public abstract IImageTexture getTexture() throws IllegalStateException;

	public abstract String getLocal();

	public ImageSize getSize() {
		if (this.content.state.getType() == StateType.AVAILABLE)
			return getTexture().getSize();
		else
			return DefaultSize;
	}

	public void draw() {
		if (this.content.state.getType() == StateType.AVAILABLE) {
			final WorldRenderer t = RenderHelper.w;
			RenderHelper.startTexture();
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