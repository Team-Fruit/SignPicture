package com.kamesuta.mc.signpic;

import com.kamesuta.mc.signpic.gui.GuiSignPicEditor;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;

public class Client {
	public final static Minecraft mc = FMLClientHandler.instance().getClient();
	public static ImageManager manager;
	public static CustomTileEntitySignRenderer renderer;

	public static void openEditor() {
		mc.displayGuiScreen(new GuiSignPicEditor());
	}

	public static void startSection(final String sec) {
		mc.mcProfiler.startSection(sec);
	}

	public static void endSection() {
		mc.mcProfiler.endSection();
	}

	public static TileEntitySign getTileSignLooking() {
		if (mc.objectMouseOver != null) {
			final int x = mc.objectMouseOver.blockX;
			final int y = mc.objectMouseOver.blockY;
			final int z = mc.objectMouseOver.blockZ;
			final Block block = mc.theWorld.getBlock(x, y, z);
			if (block instanceof BlockSign) {
				final TileEntity tile = mc.theWorld.getTileEntity(x, y, z);
				if (tile instanceof TileEntitySign)
					return (TileEntitySign)tile;
			}
		}
		return null;
	}
}
