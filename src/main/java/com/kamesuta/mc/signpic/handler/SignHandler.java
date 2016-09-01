package com.kamesuta.mc.signpic.handler;
import java.lang.reflect.Field;

import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.placer.PlacerMode;

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
	public void onSign(final GuiOpenEvent event) {
		if (PlacerMode.instance.isEnabled())
			if (event.gui instanceof GuiEditSign) {
				if (f != null) {
					try {
						final GuiEditSign ges = (GuiEditSign) event.gui;
						final TileEntitySign tileSign = (TileEntitySign) f.get(ges);
						PlacerMode.instance.getSign().sendSign(tileSign);
						event.setCanceled(true);
						PlacerMode.instance.disable();
					} catch (final IllegalArgumentException e) {
					} catch (final IllegalAccessException e) {
					}
				}
			}
	}
}