package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.render.StateRender.Color;
import com.kamesuta.mc.signpic.render.StateRender.Speed;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;

public enum ImageState {
	INIT("signpic.state.init", Color.DEFAULT, Speed.WAIT),
	INITALIZED("signpic.state.initalized", Color.DEFAULT, Speed.WAIT),
	DOWNLOADING("signpic.state.downloading", Color.DOWNLOAD, Speed.RUN),
	DOWNLOADED("signpic.state.downloaded", Color.DOWNLOAD, Speed.WAIT),
	IOLOADING("signpic.state.ioloading", Color.IOLOAD, Speed.RUN),
	IOLOADED("signpic.state.ioloaded", Color.IOLOAD, Speed.WAIT),
	TEXTURELOADING("signpic.state.textureloading", Color.TEXTURELOAD, Speed.RUN),
	TEXTURELOADED("signpic.state.textureloaded", Color.TEXTURELOAD, Speed.WAIT),
	AVAILABLE("signpic.state.available", Color.DEFAULT, Speed.RUN) {
		@Override
		public void themeImage(final ImageManager manager, final Image image) {}

		@Override
		public void mainImage(final ImageManager manager, final Image image) {
			image.draw();
		}

		@Override
		public void message(final ImageManager manager, final Image image, final FontRenderer fontrenderer) {
		}
	},
	FAILED("signpic.state.failed", Color.DEFAULT, Speed.WAIT) {
		@Override
		public void themeImage(final ImageManager manager, final Image image) {
			RenderHelper.startTexture();
			glPushMatrix();
			glTranslatef(-.5f, -.5f, 0f);
			manager.get(CustomTileEntitySignRenderer.resWarning).draw();;
			glPopMatrix();
		}
	},
	ERROR("signpic.state.error", Color.DEFAULT, Speed.WAIT) {
		@Override
		public void themeImage(final ImageManager manager, final Image image) {
			RenderHelper.startTexture();
			glPushMatrix();
			glTranslatef(-.5f, -.5f, 0f);
			manager.get(CustomTileEntitySignRenderer.resError).draw();;
			glPopMatrix();
		}
	},
	;

	public final String msg;
	protected final Color color;
	protected final Speed speed;
	ImageState(final String s, final Color color, final Speed speed) {
		this.msg = s;
		this.color = color;
		this.speed = speed;
	}

	public void themeImage(final ImageManager manager, final Image image) {
		glLineWidth(3f);
		RenderHelper.startShape();

		glPushMatrix();
		glScalef(.5f, .5f, 1f);

		// Loading Circle
		this.color.loadingColor();
		RenderHelper.drawLoadingCircle(this.speed.inner, this.speed.outer);

		// Design Circle
		this.color.designColor();
		RenderHelper.drawDesignCircle();

		// Progress Circle
		this.color.progressColor();
		final float progress = image.getProgress();
		RenderHelper.drawProgressCircle(progress);

		glPopMatrix();
	}

	public void message(final ImageManager manager, final Image image, final FontRenderer fontrenderer) {
		RenderHelper.startTexture();
		final float f1 = 0.6666667F;
		float f3 = 0.06666668F * f1;
		glTranslatef(0f, 1f, 0f);
		glPushMatrix();
		glScalef(f3, f3, 1f);
		final String msg1 = image.getStatusMessage();
		fontrenderer.drawStringWithShadow(msg1, -fontrenderer.getStringWidth(msg1) / 2, -fontrenderer.FONT_HEIGHT, 0xffffff);
		glPopMatrix();
		f3 = 0.036666668F * f1;
		glPushMatrix();
		glScalef(f3, f3, 1f);
		final String msg2 = image.getPath().path();
		fontrenderer.drawStringWithShadow(msg2, -fontrenderer.getStringWidth(msg2) / 2, 0, 0xffffff);
		glPopMatrix();
		final String msg3 = image.advMessage();
		if (msg3 != null) {
			glPushMatrix();
			glScalef(f3, f3, 1f);
			fontrenderer.drawStringWithShadow(msg3, -fontrenderer.getStringWidth(msg3) / 2, fontrenderer.FONT_HEIGHT, 0xffffff);
			glPopMatrix();
		}
	}

	public void mainImage(final ImageManager manager, final Image image) {
		final Tessellator t = Tessellator.instance;
		RenderHelper.startShape();
		glLineWidth(1f);
		glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
		t.startDrawing(GL_LINE_LOOP);
		t.addVertex(0, 0, 0);
		t.addVertex(0, 1, 0);
		t.addVertex(1, 1, 0);
		t.addVertex(1, 0, 0);
		t.draw();
	}
}
