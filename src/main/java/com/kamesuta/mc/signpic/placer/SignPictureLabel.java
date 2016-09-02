package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.R;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.ImageSize;
import com.kamesuta.mc.signpic.image.ImageSizes;
import com.kamesuta.mc.signpic.util.Sign;

public class SignPictureLabel extends WBase {
	protected Sign sign;
	protected ImageManager manager;

	public SignPictureLabel(final R position, final ImageManager manager) {
		super(position);
		this.manager = manager;
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		final Area a = getGuiPosition(pgp);
		glEnable(GL_BLEND);
		glColor4f(0f, 0f, 0f, 0.3f);
		glDisable(GL_TEXTURE_2D);
		draw(a, GL_QUADS);
		glEnable(GL_TEXTURE_2D);
		final Sign s = getSign();
		if (s != null && s.id()!=null) {
			final Image image = this.manager.get(this.sign.id());
			if (image != null) {
				glDisable(GL_CULL_FACE);
				glPushMatrix();
				translate(a);

				final ImageSize siz = image.getSize().getLimitSize(s.size());
				final ImageSize size = ImageSize.createSize(ImageSizes.INNER, siz, new ImageSize(a));

				glPushMatrix();
				glTranslatef((a.w()-size.width)/2, (a.h()-size.height)/2, 0);
				glScalef(size.width, size.height, 1f);
				image.getState().mainImage(this.manager, image);
				glPopMatrix();

				glPushMatrix();
				glTranslatef(a.w()/2, a.h()/2, 0);
				glScalef(size.width, size.height, 1f);
				glScalef(25f, 25f, 1f);
				image.getState().themeImage(this.manager, image);
				image.getState().message(this.manager, image, font);
				glPopMatrix();

				glPopMatrix();
				glEnable(GL_CULL_FACE);
			}
		}
	}

	public Sign getSign() {
		return this.sign;
	}

	public SignPictureLabel setSign(final Sign sign) {
		this.sign = sign;
		return this;
	}
}
