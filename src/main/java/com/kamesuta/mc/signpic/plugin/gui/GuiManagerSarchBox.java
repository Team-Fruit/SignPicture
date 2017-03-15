package com.kamesuta.mc.signpic.plugin.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
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

	protected final @Nonnull GuiManagerTextField textField;

	public GuiManagerSarchBox(final R position) {
		super(position);
		this.textField = new GuiManagerTextField(new R(Coord.left(10), Coord.height(15), Coord.right(300), Coord.top(5)));
	}

	@Override
	protected void initWidget() {
		add(this.textField);
		add(new Button(new R(Coord.right(210), Coord.height(15), Coord.width(80), Coord.top(5))) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				final Area a = getGuiPosition(pgp);
				OpenGL.glPushMatrix();
				WRenderer.startShape();
				OpenGL.glColor4f(.55f, .3f, .15f, 1f);
				draw(a);
				OpenGL.glColor4f(.7f, .3f, .15f, 1f);
				OpenGL.glLineWidth(2f);
				draw(a, GL_LINE_LOOP);
				OpenGL.glPopMatrix();

				super.draw(ev, pgp, p, frame, popacity);
			}
		}.setText(I18n.format("signpic.manager.search.advanced")));
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
		super.draw(ev, pgp, p, frame, popacity);
	}

	public static class Button extends WBase {

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
			if (text!=null) {
				final Area a = getGuiPosition(pgp);
				OpenGL.glColor4f(1f, 1f, 1f, 1f);
				WRenderer.startTexture();
				GuiManager.fontRenderer.drawString(text, a, ev.owner.guiScale(), Align.CENTER, VerticalAlign.MIDDLE, false);
				//				drawString(text, a, Align.CENTER, VerticalAlign.MIDDLE, false);
			}
		}

		protected boolean onClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point mouse, final int button) {
			return true;
		}

		@Override
		public boolean mouseClicked(final @Nonnull WEvent ev, final @Nonnull Area pgp, final @Nonnull Point p, final int button) {
			final Area abs = getGuiPosition(pgp);
			if (abs.pointInside(p))
				if (onClicked(ev, pgp, p, button)) {
					Client.playSound(new ResourceLocation("signpic", "gui.confirm"), 1f);
					return true;
				}
			return false;
		}
	}
}
