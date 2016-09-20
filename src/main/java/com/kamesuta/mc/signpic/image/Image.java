package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectableEntry;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentLocation;
import com.kamesuta.mc.signpic.entry.content.ContentState;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.render.RenderHelper;

import net.minecraft.client.renderer.Tessellator;

public abstract class Image implements IAsyncProcessable, IDivisionProcessable, ICollectableEntry {
	protected static final ImageSize DefaultSize = new ImageSize().defaultSize();
	protected final ContentLocation location;
	protected final ContentId id;
	protected final ContentState state;
	protected boolean isAvailable;

	public Image(final ContentLocation location, final ContentId id, final ContentState state) {
		this.location = location;
		this.id = id;
		this.state = state;
	}

	public ContentId getPath() {
		return this.id;
	}

	public abstract IImageTexture getTexture() throws IllegalStateException;

	public abstract String getLocal();

	public ImageSize getSize() {
		if (this.isAvailable)
			return getTexture().getSize();
		else
			return DefaultSize;
	}

	public void draw() {
		if (this.isAvailable) {
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