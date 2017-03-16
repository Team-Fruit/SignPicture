package com.kamesuta.mc.signpic.plugin.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.font.FontPosition;
import com.kamesuta.mc.bnnwidget.font.WFontRenderer;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.Client;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiManagerSarchBox extends WPanel {

	public final @Nonnull GuiManagerTextField textField;
	public final @Nonnull Button advancedSearch;
	public final @Nonnull Button gallary;
	public final @Nonnull Button stats;

	public GuiManagerSarchBox(final R position) {
		super(position);
		this.textField = new GuiManagerTextField(new R(Coord.left(5), Coord.height(15), Coord.right(310), Coord.top(5))) {
			@Override
			public R getGuiPosition() {
				return this.position;
			}
		};
		this.advancedSearch = new Button(new R(Coord.right(220), Coord.height(15), Coord.width(85), Coord.top(5))) {
			@Override
			public R getGuiPosition() {
				return this.position;
			};

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				final Area a = getGuiPosition(pgp);

				OpenGL.glPushMatrix();
				WRenderer.startShape();
				OpenGL.glColor4i(113, 70, 0, 255);
				draw(a);
				OpenGL.glColor4i(188, 116, 0, 255);
				OpenGL.glLineWidth(2f);
				draw(a, GL_LINE_LOOP);
				OpenGL.glPopMatrix();

				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point mouse, final int button) {
				return true;
			}
		}.setText(I18n.format("signpic.manager.search.advanced"));
		this.gallary = new Button(new R(Coord.right(160), Coord.height(15), Coord.width(55), Coord.top(5))) {
			@Override
			public R getGuiPosition() {
				return this.position;
			};

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				final Area a = getGuiPosition(pgp);

				OpenGL.glPushMatrix();
				WRenderer.startShape();
				OpenGL.glColor4i(112, 23, 0, 255);
				draw(a);
				OpenGL.glColor4i(188, 39, 0, 255);
				OpenGL.glLineWidth(2f);
				draw(a, GL_LINE_LOOP);
				OpenGL.glPopMatrix();

				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point mouse, final int button) {
				return true;
			};
		}.setText(I18n.format("signpic.manager.gallary"));
		this.stats = new Button(new R(Coord.right(100), Coord.height(15), Coord.width(55), Coord.top(5))) {
			@Override
			public R getGuiPosition() {
				return this.position;
			};

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				final Area a = getGuiPosition(pgp);

				OpenGL.glPushMatrix();
				WRenderer.startShape();
				OpenGL.glColor4i(56, 90, 104, 255);
				draw(a);
				OpenGL.glColor4i(93, 146, 172, 255);
				OpenGL.glLineWidth(2f);
				draw(a, GL_LINE_LOOP);
				OpenGL.glPopMatrix();

				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point mouse, final int button) {
				return true;
			}
		}.setText(I18n.format("signpic.manager.stats"));
	}

	@Override
	protected void initWidget() {
		add(this.textField);
		add(this.advancedSearch);
		add(this.gallary);
		add(this.stats);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
		super.draw(ev, pgp, p, frame, popacity);
	}

	public static class Button extends WBase {
		protected static @Nonnull WFontRenderer fontRenderer = new WFontRenderer(GuiManager.font);

		public @Nullable String text;

		public Button(final R position) {
			super(position);
		}

		public @Nullable String getText() {
			return this.text;
		}

		public @Nonnull Button setText(final String text) {
			this.text = text;
			return this;
		}

		@Override
		public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
			drawText(ev, pgp, p, frame, popacity);
			super.draw(ev, pgp, p, frame, popacity);
		}

		public void drawText(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse, final float frame, final float popacity) {
			final String text = getText();
			if (this.text!=null) {
				final Area a = getGuiPosition(pgp);
				OpenGL.glColor4f(1f, 1f, 1f, 1f);
				WRenderer.startTexture();
				final FontPosition fp = fontRenderer.getSetting()
						.setScale(.5f)
						.setPosition(a.x1()+a.w()/2, a.y1())
						.setFontSize(Math.round(a.h()/ev.owner.scale())*2)
						.setAlign(Align.CENTER)
						.setShadow(true)
						.setText(text);
				fontRenderer.drawString(fp);
			}
		}

		protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse, final int button) {
			return true;
		}

		@Override
		public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
			final Area abs = getGuiPosition(pgp);
			if (abs.pointInside(p))
				if (button<2)
					if (onClicked(ev, pgp, p, button)) {
						Client.playSound(new ResourceLocation("signpic", "gui.confirm"), 1f);
						return true;
					}
			return false;
		}
	}
}
