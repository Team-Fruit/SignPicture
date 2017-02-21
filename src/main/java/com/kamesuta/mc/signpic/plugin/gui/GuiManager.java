package com.kamesuta.mc.signpic.plugin.gui;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.plugin.SignData;

import net.minecraft.client.gui.GuiScreen;

public class GuiManager extends WFrame {
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
}
