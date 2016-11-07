package com.kamesuta.mc.signpic;

import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.handler.CoreHandler;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MovingObjectPosition;

public class Client {
	public final static Minecraft mc = FMLClientHandler.instance().getClient();

	public static CustomTileEntitySignRenderer renderer;
	public static CoreHandler handler;
	public static Locations location;

	public static String mcversion;
	public static String forgeversion;

	public static String id;
	public static String name;

	public static void openEditor() {
		mc.displayGuiScreen(new GuiMain(mc.currentScreen));
	}

	public static void startSection(final String sec) {
		mc.mcProfiler.startSection(sec);
	}

	public static void endSection() {
		mc.mcProfiler.endSection();
	}

	public static TileEntitySign getTileSignLooking() {
		if (MovePos.getBlock() instanceof BlockSign) {
			final TileEntity tile = MovePos.getTile();
			if (tile instanceof TileEntitySign)
				return (TileEntitySign) tile;
		}
		return null;
	}

	public static class MovePos {
		public int x;
		public int y;
		public int z;

		public MovePos(final int x, final int y, final int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public static MovingObjectPosition getMovingPos() {
			return mc.objectMouseOver;
		}

		public static MovePos getBlockPos() {
			final MovingObjectPosition movingPos = getMovingPos();
			if (movingPos!=null)
				return new MovePos(movingPos.blockX, movingPos.blockY, movingPos.blockZ);
			return null;
		}

		public static TileEntity getTile() {
			final MovePos movePos = getBlockPos();
			if (movePos!=null)
				return mc.theWorld.getTileEntity(movePos.x, movePos.y, movePos.z);
			return null;
		}

		public static Block getBlock() {
			final MovePos movePos = getBlockPos();
			if (movePos!=null)
				return mc.theWorld.getBlock(movePos.x, movePos.y, movePos.z);
			return null;
		}
	}
}
