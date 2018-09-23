package com.kamesuta.mc.signpic.compat;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.kamesuta.mc.bnnwidget.compat.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatModelBakeEvent;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

@SuppressWarnings("deprecation")
public abstract class CompatItemSignRenderer implements IPerspectiveAwareModel {
	public abstract boolean isSignPicture(@Nullable final ItemStack item);

	public abstract boolean isSeeMode();

	public abstract float getRenderSeeOpacity();

	public void renderSign(@Nullable final ItemStack item) {
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

		public static @Nonnull ItemSignTransformType fromType(final @Nullable TransformType type) {
			if (type==null)
				return NONE;
			switch (type) {
				case THIRD_PERSON_LEFT_HAND:
					return THIRD_PERSON_LEFT_HAND;
				case THIRD_PERSON_RIGHT_HAND:
					return THIRD_PERSON_RIGHT_HAND;
				case FIRST_PERSON_LEFT_HAND:
					return FIRST_PERSON_LEFT_HAND;
				case FIRST_PERSON_RIGHT_HAND:
					return FIRST_PERSON_RIGHT_HAND;
				case HEAD:
					return HEAD;
				case GUI:
					return GUI;
				case GROUND:
					return GROUND;
				case FIXED:
					return FIXED;
				default:
					return NONE;
			}
		}
	}

	public static enum ItemSignCompatVersion {
		V7,
		V8,
		V10;

		public static @Nonnull ItemSignCompatVersion version() {
			return V8;
		}
	}

	public abstract void renderSignPicture(final @Nonnull ItemSignTransformType type, final @Nonnull ItemSignCompatVersion version, final @Nullable ItemStack item);

	public final @Nonnull ModelResourceLocation modelResourceLocation = new ModelResourceLocation("minecraft:sign");
	private @Nullable IBakedModel baseModel = null;
	private @Nullable ItemStack itemStack;
	private boolean isOverride;

	public void setBaseModel(final @Nullable IBakedModel model) {
		this.baseModel = model;
	}

	@Override
	public @Nullable Pair<? extends IBakedModel, Matrix4f> handlePerspective(final @Nullable TransformType cameraTransformType) {
		Pair<? extends IBakedModel, Matrix4f> pair = null;
		final ItemStack itemStack = this.itemStack;
		if (this.baseModel instanceof IPerspectiveAwareModel)
			pair = ((IPerspectiveAwareModel) this.baseModel).handlePerspective(cameraTransformType);
		if (this.itemStack!=null&&cameraTransformType!=null&&this.isOverride) {
			OpenGL.glPushMatrix();
			if (pair!=null&&pair.getRight()!=null)
				ForgeHooksClient.multiplyCurrentGlMatrix(pair.getRight());
			OpenGL.glDisable(GL11.GL_CULL_FACE);
			renderItem(cameraTransformType, itemStack);
			// TODO
			OpenGL.glEnable(GL11.GL_LIGHTING);
			OpenGL.glEnable(GL11.GL_BLEND);
			OpenGL.glEnable(GL11.GL_TEXTURE_2D);
			OpenGL.glEnable(GL11.GL_CULL_FACE);
			OpenGL.glPopMatrix();
		}
		if (pair!=null&&this.baseModel!=null&&!this.isOverride)
			return ((IPerspectiveAwareModel) this.baseModel).handlePerspective(cameraTransformType);
		return Pair.of(this, null);
	}

	public void renderItem(final @Nonnull TransformType type, final @Nullable ItemStack item) {
		OpenGL.glPushMatrix();
		if ((type==TransformType.GROUND||type==TransformType.FIXED)&&isSeeMode()) {
			OpenGL.glPushMatrix();
			WRenderer.startTexture();
			OpenGL.glColor4f(1f, 1f, 1f, getRenderSeeOpacity());
			OpenGL.glTranslatef(-.5f, -.25f, 0.0625f/2f);
			renderSign(item);
			OpenGL.glPopMatrix();
		}
		OpenGL.glPushAttrib();
		OpenGL.glDisable(GL_CULL_FACE);
		renderSignPicture(ItemSignTransformType.fromType(type), ItemSignCompatVersion.version(), item);
		OpenGL.glPopAttrib();
		OpenGL.glPopMatrix();
	}

	@Override
	public boolean isGui3d() {
		return this.baseModel!=null&&this.baseModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return this.baseModel!=null&&this.baseModel.isBuiltInRenderer();
	}

	@Override
	public @Nullable TextureAtlasSprite getParticleTexture() {
		return this.baseModel!=null ? this.baseModel.getParticleTexture() : null;
	}

	@Override
	public @Nullable List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
		if (!this.isOverride&&this.baseModel!=null)
			return this.baseModel.getQuads(state, side, rand);
		else
			return ImmutableList.<BakedQuad> of();
	}

	@Override
	public @Nullable ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return this.baseModel!=null&&this.baseModel.isAmbientOcclusion();
	}

	private @Nonnull ItemOverrideList overrides = new ItemOverrideList(ImmutableList.<ItemOverride> of()) {
		@Override
		public @Nullable IBakedModel handleItemState(final @Nullable IBakedModel originalModel, final @Nullable ItemStack stack, final @Nullable World world, final @Nullable EntityLivingBase entity) {
			CompatItemSignRenderer.this.itemStack = stack;
			CompatItemSignRenderer.this.isOverride = isSignPicture(stack);
			final IBakedModel baseModel = CompatItemSignRenderer.this.baseModel;
			if (baseModel!=null)
				return baseModel.getOverrides().handleItemState(originalModel, stack, world, entity);
			return null;
		}
	};

	@Override
	public @Nonnull ItemOverrideList getOverrides() {
		return this.overrides;
	}

	public void registerModelBakery(final CompatModelBakeEvent event) {
		final IBakedModel object = event.getModelRegistry().getObject(this.modelResourceLocation);
		setBaseModel(object);
		event.getModelRegistry().putObject(this.modelResourceLocation, this);
	}
}