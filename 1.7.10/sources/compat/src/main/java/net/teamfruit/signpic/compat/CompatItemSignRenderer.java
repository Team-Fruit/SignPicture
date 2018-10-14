package net.teamfruit.signpic.compat;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.render.WRenderer;
import net.teamfruit.signpic.compat.Compat.CompatBakedModel;
import net.teamfruit.signpic.compat.Compat.CompatModel;
import net.teamfruit.signpic.compat.CompatEvents.CompatModelBakeEvent;
import net.teamfruit.signpic.compat.CompatEvents.CompatModelRegistryEvent;

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

	public static enum ItemSignTransformType {
		NONE,
		THIRD_PERSON_LEFT_HAND,
		THIRD_PERSON_RIGHT_HAND,
		FIRST_PERSON_LEFT_HAND,
		FIRST_PERSON_RIGHT_HAND,
		HEAD,
		GUI,
		GROUND,
		FIXED,
		;

		public static @Nonnull ItemSignTransformType fromType(final @Nullable ItemRenderType type) {
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

	public abstract void renderSignPicture(final @Nonnull ItemSignTransformType type, final @Nonnull CompatVersion version, final @Nullable ItemStack item);

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
		renderSignPicture(ItemSignTransformType.fromType(type), CompatVersion.version(), item);
		OpenGL.glPopAttrib();
		OpenGL.glPopMatrix();
	}

	public void registerModelRegistry(final CompatModelRegistryEvent event, final CompatItemSignModelLoader modelLoader) {
	}

	public void registerModelBakery(final CompatModelBakeEvent event) {
	}

	public CompatBakedModel injectBakedModel(@Nonnull final CompatBakedModel bakedModel) {
		return new CompatBakedModel();
	}

	public static class CompatModelLoaderRegistry {
		public static CompatModel getMissingModel() {
			return new CompatModel();
		}
	}
}