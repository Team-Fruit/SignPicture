package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationGL;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer {
	public static final @Nonnull ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer() {
	}

	public void renderSignPicture(final @Nonnull Entry entry, final int destroy, final float opacity) {
		// Load Image
		final Content content = entry.getContent();

		final CompoundAttr attr = entry.getMeta();

		// Size
		final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
		final SizeData size = attr.sizes.getMovie().get().aspectSize(size01);

		OpenGL.glPushMatrix();

		final OffsetData offset = attr.offsets.getMovie().get();
		OpenGL.glTranslatef(offset.x.offset, offset.y.offset, offset.z.offset);
		RotationGL.glRotate(attr.rotations.getMovie().get().getRotate());

		OpenGL.glTranslatef(-size.getWidth()/2, size.getHeight()+(size.getHeight()>=0 ? 0 : -size.getHeight())-.5f, 0f);
		OpenGL.glScalef(1f, -1f, 1f);

		entry.gui.drawScreen(0, 0, 0, opacity, size.getWidth(), size.getHeight());

		if (destroy>=0) {
			OpenGL.glPushMatrix();
			OpenGL.glScalef(size.getWidth(), size.getHeight(), 1f);
			WRenderer.startTexture();
			bindTexture(DESTROY_STAGES[destroy]);
			OpenGL.glTranslatef(0f, 0f, .01f);
			WRenderer.w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			RenderHelper.addRectVertex(0, 0, 1, 1);
			WRenderer.t.draw();
			OpenGL.glTranslatef(0f, 0f, -.02f);
			WRenderer.w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			RenderHelper.addRectVertex(0, 0, 1, 1);
			WRenderer.t.draw();
			OpenGL.glPopMatrix();
		}

		OpenGL.glPopMatrix();
	}

	public void translateBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z) {
		// Vanilla Translate
		final Block block = tile.getBlockType();
		final float f1 = 0.6666667F;
		float f3;

		if (block==Blocks.STANDING_SIGN) {
			OpenGL.glTranslatef((float) x+0.5F, (float) y+0.75F*f1, (float) z+0.5F);
			final float f2 = tile.getBlockMetadata()*360/16.0F;
			OpenGL.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
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
			OpenGL.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
			OpenGL.glTranslatef(0.0F, 0.0F, -0.4375F);
		}
	}

	public void renderSignPictureBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float opacity) {
		final Entry entry = EntryId.fromTile(tile).entry();
		if (entry.isValid()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				WRenderer.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity*Config.getConfig().renderSeeOpacity.get().floatValue());
				super.func_192841_a(tile, x, y, z, partialTicks, destroy, 0f);
			}

			OpenGL.glPushMatrix();
			translateBase(tile, x, y, z);

			// Draw Canvas
			OpenGL.glDisable(GL_CULL_FACE);
			OpenGL.glDisable(GL_LIGHTING);

			renderSignPicture(entry, destroy, opacity);

			OpenGL.glEnable(GL_LIGHTING);
			OpenGL.glEnable(GL_CULL_FACE);

			OpenGL.glPopMatrix();
		} else {
			if (opacity<1f) {
				WRenderer.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity);
			}
			super.func_192841_a(tile, x, y, z, partialTicks, destroy, 0f);
		}
	}

	@Override
	public void func_192841_a(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float p_192841_10_) {
		if (tile!=null) {
			Client.startSection("signpic-render");
			renderSignPictureBase(tile, x, y, z, partialTicks, destroy, 1f);
			Client.endSection();
		}
	}
}