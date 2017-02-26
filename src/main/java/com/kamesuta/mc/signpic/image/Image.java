package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.render.WRenderer.BlendType;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.ILoadCancelable;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.entry.IAsyncProcessable;
import com.kamesuta.mc.signpic.entry.ICollectable;
import com.kamesuta.mc.signpic.entry.IDivisionProcessable;
import com.kamesuta.mc.signpic.entry.IInitable;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.state.StateType;

public abstract class Image implements IInitable, IAsyncProcessable, IDivisionProcessable, ICollectable, ILoadCancelable {
	protected final @Nonnull Content content;

	public Image(final @Nonnull Content content) {
		this.content = content;
	}

	public abstract @Nonnull ImageTexture getTexture() throws IllegalStateException;

	public abstract @Nonnull String getLocal();

	public @Nonnull SizeData getSize() {
		if (this.content.state.getType()==StateType.AVAILABLE)
			return getTexture().getSize();
		else
			return SizeData.DefaultSize;
	}

	public void draw(final float u, final float v, final float w, final float h, final float c, final float s, final @Nullable BlendType b, final @Nullable BlendType d, final boolean r, final boolean m, final boolean l) {
		if (this.content.state.getType()==StateType.AVAILABLE) {
			WRenderer.startTexture(b, d);
			if (l)
				OpenGL.glEnable(GL_LIGHTING);
			final ImageTexture image = getTexture();
			image.bind();

			final int wraps = OpenGL.glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S);
			final int wrapt = OpenGL.glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T);
			final int mag = OpenGL.glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER);
			final int min = OpenGL.glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER);
			if (r) {
				OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			} else {
				OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
				OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			}
			if (image.hasMipmap())
				if (m&&OpenGL.openGl30()&&Config.getConfig().renderUseMipmap.get()) {
					OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, Config.getConfig().renderMipmapTypeNearest.get() ? GL_NEAREST : GL_LINEAR);
					OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, Config.getConfig().renderMipmapTypeNearest.get() ? GL_NEAREST_MIPMAP_LINEAR : GL_LINEAR_MIPMAP_LINEAR);
				} else {
					OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
					OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				}
			WRenderer.beginTextureQuads()
					.pos(0, 0, 0).tex(u, v)
					.pos(0, 1, 0).tex(u, v+h/s)
					.pos(1, 1, 0).tex(u+w/c, v+h/s)
					.pos(1, 0, 0).tex(u+w/c, v)
					.draw();
			OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wraps);
			OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapt);
			if (image.hasMipmap()) {
				OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag);
				OpenGL.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min);
			}
			WRenderer.startTexture();
		}
	}

	public void draw(final float u, final float v, final float w, final float h, final float c, final float s, final @Nullable BlendType b, final @Nullable BlendType d, final boolean r, final boolean m, final boolean l, final float x1, final float y1, final float x2, final float y2) {
		draw(u-x1, v-y1, w-x1-(1f-x2), h-y1-(1f-y2), c, s, b, d, r, m, l);
	}

	public void draw(@Nonnull final CompoundAttr meta, final float x1, final float y1, final float x2, final float y2) {
		draw(
				meta.u.getMovie().get().data,
				meta.v.getMovie().get().data,
				meta.w.getMovie().get().data,
				meta.h.getMovie().get().data,
				meta.c.getMovie().get().data,
				meta.s.getMovie().get().data,
				meta.b.getMovie().get().data,
				meta.d.getMovie().get().data,
				meta.r.getMovie().get().data,
				meta.m.getMovie().get().data,
				meta.l.getMovie().get().data,
				x1, y1, x2, y2);
	}

	public void draw(@Nonnull final CompoundAttr meta) {
		draw(meta, 0f, 0f, 1f, 1f);
	}
}