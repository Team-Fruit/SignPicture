package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.block.Block;
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

	public static final ResourceLocation resWarning = new ResourceLocation("signpic", "textures/state/warning.png");
	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer(final ImageManager manager) {
		this.manager = manager;
	}

	@Override
	public void renderTileEntityAt(final TileEntitySign tile, final double x, final double y, final double z, final float partialTicks)
	{
		Client.startSection("signpic-render");
		final Sign sign = new Sign().parseSignEntity(tile);
		if (sign.isVaild()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				RenderHelper.startTexture();
				glColor4f(1f, 1f, 1f, .5f);
				super.renderTileEntityAt(tile, x, y, z, partialTicks);
			}

			// Load Image
			final Image image = this.manager.get(sign.getURL());

			// Size
			final ImageSize size = new ImageSize().setAspectSize(sign.meta.size, image.getSize());

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
			glPushMatrix();

			glTranslatef(sign.meta.offset.x, sign.meta.offset.y, sign.meta.offset.z);
			sign.meta.rotation.rotate();

			glTranslatef(-size.width/2, size.height + ((size.height>=0)?0:-size.height)-.5f, 0f);
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

			glEnable(GL_LIGHTING);
			glEnable(GL_CULL_FACE);

			glPopMatrix();
		} else {
			super.renderTileEntityAt(tile, x, y, z, partialTicks);
		}
		Client.endSection();
	}

	@Override
	public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks)
	{
		this.renderTileEntityAt((TileEntitySign)tile, x, y, z, partialTicks);
	}
}