package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.image.meta.ImageSize;

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
		if (type==ItemRenderType.INVENTORY)
			OpenGL.glScalef(16f, 16f, 1f);
		else {
			if (type==ItemRenderType.ENTITY) {
				if (RenderItem.renderInFrame) {
					OpenGL.glRotatef(90f, 0f, 1f, 0f);
					OpenGL.glTranslatef(.5f, -.54f, 0f);
				} else {
					OpenGL.glRotatef(180f, 1f, 0f, 0f);
					OpenGL.glTranslatef(.5f, -1f, 0f);
				}
				OpenGL.glScalef(-1f, 1f, 1f);
			} else {
				OpenGL.glTranslatef(1f, 1f, 0f);
				OpenGL.glScalef(-1f, -1f, 1f);
			}
			// Size
			final ImageSize size = new ImageSize().setAspectSize(entry.meta.size, entry.content().image.getSize());
			if (type==ItemRenderType.ENTITY)
				OpenGL.glTranslatef(-(size.width-1f)/2f, 0f, 0f);
			OpenGL.glTranslatef(0f, 1f-size.height, 0f);
			OpenGL.glTranslatef(entry.meta.offset.x, entry.meta.offset.y, entry.meta.offset.z);
			entry.meta.rotation.rotate();
			OpenGL.glScalef(size.width, size.height, 1f);
		}
		entry.gui.drawScreen(0, 0, 0f, 1f, 1f, 1f);
		OpenGL.glPopAttrib();
		OpenGL.glPopMatrix();
	}
}