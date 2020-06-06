package net.teamfruit.signpic.gui.config;

#if !MC_12_LATER

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.teamfruit.signpic.Config;
import net.teamfruit.signpic.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

#if MC_7_LATER
#else
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
#endif

public class ConfigGui extends #if MC_12_LATER Config #else GuiConfig #endif {
	public ConfigGui(final @Nullable GuiScreen parent) {
		super(parent, getConfigElements(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(Config.spec.getConfigFile().getName()));
	}

	private static @Nonnull
	List<IConfigElement> getConfigElements() {
		final List<IConfigElement> list = Lists.newArrayList();

		for (final String cat : Config.spec.getConfiguration().getCategoryNames()) {
			final ConfigCategory cc = Config.spec.getConfiguration().getCategory(cat);

			if (cc.isChild())
				continue;

			list.add(new ConfigElement(cc));
		}

		return list;
	}
}
#endif