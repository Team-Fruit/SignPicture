package com.kamesuta.mc.signpic.handler;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;

public class KeyHandler {
	public static KeyHandler INSTANCE = new KeyHandler();

	private static final KeyBinding KEY_BINDING_GUI = new KeyBinding("signpic.key.gui", Keyboard.KEY_DIVIDE, "signpic.key.category");

	public static final KeyBinding[] KEY_BINDINGS = new KeyBinding[] {
			KEY_BINDING_GUI
	};

	private KeyHandler() {
	}

	public void init() {
		for (final KeyBinding keyBinding : KEY_BINDINGS) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}

	@SubscribeEvent
	public void onKeyInput(final InputEvent event) {
		final Minecraft mc = FMLClientHandler.instance().getClient();
		if (mc.currentScreen == null) {
			if (KEY_BINDING_GUI.isPressed()) {
				// mc.displayGuiScreen(new GuiWayLoad(ClientProxy.MINECRAFT.currentScreen));
				final int x = mc.objectMouseOver.blockX;
				final int y = mc.objectMouseOver.blockY;
				final int z = mc.objectMouseOver.blockZ;
				final Block block = mc.theWorld.getBlock(x, y, z);
				if (block instanceof BlockSign) {
					final TileEntity tile = mc.theWorld.getTileEntity(x, y, z);
					if (tile instanceof TileEntitySign) {
						final TileEntitySign tileSign = (TileEntitySign) tile;
						final NetHandlerPlayClient nethandlerplayclient = mc.getNetHandler();

						if (nethandlerplayclient != null)
						{
							nethandlerplayclient.addToSendQueue(new C12PacketUpdateSign(tileSign.xCoord, tileSign.yCoord, tileSign.zCoord, new String[] {"aa", "bb", "cc", "dd"}));
						}

						tileSign.setEditable(true);
					}
				}
			}
		}
	}
}
