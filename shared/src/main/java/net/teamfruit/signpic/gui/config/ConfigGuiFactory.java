package net.teamfruit.signpic.gui.config;

#if !MC_12_LATER
#if MC_7_LATER

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import javax.annotation.Nullable;
import java.util.Set;

#else
import cpw.mods.fml.client.IModGuiFactory;
#endif

public class ConfigGuiFactory implements IModGuiFactory {
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	#if MC_12_OR_LATER
	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(final GuiScreen parentScreen) {
		return new ConfigGui(parentScreen);
	}
	#else
	@SuppressWarnings("deprecation")
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public @Nullable
	Class<? extends GuiScreen> mainConfigGuiClass() {
		return ConfigGui.class;
	}
	#endif

	@Override
	public void initialize(final @Nullable Minecraft minecraftInstance) {
	}
}
#endif