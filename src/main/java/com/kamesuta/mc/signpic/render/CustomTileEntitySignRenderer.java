package com.kamesuta.mc.signpic.render;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer {
	protected final Tessellator t = Tessellator.getInstance();

	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer() {
	}

	public void renderSignPicture(final Entry entry, final float opacity) {
		// Load Image
		final Content content = entry.content();

		// Size
		final ImageSize size = new ImageSize().setAspectSize(entry.meta.size, content.image.getSize());

		GlStateManager.pushMatrix();

		GlStateManager.translate(entry.meta.offset.x, entry.meta.offset.y, entry.meta.offset.z);
		entry.meta.rotation.rotate();

		GlStateManager.translate(-size.width/2, size.height+(size.height>=0 ? 0 : -size.height)-.5f, 0f);
		GlStateManager.scale(1f, -1f, 1f);

		entry.gui.drawScreen(0, 0, 0, opacity, size.width, size.height);

		GlStateManager.popMatrix();
	}

	public void translateBase(final TileEntitySign tile, final double x, final double y, final double z, final float rotateratio) {
		// Vanilla Translate
		final Block block = tile.getBlockType();
		final float f1 = 0.6666667F;
		float f3;

		if (block==Blocks.standing_sign) {
			GlStateManager.translate((float) x+0.5F, (float) y+0.75F*f1, (float) z+0.5F);
			final float f2 = tile.getBlockMetadata()*360/16.0F;
			GlStateManager.rotate(-f2*rotateratio, 0.0F, 1.0F, 0.0F);
		} else {
			final int j = tile.getBlockMetadata();
			f3 = 0.0F;

			if (j==2)
				f3 = 180.0F;
			if (j==4)
				f3 = 90.0F;
			if (j==5)
				f3 = -90.0F;

			GlStateManager.translate((float) x+0.5F, (float) y+0.75F*f1, (float) z+0.5F);
			GlStateManager.rotate(-f3*rotateratio, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(0.0F, 0.0F, -0.4375F);
		}
	}

	public void renderSignPictureBase(final TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float opacity) {
		final Entry entry = EntryId.fromTile(tile).entry();
		if (entry.isValid()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				RenderHelper.startTexture();
				GlStateManager.color(1f, 1f, 1f, opacity*Config.instance.renderSeeOpacity);
				super.renderTileEntityAt(tile, x, y, z, partialTicks, destroy);
			}

			GlStateManager.pushMatrix();
			translateBase(tile, x, y, z, 1f);

			// Draw Canvas
			GlStateManager.disableCull();
			GlStateManager.disableLighting();

			renderSignPicture(entry, opacity);

			GlStateManager.enableLighting();
			GlStateManager.enableCull();

			GlStateManager.popMatrix();
		} else {
			if (opacity<1f) {
				RenderHelper.startTexture();
				GlStateManager.color(1f, 1f, 1f, opacity);
			}
			super.renderTileEntityAt(tile, x, y, z, partialTicks, destroy);
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy) {
		Client.startSection("signpic-render");
		renderSignPictureBase(tile, x, y, z, partialTicks, destroy, 1f);
		Client.endSection();
	}
}