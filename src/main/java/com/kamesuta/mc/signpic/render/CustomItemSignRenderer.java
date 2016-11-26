package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.TRSRTransformation;

@SuppressWarnings("deprecation")
public class CustomItemSignRenderer implements ISmartItemModel, IPerspectiveAwareModel {
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("minecraft:sign");
	private final IBakedModel baseModel;
	private ItemStack itemStack;
	private boolean isOverride;

	public CustomItemSignRenderer(final IBakedModel model) {
		this.baseModel = model;
	}

	@Override
	public IBakedModel handleItemState(final ItemStack stack) {
		this.itemStack = stack;
		this.isOverride = stack.getItem()==Items.sign&&EntryId.fromItemStack(stack).entry().isValid();
		return this;
	}

	@Override
	public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(final TransformType cameraTransformType) {
		final TRSRTransformation transform = new TRSRTransformation(new Vector3f(0.0F, 0.0F, 0.0F), new Quat4f(0.0F, 0.0F, 0.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F), new Quat4f(0.0F, 0.0F, 0.0F, 1.0F));
		if (this.itemStack!=null&&this.isOverride) {
			OpenGL.glPushMatrix();
			OpenGL.glDisable(GL11.GL_CULL_FACE);
			renderItem(cameraTransformType, this.itemStack);
			OpenGL.glEnable(GL11.GL_LIGHTING);
			OpenGL.glEnable(GL11.GL_BLEND);
			OpenGL.glEnable(GL11.GL_TEXTURE_2D);
			OpenGL.glEnable(GL11.GL_CULL_FACE);
			OpenGL.glPopMatrix();
		}
		return Pair.of(this, transform.getMatrix());
	}

	public void renderItem(final TransformType type, final ItemStack item) {
		OpenGL.glPushMatrix();
		OpenGL.glPushAttrib();
		OpenGL.glDisable(GL_CULL_FACE);
		final Entry entry = EntryId.fromItemStack(item).entry();
		// Size
		final ImageSize size = new ImageSize().setAspectSize(entry.meta.size, entry.content().image.getSize());
		if (type==TransformType.GUI) {
			final float slot = 1f;
			final ImageSize size2 = new ImageSize().setSize(ImageSizes.INNER, size, slot, slot);
			OpenGL.glTranslatef((slot-size2.width)/2f, (slot-size2.height)/2f, 0f);
			OpenGL.glScalef(slot, slot, 1f);
			entry.gui.drawScreen(0, 0, 0f, 1f, size2.width/slot, size2.height/slot);
		} else {
			OpenGL.glScalef(2f, 2f, 1f);
			if (type==TransformType.GROUND||type==TransformType.FIXED) {
				if (type==TransformType.FIXED) {
					OpenGL.glRotatef(90f, 0f, 1f, 0f);
					OpenGL.glTranslatef(.5f, -.54f, 0f);
				} else {
					OpenGL.glRotatef(180f, 1f, 0f, 0f);
					OpenGL.glTranslatef(.5f, -1f, 0f);
				}
				OpenGL.glScalef(-1f, 1f, 1f);
				OpenGL.glTranslatef(-(size.width-1f)/2f, 0f, 0f);
			} else {
				OpenGL.glTranslatef(1f, 1f, 0f);
				OpenGL.glScalef(-1f, -1f, 1f);
			}
			OpenGL.glTranslatef(0f, 1f-size.height, 0f);
			OpenGL.glTranslatef(entry.meta.offset.x, entry.meta.offset.y, entry.meta.offset.z);
			entry.meta.rotation.rotate();
			// OpenGL.glScalef(size.width, size.height, 1f);
			entry.gui.drawScreen(0, 0, 0f, 1f, size.width, size.height);
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
	public TextureAtlasSprite getParticleTexture() {
		// Client.mc.getTextureMapBlocks().getMissingSprite();
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