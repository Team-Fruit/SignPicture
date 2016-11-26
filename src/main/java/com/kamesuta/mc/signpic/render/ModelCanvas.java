package com.kamesuta.mc.signpic.render;

import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.TRSRTransformation;

@SuppressWarnings("deprecation")
public class ModelCanvas implements ISmartItemModel, IPerspectiveAwareModel {
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("minecraft:sign");
	private final IBakedModel baseModel;
	private ItemStack itemStack;
	private boolean isOverride;

	private CustomItemSignRenderer renderer = new CustomItemSignRenderer();

	public ModelCanvas(final IBakedModel model) {
		this.baseModel = model;
	}

	@Override
	public IBakedModel handleItemState(final ItemStack stack) {
		this.itemStack = stack;
		this.isOverride = this.renderer.handleRenderType(stack);
		return this;
	}

	@Override
	public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(final TransformType cameraTransformType) {
		final TRSRTransformation transform = new TRSRTransformation(new Vector3f(0.0F, 0.0F, 0.0F), new Quat4f(0.0F, 0.0F, 0.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), new Quat4f(0.0F, 0.0F, 0.0F, 1.0F));
		if (this.itemStack!=null&&this.isOverride) {
			OpenGL.glPushMatrix();
			OpenGL.glDisable(GL11.GL_CULL_FACE);
			this.renderer.renderItem(cameraTransformType, this.itemStack);
			OpenGL.glEnable(GL11.GL_LIGHTING);
			OpenGL.glEnable(GL11.GL_BLEND);
			OpenGL.glEnable(GL11.GL_TEXTURE_2D);
			OpenGL.glEnable(GL11.GL_CULL_FACE);
			OpenGL.glPopMatrix();
		}
		return Pair.of(this, transform.getMatrix());
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
	public TextureAtlasSprite getParticleTexture() {
		return this.baseModel.getParticleTexture();
	}

	@Override
	public List<BakedQuad> getFaceQuads(final EnumFacing facing) {
		return this.baseModel.getFaceQuads(facing);
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		return this.baseModel.getGeneralQuads();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return this.baseModel.isAmbientOcclusion();
	}

	@Override
	public VertexFormat getFormat() {
		return Attributes.DEFAULT_BAKED_FORMAT;
	}
}