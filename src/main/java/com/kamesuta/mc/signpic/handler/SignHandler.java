package com.kamesuta.mc.signpic.handler;
import java.lang.reflect.Field;

import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.placer.GuiSignPlacer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.event.GuiOpenEvent;

public class SignHandler {
	static {
		try {
			final Field[] fields = GuiEditSign.class.getDeclaredFields();
			for (final Field field : fields) {
				if (TileEntitySign.class.equals(field.getType())) {
					Reference.logger.info("Hook the TileEntitySign field included by GuiEditSign");
					field.setAccessible(true);
					f = field;
				}
			}
		} catch (final SecurityException e) {
			Reference.logger.error("Could not hook TileEntitySign field included by GuiEditSign");
		}
	}

	private static Field f;

	@SubscribeEvent
	public void onSignDone(final GuiOpenEvent event) {
		if (event.gui instanceof GuiEditSign) {
			if (f != null) {
				try {
					final GuiEditSign ges = (GuiEditSign) event.gui;
					final TileEntitySign tileSign = (TileEntitySign) f.get(ges);

					FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSignPlacer(tileSign));
				} catch (final IllegalArgumentException e) {
				} catch (final IllegalAccessException e) {
				}
			}
		}
	}
}