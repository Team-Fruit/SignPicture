package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@SuppressWarnings("deprecation")
public class CustomItemSignRenderer {
	public boolean handleRenderType(final ItemStack item) {
		if (item.getItem()!=Items.sign)
			return false;
		return EntryId.fromItemStack(item).entry().isValid();
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
}