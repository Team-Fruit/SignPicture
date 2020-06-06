package net.teamfruit.signpic.render;

import net.minecraft.item.ItemStack;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.render.RenderOption;
import net.teamfruit.signpic.Config;
import net.teamfruit.signpic.attr.AttrReaders;
import net.teamfruit.signpic.attr.prop.OffsetData;
import net.teamfruit.signpic.attr.prop.RotationData.RotationGL;
import net.teamfruit.signpic.attr.prop.SizeData;
import net.teamfruit.signpic.attr.prop.SizeData.ImageSizes;
import net.teamfruit.signpic.compat.Compat.CompatItems;
import net.teamfruit.signpic.compat.CompatItemSignRenderer;
import net.teamfruit.signpic.compat.CompatVersion;
import net.teamfruit.signpic.entry.Entry;
import net.teamfruit.signpic.entry.EntryId.ItemEntryId;
import net.teamfruit.signpic.entry.content.Content;
import net.teamfruit.signpic.gui.GuiImage;
import net.teamfruit.signpic.mode.CurrentMode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomItemSignRenderer extends CompatItemSignRenderer {
    @Override
    public boolean isSeeMode() {
        return CurrentMode.instance.isState(CurrentMode.State.SEE);
    }

    @Override
    public float getRenderSeeOpacity() {
        return Config.RENDER.renderSeeOpacity.get().floatValue();
    }

    @Override
    public boolean isSignPicture(@Nullable final ItemStack item) {
        if (item == null || item.getItem() != CompatItems.SIGN)
            return false;
        return ItemEntryId.fromItemStack(item).entry().isValid();
    }

    @Override
    public void renderSignPicture(final @Nonnull ItemSignTransformType type, final @Nonnull CompatVersion version, final @Nullable ItemStack item) {
        final Entry entry = ItemEntryId.fromItemStack(item).entry();
        final AttrReaders attr = entry.getMeta();
        final Content content = entry.getContent();
        // Size
        final SizeData size01 = content != null ? content.image.getSize() : SizeData.DefaultSize;
        final SizeData size = attr.sizes.getMovie().get().aspectSize(size01);
        final GuiImage gui = entry.getGui();
        if (type == ItemSignTransformType.GUI) {
            if (version == CompatVersion.V7) {
                final float slot = 16f;
                final SizeData size2 = ImageSizes.INNER.defineSize(size, slot, slot);
                OpenGL.glTranslatef((slot - size2.getWidth()) / 2f, (slot - size2.getHeight()) / 2f, 0f);
                OpenGL.glScalef(slot, slot, 1f);
                gui.drawScreen(0, 0, 0f, 1f, size2.getWidth() / slot, size2.getHeight() / slot, new RenderOption());
            } else {
                OpenGL.glScalef(1f, -1f, 1f);
                final float slot = 1f;
                final SizeData size2 = ImageSizes.INNER.defineSize(size, slot, slot);
                if (version == CompatVersion.V8)
                    OpenGL.glScalef(.5f, .5f, 1f);
                OpenGL.glTranslatef((slot - size2.getWidth()) / 2f, (slot - size2.getHeight()) / 2f, 0f);
                OpenGL.glTranslatef(-.5f, -.5f, 0f);
                OpenGL.glScalef(slot, slot, 1f);
                gui.drawScreen(0, 0, 0f, 1f, size2.getWidth() / slot, size2.getHeight() / slot, new RenderOption());
            }
        } else if (version == CompatVersion.V7) {
            if (type == ItemSignTransformType.FIXED) {
                OpenGL.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                OpenGL.glTranslatef(0f, 0.025f, 0f);
                OpenGL.glScalef(1.6F, -1.6F, 1f);
                final float f = 0.0078125F; // vanilla map offset
                OpenGL.glTranslatef(-size.getWidth() / 2f, -.5f, f * 4);
            } else if (type == ItemSignTransformType.GROUND) {
                OpenGL.glRotatef(180f, 1f, 0f, 0f);
                OpenGL.glScalef(2f, 2f, 1f);
                OpenGL.glTranslatef(.5f, -1f, 0f);
                OpenGL.glScalef(-1f, 1f, 1f);
                OpenGL.glTranslatef(-(size.getWidth() - 1f) / 2f, .125f, 0f);
            } else {
                OpenGL.glScalef(2f, 2f, 1f);
                OpenGL.glTranslatef(.5f, 1f, 0f);
                OpenGL.glScalef(-1f, -1f, 1f);
            }
            OpenGL.glTranslatef(size.getWidth() / 2f, .5f, 0f);
            OpenGL.glScalef(1f, -1f, 1f);
            gui.renderSignPicture(1f, 1f, new RenderOption());
        } else {
            if (version == CompatVersion.V8)
                OpenGL.glScalef(1f, -1f, 1f);
            else
                OpenGL.glScalef(2f, -2f, 1f);
            if (type == ItemSignTransformType.GROUND)
                OpenGL.glTranslatef(-size.getWidth() / 2f, .25f, 0f);
            else if (type == ItemSignTransformType.FIXED) {
                final float f = 0.0078125F; // vanilla map offset
                OpenGL.glTranslatef(-size.getWidth() / 2f, .5f, f);
            } else if (type == ItemSignTransformType.FIRST_PERSON_LEFT_HAND) {
                OpenGL.glScalef(-1f, 1f, 1f);
                OpenGL.glTranslatef(.25f, .25f, 0f);
                OpenGL.glTranslatef(-size.getWidth(), 0f, 0f);
            } else if (type == ItemSignTransformType.FIRST_PERSON_RIGHT_HAND)
                OpenGL.glTranslatef(-.25f, .25f, 0f);
            else if (type == ItemSignTransformType.THIRD_PERSON_LEFT_HAND) {
                OpenGL.glTranslatef(.25f, .25f, 0f);
                OpenGL.glTranslatef(-size.getWidth(), 0f, 0f);
            } else if (type == ItemSignTransformType.THIRD_PERSON_RIGHT_HAND)
                OpenGL.glTranslatef(-.25f, .25f, 0f);
            else if (type == ItemSignTransformType.HEAD)
                OpenGL.glTranslatef(-size.getWidth() / 2f, .25f, 0f);
            OpenGL.glTranslatef(0f, -size.getHeight(), 0f);
            final OffsetData offset = attr.offsets.getMovie().get();
            OpenGL.glTranslatef(offset.x.offset, offset.y.offset, offset.z.offset);
            RotationGL.glRotate(attr.rotations.getMovie().get().getRotate());
            gui.drawScreen(0, 0, 0f, 1f, size.getWidth(), size.getHeight(), new RenderOption());
        }
    }
}