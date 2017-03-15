package com.kamesuta.mc.signpic.plugin.gui;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.plugin.SignData;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiManager extends WFrame {
	public static ResourceLocation logo = new ResourceLocation("signpic", "textures/plugin/logo.png");

	public @Nonnull String key;
	public int size;
	protected final @Nonnull Map<Integer, SignData> data = Maps.newHashMap();

	protected final GuiManagerSarchBox sarchBox;

	public GuiManager(final @Nullable GuiScreen parent, final @Nonnull String data, final @Nonnull String size) {
		super(parent);
		this.key = data;
		this.size = NumberUtils.toInt(size);
		this.sarchBox = new GuiManagerSarchBox(new R(Coord.left(125)));
	}

	public GuiManager(final @Nonnull String data, final @Nonnull String size) {
		this(null, data, size);
	}

	public void data(final @Nonnull String token, final @Nonnull String s) {
		final int id = NumberUtils.toInt(token);
		final SignData d = Client.gson.fromJson(s, SignData.class);
		this.data.put(id, d);
	}

	@Override
	protected void initWidget() {
		add(new WBase(new R()) {
			VMotion m = V.pm(0);

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float opacity) {
				WRenderer.startShape();
				OpenGL.glColor4f(0f, 0f, 0f, this.m.get());
				draw(getGuiPosition(pgp));
			}

			@Override
			public void onAdded() {
				this.m.stop().add(Easings.easeLinear.move(.2f, .5f)).start();
			}

			@Override
			public boolean onCloseRequest() {
				this.m.stop().add(Easings.easeLinear.move(.15f, 0f)).start();
				return false;
			}

			@Override
			public boolean onClosing(final WEvent ev, final Area pgp, final Point mouse) {
				return this.m.isFinished();
			}
		});
		add(new WPanel(new R(Coord.left(5), Coord.top(5), Coord.height(30))) {
			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				final Area a = getGuiPosition(pgp);
				final float opacity = getGuiOpacity(popacity);

				WRenderer.startShape();
				OpenGL.glColor4f(1.0F, 1.0F, 1.0F, opacity);
				WRenderer.startTexture();
				texture().bindTexture(logo);
				drawTexture(Area.size(a.x1(), a.y1(), a.x1()+(439f/(125f/30)), a.y2()), null, null);

				super.draw(ev, pgp, p, frame, popacity);
			}

			@Override
			protected void initWidget() {
				add(GuiManager.this.sarchBox);
			}
		});
	}
}
