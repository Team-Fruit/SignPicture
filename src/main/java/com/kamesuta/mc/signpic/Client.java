package com.kamesuta.mc.signpic;

import org.apache.commons.lang3.Validate;

import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.handler.CoreHandler;
import com.kamesuta.mc.signpic.render.CustomTileEntitySignRenderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.client.FMLClientHandler;

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
		public BlockPos pos;

		public MovePos(final BlockPos pos) {
			Validate.notNull(pos, "MovePos needs position");
			this.pos = pos;
		}

		public static MovingObjectPosition getMovingPos() {
			return mc.objectMouseOver;
		}

		public static MovePos getBlockPos() {
			final MovingObjectPosition movingPos = getMovingPos();
			if (movingPos!=null) {
				final BlockPos pos = movingPos.getBlockPos();
				if (pos!=null)
					return new MovePos(pos);
			}
			return null;
		}

		public static IBlockState getBlockState() {
			final MovePos movePos = getBlockPos();
			if (movePos!=null)
				return mc.theWorld.getBlockState(movePos.pos);
			return null;
		}

		public static TileEntity getTile() {
			final MovePos movePos = getBlockPos();
			if (movePos!=null)
				return mc.theWorld.getTileEntity(movePos.pos);
			return null;
		}

		public static Block getBlock() {
			final IBlockState blockState = getBlockState();
			if (blockState!=null)
				return blockState.getBlock();
			return null;
		}
	}
}
