package com.kamesuta.mc.signpic.preview;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.compat.Compat.CompatBlock;
import com.kamesuta.mc.signpic.compat.Compat.CompatBlockPos;
import com.kamesuta.mc.signpic.compat.Compat.CompatBlocks;
import com.kamesuta.mc.signpic.compat.Compat.CompatEnumFacing;
import com.kamesuta.mc.signpic.compat.Compat.CompatMathHelper;
import com.kamesuta.mc.signpic.compat.Compat.CompatMinecraft;
import com.kamesuta.mc.signpic.compat.Compat.CompatMovingObjectPosition;
import com.kamesuta.mc.signpic.compat.Compat.CompatWorld;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class SignEntity {
	private final @Nonnull PreviewTileEntitySign tileSign = new PreviewTileEntitySign(CompatBlocks.STANDING_SIGN);
	private boolean renderable = false;
	private boolean visible = false;

	private @Nullable PreviewTileEntitySign onItemUse(final @Nonnull EntityPlayer playerIn, final @Nonnull World worldIn, @Nonnull CompatBlockPos pos, final @Nonnull CompatEnumFacing side) {
		final CompatWorld world = new CompatWorld(worldIn);
		if (side==CompatEnumFacing.DOWN)
			return null;
		else if (!CompatMinecraft.getWorld().getBlockState(pos).getMaterial().isSolid())
			return null;
		else {
			pos = pos.offset(side);

			if (!new CompatBlock(CompatBlocks.STANDING_SIGN).canPlaceBlockAt(world, pos))
				return null;
			else {
				pos.setTileEntityPos(this.tileSign);

				if (side==CompatEnumFacing.UP) {
					this.tileSign.setBlockType(CompatBlocks.STANDING_SIGN);
					final int i = CompatMathHelper.floor_double((playerIn.rotationYaw+180.0F)*16.0F/360.0F+0.5D)&15;
					this.tileSign.setBlockMetadata(i);
				} else {
					this.tileSign.setBlockType(CompatBlocks.WALL_SIGN);
					this.tileSign.setBlockMetadata(side.getIndex());
				}

				this.renderable = true;

				return this.tileSign;
			}
		}
	}

	public @Nullable TileEntitySign capturePlace() {
		final EntityPlayer player = Client.mc.thePlayer;
		final WorldClient world = Client.mc.theWorld;
		if (player!=null&&world!=null) {
			final CompatMovingObjectPosition m = CompatMovingObjectPosition.getMovingPos();
			if (m!=null) {
				final CompatBlockPos p = m.getMovingBlockPos();
				if (p!=null) {
					setVisible(true);
					return onItemUse(player, world, p, m.getSideHit());
				}
			}
		}
		return null;
	}

	public @Nonnull TileEntitySign getTileEntity() {
		return this.tileSign;
	}

	public @Nonnull TileEntitySign getRenderTileEntity() throws IllegalStateException {
		if (!isRenderable())
			throw new IllegalStateException("Not Renderable");
		return this.tileSign;
	}

	public boolean isRenderable() {
		return this.renderable;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}
}
