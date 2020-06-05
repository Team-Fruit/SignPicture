package net.teamfruit.signpic.gui.config;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.teamfruit.signpic.compat.Compat.CompatModGuiFactory;

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