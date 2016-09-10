package com.kamesuta.mc.signpic.preview;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntitySign;

public class PreviewTileEntitySign extends TileEntitySign {
	protected int blockMetadata;

	public PreviewTileEntitySign(final Block defaultBlockType) {
		this.blockType = defaultBlockType;
		this.blockMetadata = 0;
	}

	@Override
	public Block getBlockType() {
		return this.blockType;
	}

	public void setBlockType(final Block blockType) {
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