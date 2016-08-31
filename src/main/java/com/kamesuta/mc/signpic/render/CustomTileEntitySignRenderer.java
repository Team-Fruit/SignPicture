package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.ImageSize;
import com.kamesuta.mc.signpic.util.SignParser;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer
{
	protected Minecraft mc = FMLClientHandler.instance().getClient();
	protected final ImageManager manager;
	protected final Tessellator t = Tessellator.instance;

	public static final ResourceLocation resWarning = new ResourceLocation("signpic", "textures/state/warning.png");
	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer(final ImageManager manager) {
		this.manager = manager;
	}

	@Override
	public void renderTileEntityAt(final TileEntitySign tile, final double x, final double y, final double z, final float color)
	{
		this.mc.mcProfiler.startSection("signpic-render");
		final SignParser sign = new SignParser(tile);
		if (sign.isVaild()) {
			// Load Image
			final Image image = this.manager.get(sign.id());

			// Size
			final ImageSize size = sign.size().getAspectSize(image.getSize());

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
			glDisable(GL_LIGHTING);
			glEnable(GL_BLEND);
			glPushMatrix();

			glTranslatef(-size.width/2, size.height + .5f*((size.height>=0)?-1f:1f), 0f);
			glScalef(1f, -1f, 1f);

			glPushMatrix();
			glScalef(size.width, size.height, 1f);
			image.getState().mainImage(this.manager, image);
			glPopMatrix();

			if (size.width<1.5f || size.height<1.5) {
				glScalef(.5f, .5f, .5f);
				glTranslatef(size.width/2, size.height/4, 0);
			}
			glTranslatef(size.width/2, size.height/2, 0);
			glScalef(.5f, .5f, 1f);
			image.getState().themeImage(this.manager, image);
			image.getState().message(this.manager, image, func_147498_b());

			glPopMatrix();

			glDisable(GL_BLEND);
			glEnable(GL_LIGHTING);
			glEnable(GL_CULL_FACE);

			glPopMatrix();
		} else {
			super.renderTileEntityAt(tile, x, y, z, color);
		}
		this.mc.mcProfiler.endSection();
	}

	@Override
	public void renderTileEntityAt(final TileEntity p_147500_1_, final double p_147500_2_, final double p_147500_4_, final double p_147500_6_, final float p_147500_8_)
	{
		this.renderTileEntityAt((TileEntitySign)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
	}
}