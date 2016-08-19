package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.ImageSize;
import com.kamesuta.mc.signpic.image.ImageState;
import com.kamesuta.mc.signpic.util.SignParser;

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
	protected final ImageManager manager;
	protected final Tessellator t = Tessellator.instance;

	protected ResourceLocation resWarning;
	protected ResourceLocation resError;

	public CustomTileEntitySignRenderer(final ImageManager manager) {
		this.manager = manager;
		this.resWarning = new ResourceLocation(Reference.MODID, "textures/state/warning.png");
		this.resError = new ResourceLocation(Reference.MODID, "textures/state/error.png");
	}

	@Override
	public void renderTileEntityAt(final TileEntitySign tile, final double x, final double y, final double z, final float color)
	{
		final SignParser sign = new SignParser(tile);
		if (sign.isVaild()) {
			// Size
			final ImageSize size = sign.size();

			// Load Image
			final Image image = this.manager.get(sign.id());

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
			final FontRenderer fontrenderer = func_147498_b();
			//glDisable(GL_CULL_FACE);
			glDisable(GL_LIGHTING);
			glPushMatrix();
			glTranslatef(-size.width/2, size.height-.5f, 0f);
			glScalef(1f, -1f, 1f);
			glPushMatrix();
			glScalef(size.width, size.height, 1f);

			drawImage(image);
			if (image.getState() != ImageState.AVAILABLE) {
				glLineWidth(1f);
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

			if (size.width<1.5f || size.height<1.5) {
				glScalef(.5f, .5f, .5f);
				glTranslatef(size.width/2, -size.height/2-1f, 0);
			}
			// Draw Canvas - Draw Loading
			glPushMatrix();
			glTranslatef(size.width/2, size.height/2, 0);
			glScalef(.5f, .5f, 1f);
			glPushMatrix();
			glScalef(.5f, .5f, 1f);
			if (image.getState() == ImageState.IOLOADING || image.getState() == ImageState.DOWNLOADING) {
				glLineWidth(3f);
				glDisable(GL_TEXTURE_2D);
				if (image.getState() == ImageState.IOLOADING)
					glColor4f(0f/256f, 144f/256f, 55f/256f, 1f);
				if (image.getState() == ImageState.DOWNLOADING)
					glColor4f(0f/256f, 102f/256f, 204f/256f, 1f);
				this.t.startDrawing(GL_LINE_LOOP);
				addCircleVertex(0f, 1f, 1f);
				this.t.draw();

				glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
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

				if (image.getState() == ImageState.DOWNLOADING) {
					final float progress = image.getProgress();
					glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
					this.t.startDrawing(GL_POLYGON);
					this.t.addVertex(0f, 0f, 0f);
					addCircleVertex(progress, 0f, 1f);
					this.t.draw();
				}

				glEnable(GL_TEXTURE_2D);
			}
			glPopMatrix();

			glPushMatrix();
			glTranslatef(-.5f, -.5f, 0f);
			if (image.getState() == ImageState.FAILED) {
				drawImage(this.manager.get(this.resWarning));
			}
			if (image.getState() == ImageState.ERROR) {
				drawImage(this.manager.get(this.resError));
			}
			glPopMatrix();

			if (image.getState() != ImageState.AVAILABLE) {
				f3 = 0.06666668F * f1;
				glTranslatef(0f, 1f, 0f);
				glPushMatrix();
				glScalef(f3, f3, 1f);
				final String msg1 = image.getStatusMessage();
				fontrenderer.drawString(msg1, -fontrenderer.getStringWidth(msg1) / 2, -fontrenderer.FONT_HEIGHT, 0xffffff);
				glPopMatrix();
				f3 = 0.036666668F * f1;
				glPushMatrix();
				glScalef(f3, f3, 1f);
				final String msg2 = image.getId();
				fontrenderer.drawString(msg2, -fontrenderer.getStringWidth(msg2) / 2, 0, 0xffffff);
				glPopMatrix();
				final String msg3 = image.advMessage();
				if (msg3 != null) {
					glPushMatrix();
					glScalef(f3, f3, 1f);
					fontrenderer.drawString(msg3, -fontrenderer.getStringWidth(msg3) / 2, fontrenderer.FONT_HEIGHT, 0xffffff);
					glPopMatrix();
				}
			}

			glPopMatrix();
			glPopMatrix();
			glEnable(GL_CULL_FACE);
			glEnable(GL_LIGHTING);

			glPopMatrix();
		} else {
			super.renderTileEntityAt(tile, x, y, z, color);
		}
	}

	protected void drawImage(final Image image) {
		if (image.getState() == ImageState.AVAILABLE) {
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			image.getTexture();
			this.t.startDrawingQuads();
			this.t.addVertexWithUV(0, 0, 0, 0, 0);
			this.t.addVertexWithUV(0, 1, 0, 0, 1);
			this.t.addVertexWithUV(1, 1, 0, 1, 1);
			this.t.addVertexWithUV(1, 0, 0, 1, 0);
			this.t.draw();
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