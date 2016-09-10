package com.kamesuta.mc.signpic.preview;

import com.kamesuta.mc.signpic.Client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SignEntity {
	private final PreviewTileEntitySign tileSign = new PreviewTileEntitySign(Blocks.standing_sign);
	private boolean renderable = false;

	protected PreviewTileEntitySign onItemUse(final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing facing)
	{
		final IBlockState iblockstate = worldIn.getBlockState(pos);
		final boolean flag = iblockstate.getBlock().isReplaceable(worldIn, pos);

		if (facing != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag) && (!flag || facing == EnumFacing.UP))
		{
			pos = pos.offset(facing);

			if (!Blocks.standing_sign.canPlaceBlockAt(worldIn, pos))
			{
				return null;
			}
			else
			{
				this.tileSign.setPos(pos);

				if (facing == EnumFacing.UP)
				{
					this.tileSign.setBlockType(Blocks.standing_sign);
					final int i = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 15;
					this.tileSign.setBlockMetadata(i);
				}
				else
				{
					this.tileSign.setBlockType(Blocks.wall_sign);
					this.tileSign.setBlockMetadata(facing.getIndex());
				}

				this.renderable = true;

				return this.tileSign;
			}
		}
		return null;
	}

	public TileEntitySign capturePlace() {
		final Minecraft mc = Client.mc;
		final RayTraceResult over = mc.objectMouseOver;
		if (over != null && mc.thePlayer != null) {
			return onItemUse(mc.thePlayer, mc.theWorld, over.getBlockPos(), over.sideHit);
		}
		return null;
	}

	public TileEntitySign getTileEntity() {
		return this.tileSign;
	}

	public TileEntitySign getRenderTileEntity() throws IllegalStateException {
		if (!isRenderable()) throw new IllegalStateException("Not Renderable");
		return this.tileSign;
	}

	public boolean isRenderable() {
		return this.renderable;
	}
}