package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationGL;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;

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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

public class CustomItemSignRenderer implements IBakedModel {
	public static final @Nonnull ModelResourceLocation modelResourceLocation = new ModelResourceLocation("minecraft:sign");
	private final @Nonnull IBakedModel baseModel;
	private @Nullable ItemStack itemStack;
	private boolean isOverride;

	public CustomItemSignRenderer(final @Nonnull IBakedModel model) {
		this.baseModel = model;
	}

	@Override
	public @Nullable Pair<? extends IBakedModel, Matrix4f> handlePerspective(final @Nullable TransformType cameraTransformType) {
		final Pair<? extends IBakedModel, Matrix4f> pair = this.baseModel.handlePerspective(cameraTransformType);
		if (this.itemStack!=null&&cameraTransformType!=null&&this.isOverride) {
			OpenGL.glPushMatrix();
			if (pair.getRight()!=null)
				ForgeHooksClient.multiplyCurrentGlMatrix(pair.getRight());
			OpenGL.glDisable(GL11.GL_CULL_FACE);
			renderItem(cameraTransformType, this.itemStack);
			OpenGL.glEnable(GL11.GL_LIGHTING);
			OpenGL.glEnable(GL11.GL_BLEND);
			OpenGL.glEnable(GL11.GL_TEXTURE_2D);
			OpenGL.glEnable(GL11.GL_CULL_FACE);
			OpenGL.glPopMatrix();
		}
		if (!this.isOverride)
			return this.baseModel.handlePerspective(cameraTransformType);
		return Pair.of(this, null);
	}

	public void renderItem(final @Nonnull TransformType type, final @Nullable ItemStack item) {
		OpenGL.glPushMatrix();
		OpenGL.glPushAttrib();
		OpenGL.glDisable(GL_CULL_FACE);
		final Entry entry = EntryId.fromItemStack(item).entry();
		final AttrReaders attr = entry.getMeta();
		final Content content = entry.getContent();
		// Size
		final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
		final SizeData size = attr.sizes.getMovie().get().aspectSize(size01);
		OpenGL.glScalef(1f, -1f, 1f);
		if (type==TransformType.GUI) {
			final float slot = 1f;
			final SizeData size2 = SizeData.ImageSizes.INNER.defineSize(size, slot, slot);
			//OpenGL.glScalef(.5f, .5f, 1f);
			OpenGL.glTranslatef((slot-size2.getWidth())/2f, (slot-size2.getHeight())/2f, 0f);
			OpenGL.glTranslatef(-.5f, -.5f, 0f);
			OpenGL.glScalef(slot, slot, 1f);
			entry.getGui().drawScreen(0, 0, 0f, 1f, size2.getWidth()/slot, size2.getHeight()/slot, new RenderOption());
		} else {
			OpenGL.glScalef(2f, 2f, 1f);
			if (type==TransformType.GROUND)
				OpenGL.glTranslatef(-size.getWidth()/2f, .25f, 0f);
			else if (type==TransformType.FIXED) {
				final float f = 0.0078125F; // vanilla map offset
				OpenGL.glTranslatef(-size.getWidth()/2f, .5f, f);
			} else if (type==TransformType.FIRST_PERSON_LEFT_HAND) {
				OpenGL.glScalef(-1f, 1f, 1f);
				OpenGL.glTranslatef(.25f, .25f, 0f);
				OpenGL.glTranslatef(-size.getWidth(), 0f, 0f);
			} else if (type==TransformType.FIRST_PERSON_RIGHT_HAND)
				OpenGL.glTranslatef(-.25f, .25f, 0f);
			else if (type==TransformType.THIRD_PERSON_LEFT_HAND) {
				OpenGL.glTranslatef(.25f, .25f, 0f);
				OpenGL.glTranslatef(-size.getWidth(), 0f, 0f);
			} else if (type==TransformType.THIRD_PERSON_RIGHT_HAND)
				OpenGL.glTranslatef(-.25f, .25f, 0f);
			else if (type==TransformType.HEAD)
				OpenGL.glTranslatef(-size.getWidth()/2f, .25f, 0f);
			OpenGL.glTranslatef(0f, -size.getHeight(), 0f);
			final OffsetData offset = attr.offsets.getMovie().get();
			OpenGL.glTranslatef(offset.x.offset, offset.y.offset, offset.z.offset);
			RotationGL.glRotate(attr.rotations.getMovie().get().getRotate());
			entry.getGui().drawScreen(0, 0, 0f, 1f, size.getWidth(), size.getHeight(), new RenderOption());
		}
		OpenGL.glPopAttrib();
		OpenGL.glPopMatrix();
	}

	@Override
	public boolean isGui3d() {
		return this.baseModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return this.baseModel.isBuiltInRenderer();
	}

	@Override
	public @Nullable TextureAtlasSprite getParticleTexture() {
		return this.baseModel.getParticleTexture();
	}

	@SuppressWarnings("deprecation")
	@Override
	public @Nullable ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return this.baseModel.isAmbientOcclusion();
	}

	@Override
	public @Nullable List<BakedQuad> getQuads(final @Nullable IBlockState state, final @Nullable EnumFacing side, final long rand) {
		if (this.isOverride)
			return ImmutableList.<BakedQuad> of();
		else
			return this.baseModel.getQuads(state, side, rand);
	}

	private @Nonnull final ItemOverrideList overrides = new ItemOverrideList(ImmutableList.<ItemOverride> of()) {
		@Override
		public @Nullable IBakedModel handleItemState(final @Nullable IBakedModel originalModel, final @Nullable ItemStack stack, final @Nullable World world, final @Nullable EntityLivingBase entity) {
			CustomItemSignRenderer.this.itemStack = stack;
			CustomItemSignRenderer.this.isOverride = stack!=null&&stack.getItem()==Items.SIGN&&EntryId.fromItemStack(stack).entry().isValid();
			return CustomItemSignRenderer.this.baseModel.getOverrides().handleItemState(originalModel, stack, world, entity);
		}
	};

	@Override
	public @Nonnull ItemOverrideList getOverrides() {
		return this.overrides;
	}

}