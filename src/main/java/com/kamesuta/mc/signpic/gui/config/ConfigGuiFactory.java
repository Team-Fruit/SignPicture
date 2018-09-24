package com.kamesuta.mc.signpic.gui.config;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.compat.Compat.CompatModGuiFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ConfigGuiFactory extends CompatModGuiFactory {
	@Override
	public void initialize(final @Nullable Minecraft minecraftInstance) {

	}

	@Override
	public @Nullable Class<? extends GuiScreen> mainConfigGuiClassCompat() {
		return ConfigGui.class;
	}

	@Override
	public GuiScreen createConfigGuiCompat(final GuiScreen parentScreen) {
		return new ConfigGui(parentScreen);
	}
}