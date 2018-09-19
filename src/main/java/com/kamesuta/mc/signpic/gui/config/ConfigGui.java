package com.kamesuta.mc.signpic.gui.config;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.compat.Compat.CompatConfigElement;
import com.kamesuta.mc.signpic.compat.Compat.CompatGuiConfig;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;

public class ConfigGui extends CompatGuiConfig {
	public ConfigGui(final @Nullable GuiScreen parent) {
		super(parent, getConfigElements(), Reference.MODID, false, false, Config.getConfig().getFilePath());
	}

	private static @Nonnull List<CompatConfigElement> getConfigElements() {
		final List<CompatConfigElement> list = Lists.newArrayList();

		for (final String cat : Config.getConfig().getCategoryNames()) {
			final ConfigCategory cc = Config.getConfig().getCategory(cat);

			if (cc.isChild())
				continue;

			list.add(CompatConfigElement.fromCategory(cc));
		}

		return list;
	}
}