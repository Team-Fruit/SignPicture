package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import org.apache.commons.lang3.StringUtils;

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

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer
{
	final Tessellator t = Tessellator.instance;
	protected ImageManager manager;

	public CustomTileEntitySignRenderer(final ImageManager manager) {
		this.manager = manager;
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
			glDisable(GL_CULL_FACE);
			glPushMatrix();
			glScalef(-1f, -1f, -1f);
			glTranslatef(wid/2, hei-.5f, 0);
			glScalef(wid, hei, 0f);
			if (image.state == ImageState.AVAILABLE) {
				glBindTexture(GL_TEXTURE_2D, image.texture.getGlTextureId());
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

			// Draw Canvas - Draw Loading
			if (image.state == ImageState.DOWNLOADING) {
				glDisable(GL_TEXTURE_2D);
				glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				this.t.startDrawing(GL_LINE_LOOP);
				addCircleVertex(0, 1, 1);
				this.t.draw();

				final float progress = image.getProgress();
				glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
				this.t.startDrawing(GL_POLYGON);
				addCircleVertex(0, progress, 1);
				this.t.draw();
				glEnable(GL_TEXTURE_2D);
			}
			glPopMatrix();
			glEnable(GL_CULL_FACE);

			final FontRenderer fontrenderer = func_147498_b();
			f3 = 0.016666668F * f1;
			glTranslatef(0.0F, 0.5F * f1, 0.07F * f1);
			glScalef(f3, -f3, f3);
			glNormal3f(0.0F, 0.0F, -1.0F * f3);
			//glDepthMask(false);

			fontrenderer.drawString(size, -fontrenderer.getStringWidth(size) / 2, 0, 0);

			//glDepthMask(true);
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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