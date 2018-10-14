package net.teamfruit.signpic.preview;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntitySign;
import net.teamfruit.signpic.compat.Compat.CompatBlock;
import net.teamfruit.signpic.compat.Compat.CompatBlockPos;
import net.teamfruit.signpic.compat.Compat.CompatBlocks;
import net.teamfruit.signpic.compat.Compat.CompatEntityPlayer;
import net.teamfruit.signpic.compat.Compat.CompatEnumFacing;
import net.teamfruit.signpic.compat.Compat.CompatMathHelper;
import net.teamfruit.signpic.compat.Compat.CompatMinecraft;
import net.teamfruit.signpic.compat.Compat.CompatMovingObjectPosition;
import net.teamfruit.signpic.compat.Compat.CompatWorld;

public class SignEntity {
	private final @Nonnull PreviewTileEntitySign tileSign = new PreviewTileEntitySign(CompatBlocks.STANDING_SIGN);
	private boolean renderable = false;
	private boolean visible = false;

	private @Nullable PreviewTileEntitySign onItemUse(final @Nonnull CompatEntityPlayer player, final @Nonnull CompatWorld world, @Nonnull CompatBlockPos pos, final @Nonnull CompatEnumFacing side) {
		if (side==CompatEnumFacing.DOWN)
			return null;
		else if (!world.getBlockState(pos).getMaterial().isSolid())
			return null;
		else {
			pos = pos.offset(side);

			if (!new CompatBlock(CompatBlocks.STANDING_SIGN).canPlaceBlockAt(world, pos))
				return null;
			else {
				pos.setTileEntityPos(this.tileSign);

				if (side==CompatEnumFacing.UP) {
					this.tileSign.setBlockType(CompatBlocks.STANDING_SIGN);
					final int i = CompatMathHelper.floor_double((player.getPlayerObj().rotationYaw+180.0F)*16.0F/360.0F+0.5D)&15;
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
		final CompatEntityPlayer player = CompatMinecraft.getPlayer();
		final CompatWorld world = CompatMinecraft.getWorld();
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
