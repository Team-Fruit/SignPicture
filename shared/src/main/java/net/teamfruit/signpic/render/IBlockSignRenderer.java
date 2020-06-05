package net.teamfruit.signpic.render;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntitySign;

public interface IBlockSignRenderer {
	public void renderBase(final @Nonnull TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float opacity);
}
