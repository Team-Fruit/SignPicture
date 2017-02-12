package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.render.WRenderer.WVertex;
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
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class CustomTileEntitySignRenderer extends TileEntitySignRenderer {
	public static final @Nonnull ResourceLocation resError = new ResourceLocation("signpic", "textures/state/error.png");

	public CustomTileEntitySignRenderer() {
	}

	public void renderSignPicture(final @Nonnull Entry entry, final float opacity) {
		// Load Image
		final Content content = entry.getContent();

		final CompoundAttr attr = entry.getMeta();

		// Size
		final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
		final SizeData size = attr.sizes.getMovie().get().aspectSize(size01);

		OpenGL.glPushMatrix();

		final OffsetData offset = attr.offsets.getMovie().get();
		final OffsetData centeroffset = attr.centeroffsets.getMovie().get();
		final Quat4f rotate = attr.rotations.getMovie().get().getRotate();
		final boolean see = CurrentMode.instance.isState(CurrentMode.State.SEE);
		if (see) {
			OpenGL.glColor4f(.5f, .5f, .5f, opacity*.5f);
			OpenGL.glLineWidth(1f);
			WRenderer.startShape();
			final WVertex v1 = WRenderer.begin(GL_LINE_STRIP);
			final Matrix4f m = new Matrix4f();
			final Point3f p2 = new Point3f();
			final Vector3f ov = new Vector3f(offset.x.offset, offset.y.offset, offset.z.offset);
			final Vector3f cv = new Vector3f(centeroffset.x.offset, centeroffset.y.offset, centeroffset.z.offset);
			v1.pos(p2.x, p2.y, p2.z);
			m.set(ov);
			m.transform(p2);
			m.set(cv);
			m.transform(p2);
			v1.pos(p2.x, p2.y, p2.z);
			p2.set(0f, 0f, 0f);
			cv.negate();
			m.set(cv);
			m.transform(p2);
			m.set(rotate);
			m.transform(p2);
			cv.negate();
			m.set(cv);
			m.transform(p2);
			v1.pos(p2.x, p2.y, p2.z);
			v1.draw();
		}
		OpenGL.glTranslatef(offset.x.offset+centeroffset.x.offset, offset.y.offset+centeroffset.y.offset, offset.z.offset+centeroffset.z.offset);
		RotationGL.glRotate(rotate);
		OpenGL.glTranslatef(-centeroffset.x.offset, -centeroffset.y.offset, -centeroffset.z.offset);

		OpenGL.glTranslatef(-size.getWidth()/2, size.getHeight()+(size.getHeight()>=0 ? 0 : -size.getHeight())-.5f, 0f);
		OpenGL.glScalef(1f, -1f, 1f);

		entry.gui.drawScreen(0, 0, 0, opacity, size.getWidth(), size.getHeight());

		OpenGL.glPopMatrix();
	}

	public void translateBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z) {
		// Vanilla Translate
		final Block block = tile.getBlockType();
		final float f1 = 0.6666667F;
		float f3;

		if (block==Blocks.standing_sign) {
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

	public void renderSignPictureBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final float opacity) {
		final Entry entry = EntryId.fromTile(tile).entry();
		if (entry.isOutdated()&&CurrentMode.instance.isState(CurrentMode.State.SEE))
			OpenGL.glDisable(GL11.GL_DEPTH_TEST);
		if (entry.isValid()) {
			if (CurrentMode.instance.isState(CurrentMode.State.SEE)) {
				WRenderer.startTexture();
				OpenGL.glColor4f(1f, 1f, 1f, opacity*Config.getConfig().renderSeeOpacity.get().floatValue());
				super.renderTileEntityAt(tile, x, y, z, partialTicks);
			}

			final CompoundAttr attr = entry.getMeta();
			int lightx = (int) attr.f.getMovie().get().data;
			int lighty = (int) attr.g.getMovie().get().data;
			if (lightx!=-1||lighty!=-1) {
				if (lightx<0||lighty<0) {
					final OffsetData offset = attr.offsets.getMovie().get();
					final int lsign = Client.mc.theWorld.getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord, tile.zCoord, 0);
					final int lpicture = Client.mc.theWorld.getLightBrightnessForSkyBlocks(
							MathHelper.floor_double(tile.xCoord+offset.x.offset),
							MathHelper.floor_double(tile.yCoord+offset.y.offset),
							MathHelper.floor_double(tile.zCoord+offset.z.offset),
							0);
					if (lightx<0)
						if (lightx==-2)
							lightx = lpicture%65536>>4;
						else
							lightx = lsign%65536>>4;
					if (lighty<0)
						if (lighty==-2)
							lighty = lpicture/65536>>4;
						else
							lighty = lsign/65536>>4;
				}
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightx<<4, lighty<<4);
			}

			OpenGL.glPushMatrix();
			translateBase(tile, x, y, z);

			// Draw Canvas
			OpenGL.glDisable(GL_CULL_FACE);
			OpenGL.glDisable(GL_LIGHTING);

			renderSignPicture(entry, opacity);

			OpenGL.glEnable(GL_LIGHTING);
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