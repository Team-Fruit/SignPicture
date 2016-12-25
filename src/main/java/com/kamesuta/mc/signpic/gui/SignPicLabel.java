package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WRenderer;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.render.OpenGL;

import net.minecraft.util.ResourceLocation;

public class SignPicLabel extends WBase {
	public static final ResourceLocation defaultTexture = new ResourceLocation("signpic", "textures/logo.png");
	protected EntryId entryId;
	protected ContentManager manager;
	protected EntryId update;

	public SignPicLabel(final R position, final ContentManager manager) {
		super(position);
		this.manager = manager;
		if (Informations.instance.isUpdateRequired()) {
			final String image = Informations.instance.getUpdateImage();
			if (image!=null) {
				final EntryIdBuilder eidb = new EntryIdBuilder();
				eidb.setURI(image);
				this.update = eidb.build();
			}
		}
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
		final Area a = getGuiPosition(pgp);
		final float opacity = getGuiOpacity(popacity);
		final EntryId entryId = getEntryId();
		if (entryId!=null) {
			final @Nonnull Entry entry = entryId.entry();
			@Nullable
			Content content = null;
			if (entry.isValid()&&(content = entry.getContent())!=null) {
				@Nullable
				Entry upentry = null;
				if (this.update!=null)
					upentry = this.update.entry();

				if (!StringUtils.isEmpty(content.id.id()))
					drawEntry(a, opacity, entry);
				else if (upentry!=null&&upentry.isValid())
					drawEntry(a, opacity, upentry);
				else {
					WRenderer.startTexture();
					OpenGL.glColor4f(1f, 1f, 1f, .2f);
					texture().bindTexture(defaultTexture);
					final SizeData size1 = ImageSizes.INNER.defineSize(SizeData.create(1, 1), SizeData.create(a));
					drawTexture(new Area(a.x1()+a.w()/2-size1.getWidth()/2, a.y1()+a.h()/2-size1.getHeight()/2, a.x1()+a.w()/2+size1.getWidth()/2, a.y1()+a.h()/2+size1.getHeight()/2));
				}
			}
		}
	}

	public static void drawEntry(final Area a, final float opacity, @Nonnull final Entry entry) {
		OpenGL.glDisable(GL_CULL_FACE);
		OpenGL.glPushMatrix();

		final CompoundAttr attr = entry.getMeta();
		final Content content = entry.getContent();
		final SizeData size00 = attr!=null ? attr.sizes.getMovie().get() : SizeData.DefaultSize;
		final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
		final SizeData size1 = size00.aspectSize(size01);
		final SizeData size2 = ImageSizes.INNER.defineSize(size1, SizeData.create(a));
		final SizeData size = size2.scale(1f/100f);

		OpenGL.glTranslatef(a.x1(), a.y1(), 0f);
		OpenGL.glTranslatef((a.w()-size2.getWidth())/2f, (a.h()-size2.getHeight())/2f, 0f);
		OpenGL.glScalef(100, 100, 1f);
		entry.gui.drawScreen(0, 0, 0, opacity, size.getWidth(), size.getHeight());

		OpenGL.glPopMatrix();
		OpenGL.glEnable(GL_CULL_FACE);
	}

	public EntryId getEntryId() {
		return this.entryId;
	}

	public SignPicLabel setEntryId(final EntryId entryId) {
		this.entryId = entryId;
		return this;
	}
}
