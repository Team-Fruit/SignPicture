package com.kamesuta.mc.signpic.preview;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Client.MovePos;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class SignEntity {
	private final @Nonnull PreviewTileEntitySign tileSign = new PreviewTileEntitySign(Blocks.standing_sign);
	private boolean renderable = false;
	private boolean visible = false;

	private @Nullable PreviewTileEntitySign onItemUse(final @Nonnull EntityPlayer playerIn, final @Nonnull World worldIn, int x, int y, int z, final int direction) {
		if (direction==0)
			return null;
		else if (!worldIn.getBlock(x, y, z).getMaterial().isSolid())
			return null;
		else {
			if (direction==1)
				++y;

			if (direction==2)
				--z;

			if (direction==3)
				++z;

			if (direction==4)
				--x;

			if (direction==5)
				++x;

			if (!Blocks.standing_sign.canPlaceBlockAt(worldIn, x, y, z))
				return null;
			else {
				this.tileSign.xCoord = x;
				this.tileSign.yCoord = y;
				this.tileSign.zCoord = z;

				if (direction==1) {
					this.tileSign.setBlockType(Blocks.standing_sign);
					final int i = MathHelper.floor_double((playerIn.rotationYaw+180.0F)*16.0F/360.0F+0.5D)&15;
					this.tileSign.setBlockMetadata(i);
				} else {
					this.tileSign.setBlockType(Blocks.wall_sign);
					this.tileSign.setBlockMetadata(direction);
				}

				this.renderable = true;

				return this.tileSign;
			}
		}
	}

	public @Nullable TileEntitySign capturePlace() {
		final EntityClientPlayerMP player = Client.mc.thePlayer;
		final WorldClient world = Client.mc.theWorld;
		if (player!=null&&world!=null) {
			final MovingObjectPosition m = MovePos.getMovingPos();
			final MovePos p = MovePos.getBlockPos();
			if (m!=null&&p!=null) {
				setVisible(true);
				return onItemUse(player, world, p.x, p.y, p.z, m.sideHit);
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
