package com.kamesuta.mc.signpic.plugin.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;

import net.minecraft.client.resources.I18n;

public class GuiManagerSarchBox extends WPanel {

	public final @Nonnull GuiManagerTextField textField;
	public final @Nonnull GuiManagerButton advancedSearch;
	public final @Nonnull GuiManagerButton gallary;
	public final @Nonnull GuiManagerButton stats;

	public GuiManagerSarchBox(final R position) {
		super(position);
		this.textField = new GuiManagerTextField(new R(Coord.left(5), Coord.height(15), Coord.right(310), Coord.top(5))) {
			@Override
			public void onTextChanged(final String oldText) {

			}
		};
		this.advancedSearch = new GuiManagerButton(new R(Coord.right(220), Coord.height(15), Coord.width(85), Coord.top(5))) {
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
		this.gallary = new GuiManagerButton(new R(Coord.right(160), Coord.height(15), Coord.width(55), Coord.top(5))) {
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
		}.setText(I18n.format(GuiManager.type!=ManagerType.LIST ? "signpic.manager.gallary" : "signpic.manager.list"));
		this.stats = new GuiManagerButton(new R(Coord.right(100), Coord.height(15), Coord.width(55), Coord.top(5))) {
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

}
