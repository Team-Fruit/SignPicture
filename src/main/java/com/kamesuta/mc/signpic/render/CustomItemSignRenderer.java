package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;
import com.kamesuta.mc.signpic.image.meta.OffsetData;
import com.kamesuta.mc.signpic.image.meta.SizeData;
import com.kamesuta.mc.signpic.image.meta.SizeData.ImageSizes;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class CustomItemSignRenderer implements IItemRenderer {
	@Override
	public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
		if (item.getItem()!=Items.sign)
			return false;
		return EntryId.fromItemStack(item).entry().isValid();
	}

	@Override
	public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item, final ItemRendererHelper helper) {
		return type==ItemRenderType.ENTITY;
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		OpenGL.glPushMatrix();
		OpenGL.glPushAttrib();
		OpenGL.glDisable(GL_CULL_FACE);
		final Entry entry = EntryId.fromItemStack(item).entry();
		final ImageMeta meta = entry.getMeta();
		// Size
		final SizeData size = SizeData.aspectSize(meta.getSize(), entry.content().image.getSize());
		if (type==ItemRenderType.INVENTORY) {
			final float slot = 16f;
			final SizeData size2 = ImageSizes.INNER.defineSize(size, slot, slot);
			OpenGL.glTranslatef((slot-size2.width)/2f, (slot-size2.height)/2f, 0f);
			OpenGL.glScalef(slot, slot, 1f);
			entry.gui.drawScreen(0, 0, 0f, 1f, size2.width/slot, size2.height/slot);
		} else {
			if (type==ItemRenderType.ENTITY) {
				if (RenderItem.renderInFrame) {
					OpenGL.glRotatef(90f, 0f, 1f, 0f);
					OpenGL.glScalef(-1f, -1f, 1f);
					OpenGL.glTranslatef(.5f, -.62f, 0f);
				} else {
					OpenGL.glRotatef(180f, 1f, 0f, 0f);
					OpenGL.glScalef(2f, 2f, 1f);
					OpenGL.glTranslatef(.5f, -1f, 0f);
				}
				OpenGL.glScalef(-1f, 1f, 1f);
				OpenGL.glTranslatef(-(size.width-1f)/2f, .125f, 0f);
			} else {
				OpenGL.glScalef(2f, 2f, 1f);
				OpenGL.glTranslatef(.5f, 1f, 0f);
				OpenGL.glScalef(-1f, -1f, 1f);
			}
			OpenGL.glTranslatef(0f, 1f-size.height, 0f);
			final OffsetData offset = meta.getOffset();
			OpenGL.glTranslatef(offset.x, offset.y, offset.z);
			meta.getRotation().rotate();
			entry.gui.drawScreen(0, 0, 0f, 1f, size.width, size.height);
		}
		OpenGL.glPopAttrib();
		OpenGL.glPopMatrix();
	}
}