package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryManager;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.entry.content.ContentStateType;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;

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

	public static final ResourceLocation resWarning = new ResourceLocation("signpic", "textures/state/warning.png");
	public static final ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer() {}

	@Override
	public void renderTileEntityAt(final TileEntitySign tile, final double x, final double y, final double z, final float partialTicks)
	{
		Client.startSection("signpic-render");
		final Entry entry = EntryManager.instance.get(EntryId.fromTile(tile));
		if (entry.isValid()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				RenderHelper.startTexture();
				glColor4f(1f, 1f, 1f, .5f);
				super.renderTileEntityAt(tile, x, y, z, partialTicks);
			}

			// Load Image
			final Content content = entry.content();

			// Size
			final ImageSize size = new ImageSize().setAspectSize(entry.meta.size, content.image.getSize());

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

			glTranslatef(entry.meta.offset.x, entry.meta.offset.y, entry.meta.offset.z);
			entry.meta.rotation.rotate();

			glTranslatef(-size.width/2, size.height + ((size.height>=0)?0:-size.height)-.5f, 0f);
			glScalef(1f, -1f, 1f);

			glPushMatrix();
			glScalef(size.width, size.height, 1f);
			if (content.state.getType() == ContentStateType.AVAILABLE) {
				content.image.draw();
			} else {
				final Tessellator t = Tessellator.instance;
				RenderHelper.startShape();
				glLineWidth(1f);
				glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
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
					ContentManager.instance.get(ContentId.fromResource(CustomTileEntitySignRenderer.resError)).image.draw();
					glPopMatrix();
				}
				StateRender.drawLoading(content.state.progress, content.state.getType().circle, content.state.getType().speed);
				StateRender.drawMessage(content, func_147498_b());
			}

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