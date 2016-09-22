package com.kamesuta.mc.signpic.gui;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.image.meta.ImageSize.ImageSizes;

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
			final Entry entry = entrtId.entry();
			if (entry.isValid()) {
				final Content content = entry.content();
				if (content != null) {
					glDisable(GL_CULL_FACE);
					glPushMatrix();

					final ImageSize size1 = new ImageSize().setAspectSize(entry.meta.size, content.image.getSize());
					final ImageSize size2 = new ImageSize().setSize(ImageSizes.INNER, size1, new ImageSize().setArea(a));
					final ImageSize size = new ImageSize().setImageSize(size2).scale(1f/50f);

					translate(a);
					glTranslatef((a.w()-size2.width)/2f, (a.h()-size2.height)/2f, 0f);
					glScalef(50, 50, 1f);
					Client.renderer.renderImage(content, size, 1f);

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
