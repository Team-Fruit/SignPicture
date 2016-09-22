package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryManager;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;
import com.kamesuta.mc.signpic.render.RenderHelper;

public class SignPicLabel extends WBase {
	protected EntryId entryId;
	protected ContentManager manager;

	public SignPicLabel(final R position, final ContentManager manager) {
		super(position);
		this.manager = manager;
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area a = getGuiPosition(pgp);
		final EntryId entrtId = getEntryId();
		if (entrtId != null) {
			final Entry entry = EntryManager.instance.get(entrtId);
			if (entry.isValid()) {
				final Content content = entry.content();
				if (content != null) {
					RenderHelper.startTexture();
					glDisable(GL_CULL_FACE);
					glPushMatrix();
					translate(a);

					final ImageSize siz = new ImageSize().setAspectSize(entry.meta.size, content.image.getSize());
					final ImageSize size = new ImageSize().setSize(ImageSizes.INNER, siz, new ImageSize().setArea(a));

					glPushMatrix();
					glTranslatef((a.w()-size.width)/2, (a.h()-size.height)/2, 0);
					glScalef(size.width, size.height, 1f);
					//content.state.mainImage(this.manager, content.image);
					glPopMatrix();

					glPushMatrix();
					glTranslatef(a.w()/2, a.h()/2, 0);
					//glScalef(size.width, size.height, 1f);
					glScalef(25f, 25f, 1f);
					//content.state.themeImage(this.manager, content.image);
					//content.state.message(this.manager, content.image, font());
					glPopMatrix();

					glPopMatrix();
					glEnable(GL_CULL_FACE);
				}
			}
		}
	}

	public EntryId getEntryId() {
		return this.entryId;
	}

	public SignPicLabel setEntryId(final EntryId entryId) {
		this.entryId = entryId;
		return this;
	}
}
