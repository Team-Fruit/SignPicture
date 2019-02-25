package net.teamfruit.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.vecmath.Quat4f;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.render.RenderOption;
import net.teamfruit.bnnwidget.render.WRenderer;
import net.teamfruit.signpic.Client;
import net.teamfruit.signpic.Config;
import net.teamfruit.signpic.attr.prop.RotationData.RotationGL;
import net.teamfruit.signpic.attr.prop.RotationData.RotationMath;
import net.teamfruit.signpic.compat.Compat.CompatBlockPos;
import net.teamfruit.signpic.compat.Compat.CompatBlocks;
import net.teamfruit.signpic.entry.Entry;
import net.teamfruit.signpic.entry.EntryId.SignEntryId;
import net.teamfruit.signpic.gui.GuiImage;
import net.teamfruit.signpic.mode.CurrentMode;

public class CustomBlockSignRenderer implements IBlockSignRenderer {
	public static final @Nonnull ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public final @Nonnull IBlockSignRenderer base;

	public CustomBlockSignRenderer(final @Nonnull IBlockSignRenderer base) {
		this.base = base;
	}

	public void translateBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z) {
		// Vanilla Translate
		final Block block = tile.getBlockType();
		final float f1 = 0.6666667F;

		if (block==CompatBlocks.STANDING_SIGN) {
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
		if (block==CompatBlocks.STANDING_SIGN) {
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

	@Override
	public void renderBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float opacity) {
		Client.startSection("signpic-render");
		final Entry entry = SignEntryId.fromTile(tile).entry();
		if (entry.isOutdated()&&CurrentMode.instance.isState(CurrentMode.State.SEE))
			OpenGL.glDisable(GL11.GL_DEPTH_TEST);
		if (entry.isValid()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				WRenderer.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity*Config.getConfig().renderSeeOpacity.get().floatValue());
				this.base.renderBase(tile, x, y, z, partialTicks, destroy, opacity);
			}

			OpenGL.glPushMatrix();
			translateBase(tile, x, y, z);

			// Draw Canvas
			OpenGL.glDisable(GL_CULL_FACE);

			final GuiImage gui = entry.getGui();
			final CompatBlockPos pos = CompatBlockPos.getTileEntityPos(tile);
			gui.applyLight(pos.getX(), pos.getY(), pos.getZ(), getSignRotate(tile));
			gui.renderSignPicture(opacity, 1f, new RenderOption());

			OpenGL.glEnable(GL_CULL_FACE);

			OpenGL.glPopMatrix();
		} else {
			if (opacity<1f) {
				WRenderer.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity);
			}
			this.base.renderBase(tile, x, y, z, partialTicks, destroy, opacity);
		}
		OpenGL.glEnable(GL11.GL_DEPTH_TEST);
		WRenderer.startTexture();
		Client.endSection();
	}
}
