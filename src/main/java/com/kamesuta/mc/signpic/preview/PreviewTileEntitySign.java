package com.kamesuta.mc.signpic.preview;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntitySign;

public class PreviewTileEntitySign extends TileEntitySign {
	protected int blockMetadata;

	public PreviewTileEntitySign(final @Nonnull Block defaultBlockType) {
		this.blockType = defaultBlockType;
		this.blockMetadata = 0;
	}

	@Override
	public @Nullable Block getBlockType() {
		return this.blockType;
	}

	public void setBlockType(final @Nonnull Block blockType) {
		this.blockType = blockType;
	}

	@Override
	public int getBlockMetadata() {
		return this.blockMetadata;
	}

	public void setBlockMetadata(final int blockMetadata) {
		this.blockMetadata = blockMetadata;
	}

	@Override
	public void markDirty() {
	}
}