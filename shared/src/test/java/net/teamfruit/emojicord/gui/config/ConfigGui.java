package net.teamfruit.emojicord.gui.config;

#if !MC_12_LATER
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
#if MC_7_LATER
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
#else
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
#endif

import com.google.common.collect.Lists;
import net.teamfruit.emojicord.EmojicordConfig;
import net.teamfruit.emojicord.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ConfigGui extends #if MC_12_LATER Config #else GuiConfig #endif {
	public ConfigGui(final @Nullable GuiScreen parent) {
		super(parent, getConfigElements(), Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(EmojicordConfig.spec.getConfigFile().getName()));
	}

	private static @Nonnull
	List<IConfigElement> getConfigElements() {
		final List<IConfigElement> list = Lists.newArrayList();

		for (final String cat : EmojicordConfig.spec.getConfiguration().getCategoryNames()) {
			final ConfigCategory cc = EmojicordConfig.spec.getConfiguration().getCategory(cat);

			if (cc.isChild())
				continue;

			list.add(new ConfigElement(cc));
		}

		return list;
	}
}
#endif