package com.kamesuta.mc.signpic.placer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.guiwidget.WBase;
import com.kamesuta.mc.guiwidget.WEvent;
import com.kamesuta.mc.guiwidget.WFrame;
import com.kamesuta.mc.guiwidget.animation.Easings;
import com.kamesuta.mc.guiwidget.component.MButton;
import com.kamesuta.mc.guiwidget.component.MTextField;
import com.kamesuta.mc.guiwidget.position.Area;
import com.kamesuta.mc.guiwidget.position.Coord;
import com.kamesuta.mc.guiwidget.position.Point;
import com.kamesuta.mc.guiwidget.position.RArea;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.ImageSize;
import com.kamesuta.mc.signpic.image.ImageSizes;

public class GuiSignPicture extends WFrame {
	protected String url = "";
	protected String[] signurl = new String[4];
	protected ImageManager manager;

	public GuiSignPicture(final ImageManager manager) {
		this.manager = manager;
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void initWidgets() {
		add(new WBase(RArea.diff(0, 0, 0, 0)) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				glColor4f(0f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(getGuiPosition(pgp), GL_QUADS);
				glEnable(GL_TEXTURE_2D);
			}
		});
		add(new WBase(new RArea(Coord.left(5), Coord.bottom(150), Coord.left(135), Coord.bottom(20))) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
				final Area a = getGuiPosition(pgp);
				glColor4f(0f, 0f, 0f, 0.3f);
				glDisable(GL_TEXTURE_2D);
				draw(a, GL_QUADS);
				glEnable(GL_TEXTURE_2D);
				final Image image = GuiSignPicture.this.manager.get(GuiSignPicture.this.url);
				if (image != null) {
					glPushMatrix();
					translate(a);

					final ImageSize size = ImageSize.createSize(ImageSizes.LIMIT, image.getSize(), new ImageSize(a));

					glPushMatrix();
					glTranslatef((a.w()-size.width)/2, (a.h()-size.height)/2, 0);
					glScalef(size.width, size.height, 1f);
					image.getState().mainImage(GuiSignPicture.this.manager, image);
					glPopMatrix();

					glPushMatrix();
					glTranslatef(a.w()/2, a.h()/2, 0);
					glScalef(size.width, size.height, 1f);
					glScalef(25f, 25f, 1f);
					image.getState().themeImage(GuiSignPicture.this.manager, image);
					image.getState().message(GuiSignPicture.this.manager, image, font);
					glPopMatrix();

					glPopMatrix();
				}
			}
		});
		add(new MTextField(new RArea(Coord.left(5), Coord.bottom(20), Coord.right(5), Coord.bottom(5)), "aaaa") {
			@Override
			public void onFocusChanged() {
				super.onFocusChanged();
				final String url = getText();
				GuiSignPicture.this.url = url;
				for (int i=0; i<4; i++) {
					if (16*i <= url.length())
						GuiSignPicture.this.signurl[i] = url.substring(16*i, Math.min(16*i+15, url.length()));
				}
			}
		});
		//add(new MButton(new LRArea(5, -21, 30, -6, true), "aaaa") {
		final Coord b = Coord.bottom(0)
				.add(Easings.easeInOutQuart.move(2, 10))
				.add(Easings.easeInCirc.move(3, 40))
				.add(Easings.easeInOutQuart.move(1, 100));
		final Coord c = Coord.left(0)
				.add(Easings.easeInOutQuart.move(2, 10))
				.add(Easings.easeInCirc.move(3, 40))
				.add(Easings.easeInOutQuart.move(1, 100));
		add(new MButton(new RArea(b, c.addAfter(b.motion).start(), Coord.width(20), Coord.height(20)), "aaaa") {
			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point p, final int button) {
				c.motion.stop().add(Easings.easeOutCirc.move(2f, c.motion.getLast() + 30));
				return true;
			}
		});
		//		add(new WBase(RArea.diff(0, 0, 0, 0)) {
		//			@Override
		//			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		//				final Area a = new Area(75, 75, 150, 150);
		//				final Area b = new Area(85, 85, 160, 160);
		//				clip.clipArea(b);
		//				glColor4f(0f, 0f, 1f, 0.3f);
		//				glDisable(GL_TEXTURE_2D);
		//				draw(pgp, GL_QUADS);
		//				glEnable(GL_TEXTURE_2D);
		//				clip.end();
		//				clip.clipArea(a);
		//				glColor4f(1f, 0f, 0f, 0.3f);
		//				glDisable(GL_TEXTURE_2D);
		//				draw(pgp, GL_QUADS);
		//				glEnable(GL_TEXTURE_2D);
		//				clip.end();
		//
		//				glDisable(GL_TEXTURE_2D);
		//				glColor4f(1f, 0f, 0f, 1f);
		//				draw(a, GL_LINE_LOOP);
		//				draw(b, GL_LINE_LOOP);
		//				glEnable(GL_TEXTURE_2D);
		//			}
		//		});
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
}
