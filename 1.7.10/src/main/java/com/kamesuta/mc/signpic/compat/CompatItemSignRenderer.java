package com.kamesuta.mc.signpic.compat;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.compat.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public abstract class CompatItemSignRenderer implements IItemRenderer {
	public abstract boolean isSignPicture(@Nullable final ItemStack item);

	public abstract boolean isSeeMode();

	public abstract float getRenderSeeOpacity();

	public void renderSign() {
		final IIcon iicon = Items.sign.getIconFromDamage(0);
		TextureUtil.func_152777_a(false, false, 1f);
		final float f = iicon.getMinU();
		final float f1 = iicon.getMaxU();
		final float f2 = iicon.getMinV();
		final float f3 = iicon.getMaxV();
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625f);
	}

	public static enum SignTransformType {
		NONE,
		THIRD_PERSON,
		FIRST_PERSON,
		HEAD,
		GUI,
		GROUND,
		FIXED,
		;

		public static @Nonnull SignTransformType fromType(final @Nullable ItemRenderType type) {
			if (type==null)
				return NONE;
			switch (type) {
				case ENTITY:
					if (RenderItem.renderInFrame)
						return FIXED;
					else
						return GROUND;
				case INVENTORY:
					return GUI;
				default:
					return NONE;
			}
		}
	}

	public static enum SignCompatVersion {
		V7,
		V8,
		V10;

		public static @Nonnull SignCompatVersion version() {
			return V7;
		}
	}

	public abstract void renderSignPicture(final @Nonnull SignTransformType type, final @Nullable ItemStack item);

	@Override
	public boolean handleRenderType(final @Nullable ItemStack item, final @Nullable ItemRenderType type) {
		return isSignPicture(item);
	}

	@Override
	public boolean shouldUseRenderHelper(final @Nullable ItemRenderType type, final @Nullable ItemStack item, final @Nullable ItemRendererHelper helper) {
		return type==ItemRenderType.ENTITY;
	}

	@Override
	public void renderItem(final @Nullable ItemRenderType type, final @Nullable ItemStack item, final @Nullable Object... data) {
		OpenGL.glPushMatrix();
		if (type==ItemRenderType.ENTITY&&isSeeMode()) {
			OpenGL.glPushMatrix();
			WRenderer.startTexture();
			OpenGL.glColor4f(1f, 1f, 1f, getRenderSeeOpacity());
			OpenGL.glTranslatef(-.5f, -.25f, 0.0625f/2f);
			renderSign();
			OpenGL.glPopMatrix();
		}
		OpenGL.glPushAttrib();
		OpenGL.glDisable(GL_CULL_FACE);
		renderSignPicture(SignTransformType.fromType(type), item);
		OpenGL.glPopAttrib();
		OpenGL.glPopMatrix();
	}
}