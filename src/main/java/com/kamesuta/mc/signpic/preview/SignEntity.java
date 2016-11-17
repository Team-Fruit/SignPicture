package com.kamesuta.mc.signpic.preview;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Client.MovePos;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class SignEntity {
	private final PreviewTileEntitySign tileSign = new PreviewTileEntitySign(Blocks.standing_sign);
	private boolean renderable = false;
	private boolean visible = false;

	protected PreviewTileEntitySign onItemUse(final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side) {
		if (side==EnumFacing.DOWN) {
			return null;
		} else if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid()) {
			return null;
		} else {
			pos = pos.offset(side);

			if (!Blocks.standing_sign.canPlaceBlockAt(worldIn, pos)) {
				return null;
			} else {
				this.tileSign.setPos(pos);

				if (side==EnumFacing.UP) {
					this.tileSign.setBlockType(Blocks.standing_sign);
					final int i = MathHelper.floor_double((playerIn.rotationYaw+180.0F)*16.0F/360.0F+0.5D)&15;
					this.tileSign.setBlockMetadata(i);
				} else {
					this.tileSign.setBlockType(Blocks.wall_sign);
					this.tileSign.setBlockMetadata(side.getIndex());
				}

				this.renderable = true;

				return this.tileSign;
			}
		}
	}

	public TileEntitySign capturePlace() {
		final Minecraft mc = Client.mc;
		if (mc.thePlayer!=null) {
			final MovingObjectPosition m = MovePos.getMovingPos();
			final MovePos p = MovePos.getBlockPos();
			if (m!=null&&p!=null) {
				setVisible(true);
				return onItemUse(mc.thePlayer, mc.theWorld, p.pos, m.sideHit);
			}
		}
		return null;
	}

	public TileEntitySign getTileEntity() {
		return this.tileSign;
	}

	public TileEntitySign getRenderTileEntity() throws IllegalStateException {
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
