package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Quat4f;

import org.lwjgl.opengl.GL11;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationGL;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationMath;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId.SignEntryId;
import com.kamesuta.mc.signpic.gui.GuiImage;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer {
	public static final @Nonnull ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer() {
	}

	public void translateBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z) {
		// Vanilla Translate
		final Block block = tile.getBlockType();
		final float f1 = 0.6666667F;

		if (block==Blocks.standing_sign) {
			OpenGL.glTranslatef((float) x+.5f, (float) y+.75f*f1, (float) z+.5f);
			RotationGL.glRotate(getSignRotate(tile));
		} else {
			OpenGL.glTranslatef((float) x+.5f, (float) y+.75f*f1, (float) z+.5f);
			RotationGL.glRotate(getSignRotate(tile));
			OpenGL.glTranslatef(0f, 0f, -.4375f);
		}
	}

	public @Nonnull Quat4f getSignRotate(final @Nonnull TileEntitySign tile) {
		// Vanilla Translate
		final Block block = tile.getBlockType();
		if (block==Blocks.standing_sign) {
			final float f2 = tile.getBlockMetadata()*360f/16f;
			return RotationMath.quatDeg(-f2, 0f, 1f, 0f);
		} else {
			final int j = tile.getBlockMetadata();
			float f3;
			switch (j) {
				case 2:
					f3 = 180f;
					break;
				case 4:
					f3 = 90f;
					break;
				case 5:
					f3 = -90f;
					break;
				default:
					f3 = 0f;
					break;
			}
			return RotationMath.quatDeg(-f3, 0f, 1f, 0f);
		}
	}

	public void renderSignPictureBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final float opacity) {
		final Entry entry = SignEntryId.fromTile(tile).entry();
		if (entry.isOutdated()&&CurrentMode.instance.isState(CurrentMode.State.SEE))
			OpenGL.glDisable(GL11.GL_DEPTH_TEST);
		if (entry.isValid()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				WRenderer.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity*Config.getConfig().renderSeeOpacity.get().floatValue());
				super.renderTileEntityAt(tile, x, y, z, partialTicks);
			}

			OpenGL.glPushMatrix();
			translateBase(tile, x, y, z);

			// Draw Canvas
			OpenGL.glDisable(GL_CULL_FACE);

			final GuiImage gui = entry.getGui();
			gui.applyLight(tile.xCoord, tile.yCoord, tile.zCoord, getSignRotate(tile));
			gui.renderSignPicture(opacity, 1f, new RenderOption());

			OpenGL.glEnable(GL_CULL_FACE);

			OpenGL.glPopMatrix();
		} else {
			if (opacity<1f) {
				WRenderer.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity);
			}
			super.renderTileEntityAt(tile, x, y, z, partialTicks);
		}
		OpenGL.glEnable(GL11.GL_DEPTH_TEST);
		WRenderer.startTexture();
	}

	@Override
	public void renderTileEntityAt(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks) {
		if (tile!=null) {
			Client.startSection("signpic-render");
			renderSignPictureBase(tile, x, y, z, partialTicks, 1f);
			Client.endSection();
		}
	}

	@Override
	public void renderTileEntityAt(final @Nullable TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
		this.renderTileEntityAt((TileEntitySign) tile, x, y, z, partialTicks);
	}
}