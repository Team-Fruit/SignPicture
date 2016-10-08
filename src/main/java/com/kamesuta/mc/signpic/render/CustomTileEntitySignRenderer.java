package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.state.ContentStateType;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer
{
	protected final Tessellator t = Tessellator.instance;

	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer() {}

	public void renderImage(final Content content, final ImageSize size, final float opacity) {
		glPushMatrix();
		glScalef(size.width, size.height, 1f);
		if (content.state.getType() == ContentStateType.AVAILABLE) {
			glColor4f(1.0F, 1.0F, 1.0F, opacity * 1.0F);
			content.image.draw();
		} else {
			final Tessellator t = Tessellator.instance;
			RenderHelper.startShape();
			glLineWidth(1f);
			glColor4f(1.0F, 0.0F, 0.0F, opacity * 1.0F);
			t.startDrawing(GL_LINE_LOOP);
			t.addVertex(0, 0, 0);
			t.addVertex(0, 1, 0);
			t.addVertex(1, 1, 0);
			t.addVertex(1, 0, 0);
			t.draw();
		}
		glPopMatrix();

		if (size.width<1.5f || size.height<1.5) {
			glScalef(.5f, .5f, .5f);
			glTranslatef(size.width/2, size.height/4, 0);
		}
		glTranslatef(size.width/2, size.height/2, 0);
		glScalef(.5f, .5f, 1f);
		if (content.state.getType() != ContentStateType.AVAILABLE) {
			if (content.state.getType() == ContentStateType.ERROR) {
				RenderHelper.startShape();
				glPushMatrix();
				glTranslatef(-.5f, -.5f, 0f);
				RenderHelper.startTexture();
				bindTexture(resError);
				RenderHelper.drawRectTexture(GL_QUADS);
				glPopMatrix();
			}
			StateRender.drawLoading(content.state.getProgress(), content.state.getType().circle, content.state.getType().speed);
			StateRender.drawMessage(content, func_147498_b());
		}
	}

	public void renderSignPicture(final Entry entry, final float opacity) {
		// Load Image
		final Content content = entry.content();

		// Size
		final ImageSize size = new ImageSize().setAspectSize(entry.meta.size, content.image.getSize());

		glPushMatrix();

		glTranslatef(entry.meta.offset.x, entry.meta.offset.y, entry.meta.offset.z);
		entry.meta.rotation.rotate();

		glTranslatef(-size.width/2, size.height + ((size.height>=0)?0:-size.height)-.5f, 0f);
		glScalef(1f, -1f, 1f);

		renderImage(content, size, opacity);

		glPopMatrix();
	}

	public void translateBase(final TileEntitySign tile, final double x, final double y, final double z, final float rotateratio) {
		// Vanilla Translate
		final Block block = tile.getBlockType();
		final float f1 = 0.6666667F;
		float f3;

		if (block == Blocks.standing_sign) {
			glTranslatef((float)x + 0.5F, (float)y + 0.75F * f1, (float)z + 0.5F);
			final float f2 = tile.getBlockMetadata() * 360 / 16.0F;
			glRotatef(-f2 * rotateratio, 0.0F, 1.0F, 0.0F);
		} else {
			final int j = tile.getBlockMetadata();
			f3 = 0.0F;

			if (j == 2) f3 = 180.0F;
			if (j == 4) f3 = 90.0F;
			if (j == 5) f3 = -90.0F;

			glTranslatef((float)x + 0.5F, (float)y + 0.75F * f1, (float)z + 0.5F);
			glRotatef(-f3 * rotateratio, 0.0F, 1.0F, 0.0F);
			glTranslatef(0.0F, 0.0F, -0.4375F);
		}
	}

	public void renderSignPictureBase(final TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final float opacity) {
		final Entry entry = EntryId.fromTile(tile).entry();
		if (entry.isValid()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				RenderHelper.startTexture();
				glColor4f(1f, 1f, 1f, opacity * Config.instance.renderSeeOpacity);
				super.renderTileEntityAt(tile, x, y, z, partialTicks);
			}

			glPushMatrix();
			translateBase(tile, x, y, z, 1f);

			// Draw Canvas
			glDisable(GL_CULL_FACE);
			glDisable(GL_LIGHTING);

			renderSignPicture(entry, opacity);

			glEnable(GL_LIGHTING);
			glEnable(GL_CULL_FACE);

			glPopMatrix();
		} else {
			if (opacity < 1f) {
				RenderHelper.startTexture();
				glColor4f(1f, 1f, 1f, opacity);
			}
			super.renderTileEntityAt(tile, x, y, z, partialTicks);
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntitySign tile, final double x, final double y, final double z, final float partialTicks)
	{
		Client.startSection("signpic-render");
		renderSignPictureBase(tile, x, y, z, partialTicks, 1f);
		Client.endSection();
	}

	@Override
	public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks)
	{
		this.renderTileEntityAt((TileEntitySign)tile, x, y, z, partialTicks);
	}
}