package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.attr.prop.SizeData.ImageSizes;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.information.Informations;

import net.minecraft.util.ResourceLocation;

public class SignPicLabel extends WBase {
	public static final @Nonnull ResourceLocation defaultTexture = new ResourceLocation("signpic", "textures/logo.png");
	protected @Nullable EntryId entryId;
	protected @Nonnull ContentManager manager;
	protected @Nullable EntryId update;

	public SignPicLabel(final @Nonnull R position, final @Nonnull ContentManager manager) {
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
	public void draw(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final float frame, final float popacity, final @Nonnull RenderOption opt) {
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

				if (!StringUtils.isEmpty(content.id.getID()))
					drawEntry(a, opacity, entry, opt);
				else if (upentry!=null&&upentry.isValid())
					drawEntry(a, opacity, upentry, opt);
				else {
					WRenderer.startTexture();
					OpenGL.glColor4f(1f, 1f, 1f, .2f);
					texture().bindTexture(defaultTexture);
					final SizeData size1 = ImageSizes.INNER.defineSize(SizeData.create(1, 1), SizeData.create(a));
					drawTexture(Area.abs(a.x1()+a.w()/2-size1.getWidth()/2, a.y1()+a.h()/2-size1.getHeight()/2, a.x1()+a.w()/2+size1.getWidth()/2, a.y1()+a.h()/2+size1.getHeight()/2), null, null);
				}
			}
		}
	}

	public static void drawEntry(final @Nonnull Area a, final float opacity, final @Nonnull Entry entry, @Nonnull final RenderOption opt) {
		OpenGL.glDisable(GL_CULL_FACE);
		OpenGL.glPushMatrix();

		final AttrReaders attr = entry.getMeta();
		final Content content = entry.getContent();
		final SizeData size00 = attr.sizes.getMovie().get();
		final SizeData size01 = content!=null ? content.image.getSize() : SizeData.DefaultSize;
		final SizeData size1 = size00.aspectSize(size01);
		final SizeData size2 = ImageSizes.INNER.defineSize(size1, SizeData.create(a));
		final SizeData size = size2.scale(1f/100f);

		OpenGL.glTranslatef(a.x1(), a.y1(), 0f);
		OpenGL.glTranslatef((a.w()-size2.getWidth())/2f, (a.h()-size2.getHeight())/2f, 0f);
		OpenGL.glScalef(size.getWidth()<0 ? -1f : 1f, size.getHeight()<0 ? -1f : 1f, 1f);
		OpenGL.glScalef(100, 100, 1f);
		entry.getGui().drawScreen(0, 0, 0, opacity, size.getWidth(), size.getHeight(), opt);

		OpenGL.glPopMatrix();
		OpenGL.glEnable(GL_CULL_FACE);
	}

	public @Nullable EntryId getEntryId() {
		return this.entryId;
	}

	public @Nonnull SignPicLabel setEntryId(final @Nullable EntryId entryId) {
		this.entryId = entryId;
		return this;
	}
}
