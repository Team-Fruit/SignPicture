package com.kamesuta.mc.signpic.image;

import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.state.ContentStateType;

import net.minecraft.client.renderer.Tessellator;

public abstract class Image implements IInitable, IAsyncProcessable, IDivisionProcessable, ICollectable {
	protected static final ImageSize DefaultSize = new ImageSize().defaultSize();
	protected final Content content;

	public Image(final Content content) {
		this.content = content;
	}

	public abstract IImageTexture getTexture() throws IllegalStateException;

	public abstract String getLocal();

	public ImageSize getSize() {
		if (this.content.state.getType() == ContentStateType.AVAILABLE)
			return getTexture().getSize();
		else
			return DefaultSize;
	}

	public void draw() {
		if (this.content.state.getType() == ContentStateType.AVAILABLE) {
			final Tessellator t = Tessellator.instance;
			RenderHelper.startTexture();
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