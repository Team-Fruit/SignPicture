package com.kamesuta.mc.signpic.plugin.gui;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.bnnwidget.WPanel;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.plugin.SignData;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiManager extends WFrame {
	public static ResourceLocation logo = new ResourceLocation("signpic", "textures/plugin/logo.png");

	public @Nonnull String key;
	public int size;

	protected final @Nonnull Map<Integer, SignData> data = Maps.newHashMap();

	public GuiManager(final @Nonnull GuiScreen parent, final @Nonnull String data, final @Nonnull String size) {
		super(parent);
		this.key = data;
		this.size = NumberUtils.toInt(size);
	}

	public GuiManager(final @Nonnull String data, final @Nonnull String size) {
		this.key = data;
		this.size = NumberUtils.toInt(size);
	}

	public void data(final @Nonnull String token, final @Nonnull String s) {
		final int id = NumberUtils.toInt(token);
		final SignData d = Client.gson.fromJson(s, SignData.class);
		this.data.put(id, d);
	}

	@Override
	protected void initWidget() {
		add(new WPanel(new R(Coord.left(0), Coord.right(0), Coord.top(0), Coord.height(50))) {

			@Override
			public void draw(final WEvent ev, final Area pgp, final Point p, final float frame, final float popacity) {
				super.draw(ev, pgp, p, frame, popacity);

			}
		});
	}
}
