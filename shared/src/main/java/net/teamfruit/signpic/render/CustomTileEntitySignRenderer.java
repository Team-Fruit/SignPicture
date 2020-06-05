package net.teamfruit.signpic.render;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntitySign;
import net.teamfruit.signpic.compat.Compat.CompatTileEntitySignRenderer;

public class CustomTileEntitySignRenderer extends CompatTileEntitySignRenderer implements IBlockSignRenderer {
	public final CustomBlockSignRenderer renderer = new CustomBlockSignRenderer(this);

	@Override
	public void renderBase(final TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float opacity) {
		renderBaseTileEntityAt(tile, x, y, z, partialTicks, destroy, 1f);
	}

	@Override
	public void renderTileEntityAtCompat(final @Nullable TileEntitySign tile, final double x, final double y, final double z, final float partialTicks, final int destroy, final float opacity) {
		if (tile!=null)
			this.renderer.renderBase(tile, x, y, z, partialTicks, destroy, opacity);
	}
}