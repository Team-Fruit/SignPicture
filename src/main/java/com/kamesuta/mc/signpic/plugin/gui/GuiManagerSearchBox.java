package com.kamesuta.mc.signpic.plugin.gui;

import static org.lwjgl.opengl.GL11.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.util.Timer;

import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.component.FontLabel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.RenderOption;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.plugin.search.FilterParser;
import com.kamesuta.mc.signpic.plugin.search.ImplFilterExpression;
import com.kamesuta.mc.signpic.plugin.search.Searchable;

import net.minecraft.client.resources.I18n;

public class GuiManagerSearchBox extends WPanel {

	public final @Nonnull GuiManagerTextField textField;
	public final @Nonnull GuiManagerButton advancedSearch;
	public final @Nonnull GuiManagerButton gallary;
	public final @Nonnull GuiManagerButton stats;

	protected final GuiManager manager;

	protected @Nullable Searchable searchTarget;

	public GuiManagerSearchBox(final @Nonnull R position, final @Nonnull GuiManager manager) {
		super(position);
		this.manager = manager;
		this.textField = new GuiManagerTextField(new R(Coord.left(5), Coord.height(15), Coord.right(310), Coord.top(5))) {
			private final Timer timer = new Timer();

			@Override
			public void onTextChanged(final String oldText) {
				if (GuiManagerSearchBox.this.searchTarget!=null) {
					this.timer.set(-.5f);
				}
			}

			@Override
			public void update(final WEvent ev, final Area pgp, final Point p) {
				super.update(ev, pgp, p);
				if (this.timer.getTime()<0f)
					GuiManagerSearchBox.this.searchTarget.filter(new ImplFilterExpression(GuiManagerSearchBox.this.manager.data, FilterParser.parse(this.textField.getText())));
				if (this.timer.getTime()<0f) {
					final Searchable target = GuiManagerSearchBox.this.searchTarget;
					if (target!=null)
						if (StringUtils.isNotEmpty(this.textField.getText()))
							target.filter(new ImplFilterExpression(GuiManagerSearchBox.this.manager.data, FilterParser.parse(this.textField.getText())));
						else
							target.filter(null);
				}
			}
		};
		this.advancedSearch = new GuiManagerButton(new R(Coord.right(220), Coord.height(15), Coord.width(85), Coord.top(5))) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, @Nonnull final RenderOption opt) {
				final Area a = getGuiPosition(pgp);

				OpenGL.glPushMatrix();
				WRenderer.startShape();
				OpenGL.glColor4i(113, 70, 0, 255);
				draw(a);
				OpenGL.glColor4i(188, 116, 0, 255);
				OpenGL.glLineWidth(2f);
				draw(a, GL_LINE_LOOP);
				OpenGL.glPopMatrix();

				super.draw(ev, pgp, p, frame, popacity, opt);
			}

			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point mouse, final int button) {
				return true;
			}
		}.setText(I18n.format("signpic.manager.search.advanced"));
		this.gallary = new GuiManagerButton(new R(Coord.right(160), Coord.height(15), Coord.width(55), Coord.top(5))) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, @Nonnull final RenderOption opt) {
				final Area a = getGuiPosition(pgp);

				OpenGL.glPushMatrix();
				WRenderer.startShape();
				OpenGL.glColor4i(112, 23, 0, 255);
				draw(a);
				OpenGL.glColor4i(188, 39, 0, 255);
				OpenGL.glLineWidth(2f);
				draw(a, GL_LINE_LOOP);
				OpenGL.glPopMatrix();

				super.draw(ev, pgp, p, frame, popacity, opt);
			}

			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point mouse, final int button) {
				return true;
			};
		}.setText(I18n.format(GuiManager.type!=ManagerType.LIST ? "signpic.manager.list" : "signpic.manager.gallary"));
		this.stats = new GuiManagerButton(new R(Coord.right(100), Coord.height(15), Coord.width(55), Coord.top(5))) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity, @Nonnull final RenderOption opt) {
				final Area a = getGuiPosition(pgp);

				OpenGL.glPushMatrix();
				WRenderer.startShape();
				OpenGL.glColor4i(56, 90, 104, 255);
				draw(a);
				OpenGL.glColor4i(93, 146, 172, 255);
				OpenGL.glLineWidth(2f);
				draw(a, GL_LINE_LOOP);
				OpenGL.glPopMatrix();

				super.draw(ev, pgp, p, frame, popacity, opt);
			}

			@Override
			protected boolean onClicked(final WEvent ev, final Area pgp, final Point mouse, final int button) {
				return true;
			}
		}.setText(I18n.format("signpic.manager.stats"));
	}

	public void setSearchTarget(final @Nullable Searchable searchTarget) {
		this.searchTarget = searchTarget;
	}

	@Override
	protected void initWidget() {
		add(this.textField);
		add(this.advancedSearch);
		add(this.gallary);
		add(this.stats);
		add(new FontLabel(new R(Coord.left(5), Coord.top(20), Coord.height(10), Coord.width(80)), GuiManager.PLAIN_FONT) {
			int sizeCache = -1;

			@Override
			public String getText() {
				final int size = searchTarget!=null ? searchTarget.getNow().size() : GuiManagerSearchBox.this.manager.data.size();
				if (this.sizeCache==size)
					return this.text;
				final StringBuilder sb = new StringBuilder();
				sb.append(size);
				if (GuiManagerSearchBox.this.manager.size>size)
					sb.append('+');
				sb.append(' ');
				sb.append(I18n.format("signpic.manager.matches"));
				this.sizeCache = GuiManagerSearchBox.this.manager.data.size();
				this.text = sb.toString();
				return this.text;
			};
		}.setColor(0x9acd32));
	}

}
