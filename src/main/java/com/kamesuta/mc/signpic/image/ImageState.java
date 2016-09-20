package com.kamesuta.mc.signpic.image;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.render.StateRender.LoadingCircle;
import com.kamesuta.mc.signpic.render.StateRender.LoadingCircleType;

import net.minecraft.client.gui.FontRenderer;

public enum ImageState {
	INIT("signpic.state.init", LoadingCircle.DEFAULT, LoadingCircleType.WAIT),
	INITALIZED("signpic.state.initalized", LoadingCircle.DEFAULT, LoadingCircleType.WAIT),
	DOWNLOADING("signpic.state.downloading", LoadingCircle.DOWNLOAD, LoadingCircleType.RUN),
	DOWNLOADED("signpic.state.downloaded", LoadingCircle.DOWNLOAD, LoadingCircleType.WAIT),
	IOLOADING("signpic.state.ioloading", LoadingCircle.CONTENTLOAD, LoadingCircleType.RUN),
	IOLOADED("signpic.state.ioloaded", LoadingCircle.CONTENTLOAD, LoadingCircleType.WAIT),
	//	TEXTURELOADING("signpic.state.textureloading", LoadingCircle.TEXTURELOAD, LoadingCircleType.RUN),
	//	TEXTURELOADED("signpic.state.textureloaded", LoadingCircle.TEXTURELOAD, LoadingCircleType.WAIT),
	AVAILABLE("signpic.state.available", LoadingCircle.DEFAULT, LoadingCircleType.RUN) {
		@Override
		public void themeImage(final ContentManager manager, final Content content) {}

		@Override
		public void message(final ContentManager manager, final Content content, final FontRenderer fontrenderer) {
		}
	},
	FAILED("signpic.state.failed", LoadingCircle.DEFAULT, LoadingCircleType.WAIT) {
		@Override
		public void themeImage(final ContentManager manager, final Content content) {
			RenderHelper.startTexture();
			glPushMatrix();
			glTranslatef(-.5f, -.5f, 0f);
			manager.get(ContentId.fromResource(CustomTileEntitySignRenderer.resWarning)).image.draw();
			glPopMatrix();
		}
	},
	ERROR("signpic.state.error", LoadingCircle.DEFAULT, LoadingCircleType.WAIT) {
		@Override
		public void themeImage(final ContentManager manager, final Content content) {
			RenderHelper.startTexture();
			glPushMatrix();
			glTranslatef(-.5f, -.5f, 0f);
			manager.get(ContentId.fromResource(CustomTileEntitySignRenderer.resError)).image.draw();
			glPopMatrix();
		}
	},
	;

	public final String msg;
	protected final LoadingCircle color;
	protected final LoadingCircleType speed;
	ImageState(final String s, final LoadingCircle color, final LoadingCircleType speed) {
		this.msg = s;
		this.color = color;
		this.speed = speed;
	}

	public void themeImage(final ContentManager manager, final Content content) {
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
		final float progress = content.state.progress.getProgress();
		RenderHelper.drawProgressCircle(progress);

		glPopMatrix();
	}

	public void message(final ContentManager manager, final Content content, final FontRenderer fontrenderer) {
		RenderHelper.startTexture();
		final float f1 = 0.6666667F;
		float f3 = 0.06666668F * f1;
		glTranslatef(0f, 1f, 0f);
		glPushMatrix();
		glScalef(f3, f3, 1f);
		final String msg1 = content.state.getStateMessage();
		fontrenderer.drawStringWithShadow(msg1, -fontrenderer.getStringWidth(msg1) / 2, -fontrenderer.FONT_HEIGHT, 0xffffff);
		glPopMatrix();
		f3 = 0.036666668F * f1;
		glPushMatrix();
		glScalef(f3, f3, 1f);
		final String msg2 = content.id.path();
		fontrenderer.drawStringWithShadow(msg2, -fontrenderer.getStringWidth(msg2) / 2, 0, 0xffffff);
		glPopMatrix();
		final String msg3 = content.state.getMessage();
		if (msg3 != null) {
			glPushMatrix();
			glScalef(f3, f3, 1f);
			fontrenderer.drawStringWithShadow(msg3, -fontrenderer.getStringWidth(msg3) / 2, fontrenderer.FONT_HEIGHT, 0xffffff);
			glPopMatrix();
		}
	}
}
