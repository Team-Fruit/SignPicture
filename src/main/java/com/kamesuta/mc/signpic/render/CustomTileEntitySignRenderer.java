package com.kamesuta.mc.signpic.render;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import com.kamesuta.mc.signpic.image.ImageManager;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
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
	public void renderTileEntityAt(final TileEntitySign p_147500_1_, final double p_147500_2_, final double p_147500_4_, final double p_147500_6_, final float p_147500_8_)
	{
		final String s = StringUtils.join(p_147500_1_.signText);
		if (s.endsWith("]") && s.contains("[")) {
			final int start = s.lastIndexOf("[");
			final int end = s.length();
			final String url = s.substring(0, start);
			final String size = s.substring(start +1, end -1);

			this.manager.get(url);

			final String[] sp_size = size.split("x");
			double wid = 1;
			try {
				if (sp_size.length >= 1)
					wid = Double.parseDouble(sp_size[0]);
			} catch (final NumberFormatException e) {}
			final double hei = Double.NaN;
			try {
				if (sp_size.length >= 2)
					wid = Double.parseDouble(sp_size[1]);
			} catch (final NumberFormatException e) {}

			final Block block = p_147500_1_.getBlockType();
			GL11.glPushMatrix();
			final float f1 = 0.6666667F;
			float f3;

			if (block == Blocks.standing_sign)
			{
				GL11.glTranslatef((float)p_147500_2_ + 0.5F, (float)p_147500_4_ + 0.75F * f1, (float)p_147500_6_ + 0.5F);
				final float f2 = p_147500_1_.getBlockMetadata() * 360 / 16.0F;
				GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
			}
			else
			{
				final int j = p_147500_1_.getBlockMetadata();
				f3 = 0.0F;

				if (j == 2)
				{
					f3 = 180.0F;
				}

				if (j == 4)
				{
					f3 = 90.0F;
				}

				if (j == 5)
				{
					f3 = -90.0F;
				}

				GL11.glTranslatef((float)p_147500_2_ + 0.5F, (float)p_147500_4_ + 0.75F * f1, (float)p_147500_6_ + 0.5F);
				GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
			}

			final FontRenderer fontrenderer = func_147498_b();
			f3 = 0.016666668F * f1;
			GL11.glTranslatef(0.0F, 0.5F * f1, 0.07F * f1);
			GL11.glScalef(f3, -f3, f3);
			GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
			GL11.glDepthMask(false);

			fontrenderer.drawString(size, -fontrenderer.getStringWidth(size) / 2, 0, 0);

			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		} else {
			super.renderTileEntityAt(p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntity p_147500_1_, final double p_147500_2_, final double p_147500_4_, final double p_147500_6_, final float p_147500_8_)
	{
		this.renderTileEntityAt((TileEntitySign)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
	}
}