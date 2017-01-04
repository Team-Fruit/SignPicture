package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.RotationData.RotationGL;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class CustomItemSignRenderer implements IItemRenderer {
	@Override
	public boolean handleRenderType(final @Nullable ItemStack item, final @Nullable ItemRenderType type) {
		if (item!=null&&item.getItem()!=Items.sign)
			return false;
		return EntryId.fromItemStack(item).entry().isValid();
	}

	@Override
	public boolean shouldUseRenderHelper(final @Nullable ItemRenderType type, final @Nullable ItemStack item, final @Nullable ItemRendererHelper helper) {
		return type==ItemRenderType.ENTITY;
	}

	@Override
	public void renderItem(final @Nullable ItemRenderType type, final @Nullable ItemStack item, final @Nullable Object... data) {
		OpenGL.glPushMatrix();
		OpenGL.glPushAttrib();
		OpenGL.glDisable(GL_CULL_FACE);
		final Entry entry = EntryId.fromItemStack(item).entry();
		final CompoundAttr attr = entry.getMeta();
		final Content content = entry.getContent();
		// Size
		final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
		final SizeData size = attr.sizes.getMovie().get().aspectSize(size01);
		if (type==ItemRenderType.INVENTORY) {
			final float slot = 16f;
			final SizeData size2 = ImageSizes.INNER.defineSize(size, slot, slot);
			OpenGL.glTranslatef((slot-size2.getWidth())/2f, (slot-size2.getHeight())/2f, 0f);
			OpenGL.glScalef(slot, slot, 1f);
			entry.gui.drawScreen(0, 0, 0f, 1f, size2.getWidth()/slot, size2.getHeight()/slot);
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
			OpenGL.glTranslatef(0f, 1f-size.getHeight(), 0f);
			final OffsetData offset = attr.offsets.getMovie().get();
			OpenGL.glTranslatef(offset.x.offset, -offset.y.offset, offset.z.offset);
			RotationGL.glRotate(attr.rotations.getMovie().get().getRotate());
			entry.gui.drawScreen(0, 0, 0f, 1f, size.getWidth(), size.getHeight());
		}
		OpenGL.glPopAttrib();
		OpenGL.glPopMatrix();
	}
}