package com.kamesuta.mc.signpic.render;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
	ArrayList<String> modelParts = Lists.newArrayList();
	private final IBakedModel baseModel;
	ItemStack itemStack;

	public ModelCanvas(final IBakedModel model) {
		this.baseModel = model;
	}

	@Override
	public IBakedModel handleItemState(final ItemStack stack) {
		this.itemStack = stack;
		return this;
	}

	@Override
	public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(final TransformType cameraTransformType) {
		final TRSRTransformation transform = new TRSRTransformation(new Vector3f(0.0F, 0.0F, 0.0F), new Quat4f(0.0F, 0.0F, 0.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), new Quat4f(0.0F, 0.0F, 0.0F, 1.0F));
		OpenGL.glPushMatrix();
		OpenGL.glDisable(GL11.GL_CULL_FACE);
		OpenGL.glDisable(GL11.GL_TEXTURE_2D);
		OpenGL.glColor4f(1f, 0f, 0f, 1f);
		RenderHelper.w.begin(7, DefaultVertexFormats.POSITION);
		RenderHelper.w.pos(0, 0, 0).endVertex();
		RenderHelper.w.pos(0, 1, 0).endVertex();
		RenderHelper.w.pos(1, 1, 0).endVertex();
		RenderHelper.w.pos(1, 0, 0).endVertex();
		RenderHelper.t.draw();
		OpenGL.glEnable(GL11.GL_TEXTURE_2D);
		OpenGL.glEnable(GL11.GL_CULL_FACE);
		OpenGL.glPopMatrix();

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
		return true;
	}

	@Override
	public VertexFormat getFormat() {
		return Attributes.DEFAULT_BAKED_FORMAT;
	}
}