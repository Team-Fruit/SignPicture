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
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.render.OpenGL;
import com.kamesuta.mc.signpic.render.RenderHelper;

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
			final Entry entry = entryId.entry();
			if (entry!=null&&entry.isValid()) {
				final Content content = entry.content();
				@Nullable
				Entry upentry = null;
				if (this.update!=null)
					upentry = this.update.entry();

				if (!StringUtils.isEmpty(content.id.id()))
					drawEntry(a, opacity, entry);
				else if (upentry!=null&&upentry.isValid())
					drawEntry(a, opacity, upentry);
				else {
					RenderHelper.startTexture();
					OpenGL.glColor4f(1f, 1f, 1f, .2f);
					texture().bindTexture(defaultTexture);
					final ImageSize size1 = new ImageSize().setSize(ImageSizes.INNER, new ImageSize().setSize(1, 1), new ImageSize().setArea(a));
					drawTexture(new Area(a.x1()+a.w()/2-size1.width/2, a.y1()+a.h()/2-size1.height/2, a.x1()+a.w()/2+size1.width/2, a.y1()+a.h()/2+size1.height/2));
				}
			}
		}
	}

	public static void drawEntry(final Area a, final float opacity, @Nonnull final Entry entry) {
		OpenGL.glDisable(GL_CULL_FACE);
		OpenGL.glPushMatrix();

		final ImageSize size1 = new ImageSize().setAspectSize(entry.getMeta().size, entry.content().image.getSize());
		final ImageSize size2 = new ImageSize().setSize(ImageSizes.INNER, size1, new ImageSize().setArea(a));
		final ImageSize size = new ImageSize().setImageSize(size2).scale(1f/100f);

		OpenGL.glTranslatef(a.x1(), a.y1(), 0f);
		OpenGL.glTranslatef((a.w()-size2.width)/2f, (a.h()-size2.height)/2f, 0f);
		OpenGL.glScalef(100, 100, 1f);
		entry.gui.drawScreen(0, 0, 0, opacity, size.width, size.height);

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
