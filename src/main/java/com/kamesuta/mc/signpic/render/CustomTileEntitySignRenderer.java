package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer
{
	protected final ImageManager manager;
	public static final ResourceLocation resWarning = new ResourceLocation("signpic", "textures/state/warning.png");
	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer(final ImageManager manager) {
		this.manager = manager;
	}

	@Override
	public void renderTileEntityAt(final TileEntitySign te, final double x, final double y, final double z, final float partialTicks, final int destroyStage)
	{
		Client.startSection("signpic-render");
		final Sign sign = new Sign().parseSignEntity(te);
		if (sign.isVaild()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				RenderHelper.startTexture();
				GlStateManager.color(1f, 1f, 1f, .5f);
				super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
			}

			// Load Image
			final Image image = this.manager.get(sign.getURL());

			// Size
			final ImageSize size = new ImageSize().setAspectSize(sign.meta.size, image.getSize());

			// Vanilla Translate
			final Block block = te.getBlockType();
			GlStateManager.pushMatrix();
			final float f1 = 0.6666667F;
			float f3;

			if (block == Blocks.STANDING_SIGN) {
				GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f1, (float)z + 0.5F);
				final float f2 = te.getBlockMetadata() * 360 / 16.0F;
				GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
			} else {
				final int j = te.getBlockMetadata();
				f3 = 0.0F;

				if (j == 2) f3 = 180.0F;
				if (j == 4) f3 = 90.0F;
				if (j == 5) f3 = -90.0F;

				GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f1, (float)z + 0.5F);
				GlStateManager.rotate(-f3, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.0F, 0.0F, -0.4375F);
			}

			// Draw Canvas
			GlStateManager.disableCull();;
			GlStateManager.disableLighting();;
			GlStateManager.pushMatrix();

			GlStateManager.translate(sign.meta.offset.x, sign.meta.offset.y, sign.meta.offset.z);
			sign.meta.rotation.rotate();

			GlStateManager.translate(-size.width/2, size.height + ((size.height>=0)?0:-size.height)-.5f, 0f);
			GlStateManager.scale(1f, -1f, 1f);

			GlStateManager.pushMatrix();
			GlStateManager.scale(size.width, size.height, 1f);
			if (destroyStage>= 0) {
				GlStateManager.pushMatrix();
				RenderHelper.startTexture();
				bindTexture(DESTROY_STAGES[destroyStage]);
				GlStateManager.translate(0f, 0f, .01f);
				RenderHelper.w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				RenderHelper.addRectVertex(0, 0, 1, 1);
				RenderHelper.t.draw();
				GlStateManager.translate(0f, 0f, -.02f);
				RenderHelper.w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				RenderHelper.addRectVertex(0, 0, 1, 1);
				RenderHelper.t.draw();
				GlStateManager.popMatrix();
			}
			image.getState().mainImage(this.manager, image);
			GlStateManager.popMatrix();

			if (size.width<1.5f || size.height<1.5) {
				GlStateManager.scale(.5f, .5f, .5f);
				GlStateManager.translate(size.width/2, size.height/4, 0);
			}
			GlStateManager.translate(size.width/2, size.height/2, 0);
			GlStateManager.scale(.5f, .5f, 1f);
			image.getState().themeImage(this.manager, image);
			image.getState().message(this.manager, image, getFontRenderer());

			GlStateManager.popMatrix();

			GlStateManager.enableLighting();;
			GlStateManager.enableCull();;

			GlStateManager.popMatrix();
		} else {
			super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		}
		Client.endSection();
	}
}