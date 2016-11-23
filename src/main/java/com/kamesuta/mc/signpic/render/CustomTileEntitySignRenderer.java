package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer {
	protected final Tessellator t = Tessellator.getInstance();

	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer() {
	}

	public void renderSignPicture(final Entry entry, final int destroy, final float opacity) {
		// Load Image
		final Content content = entry.content();

		// Size
		final ImageSize size = new ImageSize().setAspectSize(entry.meta.size, content.image.getSize());

		OpenGL.glPushMatrix();

		OpenGL.glTranslatef(entry.meta.offset.x, entry.meta.offset.y, entry.meta.offset.z);
		entry.meta.rotation.rotate();

		OpenGL.glTranslatef(-size.width/2, size.height+(size.height>=0 ? 0 : -size.height)-.5f, 0f);
		OpenGL.glScalef(1f, -1f, 1f);

		OpenGL.glPushMatrix();
		entry.gui.drawScreen(0, 0, 0, opacity, size.width, size.height);
		OpenGL.glPopMatrix();

		if (destroy>=0) {
			OpenGL.glPushMatrix();
			OpenGL.glScalef(size.width, size.height, 1f);
			RenderHelper.startTexture();
			bindTexture(DESTROY_STAGES[destroy]);
			OpenGL.glTranslatef(0f, 0f, .01f);
			RenderHelper.w.func_181668_a(GL_QUADS, DefaultVertexFormats.field_181707_g);
			RenderHelper.addRectVertex(0, 0, 1, 1);
			RenderHelper.t.draw();
			OpenGL.glTranslatef(0f, 0f, -.02f);
			RenderHelper.w.func_181668_a(GL_QUADS, DefaultVertexFormats.field_181707_g);
			RenderHelper.addRectVertex(0, 0, 1, 1);
			RenderHelper.t.draw();
			OpenGL.glPopMatrix();
		}

		OpenGL.glPopMatrix();
	}

	public void translateBase(final TileEntitySign tile, final double x, final double y, final double z, final float rotateratio) {
		// Vanilla Translate
		final Block block = tile.getBlockType();
		final float f1 = 0.6666667F;
		float f3;

		if (block==Blocks.standing_sign) {
			OpenGL.glTranslatef((float) x+0.5F, (float) y+0.75F*f1, (float) z+0.5F);
			final float f2 = tile.getBlockMetadata()*360/16.0F;
			OpenGL.glRotatef(-f2*rotateratio, 0.0F, 1.0F, 0.0F);
		} else {
			final int j = tile.getBlockMetadata();
			f3 = 0.0F;

			if (j==2)
				f3 = 180.0F;
			if (j==4)
				f3 = 90.0F;
			if (j==5)
				f3 = -90.0F;

			OpenGL.glTranslatef((float) x+0.5F, (float) y+0.75F*f1, (float) z+0.5F);
			OpenGL.glRotatef(-f3*rotateratio, 0.0F, 1.0F, 0.0F);
			OpenGL.glTranslatef(0.0F, 0.0F, -0.4375F);
		}
	}

	public void renderSignPictureBase(final TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float opacity) {
		final Entry entry = EntryId.fromTile(tile).entry();
		if (entry.isValid()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				RenderHelper.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity*Config.instance.renderSeeOpacity);
				super.renderTileEntityAt(tile, x, y, z, partialTicks, destroy);
			}

			OpenGL.glPushMatrix();
			translateBase(tile, x, y, z, 1f);

			// Draw Canvas
			OpenGL.glDisable(GL_CULL_FACE);
			OpenGL.glDisable(GL_LIGHTING);

			renderSignPicture(entry, destroy, opacity);

			OpenGL.glEnable(GL_LIGHTING);
			OpenGL.glEnable(GL_CULL_FACE);

			OpenGL.glPopMatrix();
		} else {
			if (opacity<1f) {
				RenderHelper.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity);
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