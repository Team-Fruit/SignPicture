package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.state.StateType;

import net.minecraft.client.renderer.Tessellator;

public abstract class Image implements IInitable, IAsyncProcessable, IDivisionProcessable, ICollectable {
	protected static final ImageSize DefaultSize = new ImageSize().defaultSize();
	protected final Content content;

	public Image(final Content content) {
		this.content = content;
	}

	public abstract ImageTexture getTexture() throws IllegalStateException;

	public abstract String getLocal();

	public ImageSize getSize() {
		if (this.content.state.getType()==StateType.AVAILABLE)
			return getTexture().getSize();
		else
			return DefaultSize;
	}

	public void draw(final float u, final float v, final float w, final float h, final float c, final float s, final boolean r, final boolean m) {
		if (this.content.state.getType()==StateType.AVAILABLE) {
			final Tessellator t = Tessellator.instance;
			RenderHelper.startTexture();
			final ImageTexture image = getTexture();
			image.bind();

			final int wraps = glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S);
			final int wrapt = glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T);
			final int mag = glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER);
			final int min = glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER);
			if (r) {
				glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			} else {
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			}
			if (image.hasMipmap())
				if (m&&DynamicImageTexture.openGl30()&&Config.instance.renderUseMipmap) {
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, Config.instance.renderMipmapTypeNearest ? GL_NEAREST : GL_LINEAR);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, Config.instance.renderMipmapTypeNearest ? GL_NEAREST_MIPMAP_LINEAR : GL_LINEAR_MIPMAP_LINEAR);
				} else {
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				}
			t.startDrawingQuads();
			t.addVertexWithUV(0, 0, 0, u, v);
			t.addVertexWithUV(0, 1, 0, u, v+h/s);
			t.addVertexWithUV(1, 1, 0, u+w/c, v+h/s);
			t.addVertexWithUV(1, 0, 0, u+w/c, v);
			t.draw();
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wraps);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapt);
			if (image.hasMipmap()) {
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min);
			}
		}
	}
}