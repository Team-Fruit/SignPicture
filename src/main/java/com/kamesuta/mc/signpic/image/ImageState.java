package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.render.StateRender.Color;
import com.kamesuta.mc.signpic.render.StateRender.Speed;

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
	},
	FAILED("signpic.state.failed", Color.DEFAULT, Speed.WAIT) {
		@Override
		public void themeImage(final ImageManager manager, final Image image) {
			glPushMatrix();
			glTranslatef(-.5f, -.5f, 0f);
			manager.get(CustomTileEntitySignRenderer.resWarning).draw();;
			glPopMatrix();
		}
	},
	ERROR("signpic.state.error", Color.DEFAULT, Speed.WAIT) {
		@Override
		public void themeImage(final ImageManager manager, final Image image) {
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
		glDisable(GL_TEXTURE_2D);

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

		glEnable(GL_TEXTURE_2D);
	}
}
