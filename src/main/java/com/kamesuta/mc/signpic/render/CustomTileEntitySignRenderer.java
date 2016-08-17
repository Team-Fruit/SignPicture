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
			glScalef(-1f, 1f, -1f);
			glTranslatef(.5f, .5f, 0f);
			glTranslatef(wid/2-.5f, hei-1f, 0);
			glScalef(-wid, -hei, 0f);
			if (image.state == ImageState.AVAILABLE) {
				glBindTexture(GL_TEXTURE_2D, image.texture.getGlTextureId());
				final Tessellator t = Tessellator.instance;
				t.startDrawingQuads();
				t.addVertexWithUV(0, 0, 0, 0, 0);
				t.addVertexWithUV(0, 1, 0, 0, 1);
				t.addVertexWithUV(1, 1, 0, 1, 1);
				t.addVertexWithUV(1, 0, 0, 1, 0);
				t.draw();
			} else {
				glDisable(GL_TEXTURE_2D);
				glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
				glBegin(GL_LINE_LOOP);
				glVertex3i(0, 0, 0);
				glVertex3i(0, 1, 0);
				glVertex3i(1, 1, 0);
				glVertex3i(1, 0, 0);
				glEnd();
				glEnable(GL_TEXTURE_2D);
			}

			// Draw Canvas - Draw Loading
			if (image.state == ImageState.DOWNLOADING) {
				glDisable(GL_TEXTURE_2D);
				glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				glBegin(GL_LINE_LOOP);
				for(int i =0; i <= 32; i++){
					final double angle = -Math.PI/2+2*Math.PI*i/32;
					final double ix = Math.cos(angle)/4;
					final double iy = Math.sin(angle)/4;
					glVertex3d(.5+ix/2, .5+iy/2, 0);
				}
				glEnd();

				final float progress = image.getProgress();
				glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
				glBegin(GL_POLYGON);
				glVertex3d(.5, .5, 0);
				for(int i =0; i <= 32; i++){
					final double angle = -Math.PI/2+2*Math.PI*i/32;
					if (((double)i / 32) >= progress)
						break;
					final double ix = Math.cos(angle)/4;
					final double iy = Math.sin(angle)/4;
					glVertex3d(.5+ix/2, .5+iy/2, 0);
				}
				final double angle = -Math.PI/2+2*Math.PI*progress;
				final double ix = Math.cos(angle)/4;
				final double iy = Math.sin(angle)/4;
				glVertex3d(.5+ix/2, .5+iy/2, 0);

				glEnd();
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

	@Override
	public void renderTileEntityAt(final TileEntity p_147500_1_, final double p_147500_2_, final double p_147500_4_, final double p_147500_6_, final float p_147500_8_)
	{
		this.renderTileEntityAt((TileEntitySign)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
	}
}