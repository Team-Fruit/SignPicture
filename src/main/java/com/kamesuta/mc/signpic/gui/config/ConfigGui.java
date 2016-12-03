package com.kamesuta.mc.signpic.gui.config;

import java.util.ArrayList;
import java.util.List;

import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.Reference;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

public class ConfigGui extends GuiConfig {

	public ConfigGui(final GuiScreen parent) {
		super(parent, getConfigElements(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(Config.instance.getFilePath()));
	}

	@SuppressWarnings("rawtypes")
	private static List<IConfigElement> getConfigElements() {
		final List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (final String cat : Config.instance.getCategoryNames()) {
			final ConfigCategory cc = Config.instance.getCategory(cat);

			if (cc.isChild())
				continue;

			final ConfigElement ce = new ConfigElement<String>(cc);
			list.add(ce);
		}

		return list;
	}
}