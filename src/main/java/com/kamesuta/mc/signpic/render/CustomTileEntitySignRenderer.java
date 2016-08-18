package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.ImageState;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer
{
	protected ImageManager manager;
	protected final Tessellator t = Tessellator.instance;

	protected ResourceLocation resWarning;

	public CustomTileEntitySignRenderer(final ImageManager manager) {
		this.manager = manager;
		this.resWarning = new ResourceLocation(Reference.MODID, "textures/state/warning.png");
	}

	@Override
	public void renderTileEntityAt(final TileEntitySign tile, final double x, final double y, final double z, final float color)
	{
		final String s = StringUtils.join(tile.signText);
		if (s.endsWith("]") && s.contains("[")) {
			// Extract URL
			final int start = s.lastIndexOf("[");
			String url = s.substring(0, start);
			if (url.startsWith("$")) {
				url = "https://" + url.substring(1);
			} else if (url.startsWith("//")) {
				url = "http://" + url.substring(2);
			} else if (!(url.startsWith("http://") || url.startsWith("https://"))) {
				url = "http://" + url;
			}

			// Extract Size
			final String size = s.substring(start+1, s.length()-1);
			final String[] sp_size = size.split("x");
			float wid = 1;
			try {
				if (sp_size.length >= 1)
					wid = Float.parseFloat(sp_size[0]);
			} catch (final NumberFormatException e) {}
			float hei = 1;
			try {
				if (sp_size.length >= 2)
					hei = Float.parseFloat(sp_size[1]);
			} catch (final NumberFormatException e) {}

			// Load Image
			final Image image = this.manager.get(url);

			// Vanilla Translate
			final Block block = tile.getBlockType();
			glPushMatrix();
			final float f1 = 0.6666667F;
			float f3;

			if (block == Blocks.standing_sign) {
				glTranslatef((float)x + 0.5F, (float)y + 0.75F * f1, (float)z + 0.5F);
				final float f2 = tile.getBlockMetadata() * 360 / 16.0F;
				glRotatef(-f2, 0.0F, 1.0F, 0.0F);
			} else {
				final int j = tile.getBlockMetadata();
				f3 = 0.0F;

				if (j == 2) f3 = 180.0F;
				if (j == 4) f3 = 90.0F;
				if (j == 5) f3 = -90.0F;

				glTranslatef((float)x + 0.5F, (float)y + 0.75F * f1, (float)z + 0.5F);
				glRotatef(-f3, 0.0F, 1.0F, 0.0F);
				glTranslatef(0.0F, 0.0F, -0.4375F);
			}

			// Draw Canvas
			//glDisable(GL_CULL_FACE);
			glPushMatrix();
			glTranslatef(-wid/2, hei-.5f, 0f);
			glScalef(1f, -1f, -1f);
			glPushMatrix();
			glScalef(wid, hei, 1f);
			if (image.getState() == ImageState.AVAILABLE) {
				glBindTexture(GL_TEXTURE_2D, image.getTexture().getGlTextureId());
				this.t.startDrawingQuads();
				this.t.addVertexWithUV(0, 0, 0, 0, 0);
				this.t.addVertexWithUV(0, 1, 0, 0, 1);
				this.t.addVertexWithUV(1, 1, 0, 1, 1);
				this.t.addVertexWithUV(1, 0, 0, 1, 0);
				this.t.draw();
			} else {
				glDisable(GL_TEXTURE_2D);
				glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
				this.t.startDrawing(GL_LINE_LOOP);
				this.t.addVertex(0, 0, 0);
				this.t.addVertex(0, 1, 0);
				this.t.addVertex(1, 1, 0);
				this.t.addVertex(1, 0, 0);
				this.t.draw();
				glEnable(GL_TEXTURE_2D);
			}
			glPopMatrix();

			if (wid<1.5f || hei<1.5) {
				glScalef(.5f, .5f, .5f);
				glTranslatef(wid/2, -hei/2-.6f, 0);
			}
			// Draw Canvas - Draw Loading
			glPushMatrix();
			glTranslatef(wid/2, hei/2, 0);
			glScalef(.5f, .5f, 1f);
			if (image.getState() == ImageState.DOWNLOADING) {
				glDisable(GL_TEXTURE_2D);
				glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.t.startDrawing(GL_LINE_LOOP);
				addCircleVertex(0f, 1f, 1f);
				this.t.draw();

				final float progress = .7f;
				glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
				this.t.startDrawing(GL_POLYGON);
				this.t.addVertex(0f, 0f, 0f);
				addCircleVertex(progress, 0f, 1f);
				//addCircleVertex(0, progress);
				this.t.draw();

				final long time = System.currentTimeMillis();
				final float time1 = time % 893 / 893f;
				this.t.startDrawing(GL_LINE_LOOP);
				addCircleVertex(time1, time1+0.2f, 1.07f);
				addCircleVertex(time1+0.2f, time1, 1.09f);
				this.t.draw();
				final float time2 = time % 627 / 627f;
				this.t.startDrawing(GL_LINE_LOOP);
				addCircleVertex(time2, time2+0.1f, 1.03f);
				addCircleVertex(time2+0.1f, time2, 1.05f);
				this.t.draw();
				glEnable(GL_TEXTURE_2D);
			}

			if (image.getState() == ImageState.FAILED) {
				final Image warning = this.manager.get(this.resWarning);
				if (warning.getState() == ImageState.AVAILABLE) {
					glPushMatrix();
					glTranslatef(-.5f, -.5f, 0f);
					glBindTexture(GL_TEXTURE_2D, warning.getTexture().getGlTextureId());
					this.t.startDrawingQuads();
					this.t.addVertexWithUV(0, 0, 0, 0, 0);
					this.t.addVertexWithUV(0, 1, 0, 0, 1);
					this.t.addVertexWithUV(1, 1, 0, 1, 1);
					this.t.addVertexWithUV(1, 0, 0, 1, 0);
					this.t.draw();
					glPopMatrix();
				}
			}

			glDepthMask(false);
			final FontRenderer fontrenderer = func_147498_b();
			glPushMatrix();
			f3 = 0.06666668F * f1;
			glTranslatef(0f, -1.3f, 0f);
			glScalef(f3, f3, 1f);
			final String msg1 = image.getStatusMessage();
			fontrenderer.drawString(msg1, -fontrenderer.getStringWidth(msg1) / 2, -fontrenderer.FONT_HEIGHT, 0xffffff);
			glPopMatrix();
			glPushMatrix();
			f3 = 0.036666668F * f1;
			glTranslatef(0f, -1.3f, 0f);
			glScalef(f3, f3, 1f);
			final String msg2 = image.getId();
			fontrenderer.drawString(msg2, -fontrenderer.getStringWidth(msg2) / 2, 0, 0xffffff);
			glPopMatrix();
			glDepthMask(true);

			glPopMatrix();
			glPopMatrix();
			glEnable(GL_CULL_FACE);

			glPopMatrix();
		} else {
			super.renderTileEntityAt(tile, x, y, z, color);
		}
	}

	protected void addCircleVertex(final float start, final float end, final float r) {
		final float acc = 32f;
		final double sangle = Math.PI*(2d*start-.5);
		final double sx = Math.cos(sangle);
		final double sy = Math.sin(sangle);
		final double eangle = Math.PI*(2d*end-.5);
		final double ex = Math.cos(eangle);
		final double ey = Math.sin(eangle);

		this.t.addVertex(sx*r, sy*r, 0);
		for(int i=(int)((end<start)?Math.floor(start*acc):Math.ceil(start*acc)); (end<start)?i>end*acc:i<end*acc; i+=(end<start)?-1:1) {
			final double angle = Math.PI*(2d*i/acc-.5);
			final double ix = Math.cos(angle);
			final double iy = Math.sin(angle);
			this.t.addVertex(ix*r, iy*r, 0);
		}
		this.t.addVertex(ex*r, ey*r, 0);
	}

	@Override
	public void renderTileEntityAt(final TileEntity p_147500_1_, final double p_147500_2_, final double p_147500_4_, final double p_147500_6_, final float p_147500_8_)
	{
		this.renderTileEntityAt((TileEntitySign)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
	}
}