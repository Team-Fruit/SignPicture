package com.kamesuta.mc.signpic.handler;
import java.lang.reflect.Field;

import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.gui.GuiSignPicture;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.mode.Mode;
import com.kamesuta.mc.signpic.util.Sign;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;

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

	protected Minecraft mc = FMLClientHandler.instance().getClient();
	private static Field f;

	@SubscribeEvent
	public void onSign(final GuiOpenEvent event) {
		if (CurrentMode.instance.isMode(Mode.PLACE))
			if (event.gui instanceof GuiEditSign) {
				if (f != null) {
					try {
						final GuiEditSign ges = (GuiEditSign) event.gui;
						final TileEntitySign tileSign = (TileEntitySign) f.get(ges);
						CurrentMode.instance.getSign().sendSign(tileSign);
						event.setCanceled(true);
						if (!CurrentMode.instance.isContinue())
							CurrentMode.instance.setMode();
					} catch (final IllegalArgumentException e) {
					} catch (final IllegalAccessException e) {
					}
				}
			}
	}

	@SubscribeEvent
	public void onClick(final MouseEvent event) {
		if (CurrentMode.instance.isMode(Mode.COPY)) {
			if (event.buttonstate && this.mc.gameSettings.keyBindUseItem.getKeyCode() == event.button - 100) {
				if (this.mc.objectMouseOver != null) {
					final int x = this.mc.objectMouseOver.blockX;
					final int y = this.mc.objectMouseOver.blockY;
					final int z = this.mc.objectMouseOver.blockZ;
					final Block block = this.mc.theWorld.getBlock(x, y, z);
					if (block instanceof BlockSign) {
						final TileEntity tile = this.mc.theWorld.getTileEntity(x, y, z);
						if (tile instanceof TileEntitySign) {
							final TileEntitySign tilesign = (TileEntitySign)tile;
							final Sign sign = new Sign().parseSignEntity(tilesign);
							if (sign.isVaild()) {
								CurrentMode.instance.setSign(sign);
								event.setCanceled(true);
								this.mc.displayGuiScreen(new GuiSignPicture());
								if (!CurrentMode.instance.isContinue())
									CurrentMode.instance.setMode();
							}
						}
					}
				}
			}
		}
	}
}