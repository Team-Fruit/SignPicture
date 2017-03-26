package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId.ItemEntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.gui.GuiImage;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class CustomItemSignRenderer implements IItemRenderer {
	@Override
	public boolean handleRenderType(final @Nullable ItemStack item, final @Nullable ItemRenderType type) {
		if (item!=null&&item.getItem()!=Items.sign)
			return false;
		return ItemEntryId.fromItemStack(item).entry().isValid();
	}

	@Override
	public boolean shouldUseRenderHelper(final @Nullable ItemRenderType type, final @Nullable ItemStack item, final @Nullable ItemRendererHelper helper) {
		return type==ItemRenderType.ENTITY;
	}

	@Override
	public void renderItem(final @Nullable ItemRenderType type, final @Nullable ItemStack item, final @Nullable Object... data) {
		OpenGL.glPushMatrix();
		if (type==ItemRenderType.ENTITY&&CurrentMode.instance.isState(CurrentMode.State.SEE)) {
			OpenGL.glPushMatrix();
			WRenderer.startTexture();
			OpenGL.glColor4f(1f, 1f, 1f, Config.getConfig().renderSeeOpacity.get().floatValue());
			OpenGL.glTranslatef(-.5f, -.25f, 0.0625f/2f);
			renderSign();
			OpenGL.glPopMatrix();
		}
		OpenGL.glPushAttrib();
		OpenGL.glDisable(GL_CULL_FACE);
		final Entry entry = ItemEntryId.fromItemStack(item).entry();
		final AttrReaders attr = entry.getMeta();
		final Content content = entry.getContent();
		// Size
		final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
		final SizeData size = attr.sizes.getMovie().get().aspectSize(size01);
		final GuiImage gui = entry.getGui();
		if (type==ItemRenderType.INVENTORY) {
			final float slot = 16f;
			final SizeData size2 = ImageSizes.INNER.defineSize(size, slot, slot);
			OpenGL.glTranslatef((slot-size2.getWidth())/2f, (slot-size2.getHeight())/2f, 0f);
			OpenGL.glScalef(slot, slot, 1f);
			gui.drawScreen(0, 0, 0f, 1f, size2.getWidth()/slot, size2.getHeight()/slot, new RenderOption());
		} else {
			if (type==ItemRenderType.ENTITY) {
				if (RenderItem.renderInFrame) {
					OpenGL.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					OpenGL.glTranslatef(0f, 0.025f, 0f);
					OpenGL.glScalef(1.6F, -1.6F, 1f);
					final float f = 0.0078125F; // vanilla map offset
					OpenGL.glTranslatef(-size.getWidth()/2f, -.5f, f*4);
				} else {
					OpenGL.glRotatef(180f, 1f, 0f, 0f);
					OpenGL.glScalef(2f, 2f, 1f);
					OpenGL.glTranslatef(.5f, -1f, 0f);
					OpenGL.glScalef(-1f, 1f, 1f);
					OpenGL.glTranslatef(-(size.getWidth()-1f)/2f, .125f, 0f);
				}
			} else {
				OpenGL.glScalef(2f, 2f, 1f);
				OpenGL.glTranslatef(.5f, 1f, 0f);
				OpenGL.glScalef(-1f, -1f, 1f);
			}
			OpenGL.glTranslatef(size.getWidth()/2f, .5f, 0f);
			OpenGL.glScalef(1f, -1f, 1f);
			gui.renderSignPicture(1f, 1f, new RenderOption());
			/*
			OpenGL.glTranslatef(0f, 1f-size.getHeight(), 0f);
			final OffsetData offset = attr.offsets.getMovie().get();
			OpenGL.glTranslatef(offset.x.offset, -offset.y.offset, offset.z.offset);
			RotationGL.glRotate(attr.rotations.getMovie().get().getRotate());
			entry.gui.drawScreen(0, 0, 0f, 1f, size.getWidth(), size.getHeight());
			*/
		}
		OpenGL.glPopAttrib();
		OpenGL.glPopMatrix();
	}

	private void renderSign() {
		final IIcon iicon = Items.sign.getIconFromDamage(0);
		TextureUtil.func_152777_a(false, false, 1f);
		final float f = iicon.getMinU();
		final float f1 = iicon.getMaxU();
		final float f2 = iicon.getMinV();
		final float f3 = iicon.getMaxV();
		ItemRenderer.renderItemIn2D(WRenderer.t, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625f);
	}
}