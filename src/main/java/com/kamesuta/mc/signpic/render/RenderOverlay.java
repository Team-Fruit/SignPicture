package com.kamesuta.mc.signpic.render;

import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.ImageSize;
import com.kamesuta.mc.signpic.util.SignParser;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class RenderOverlay {
	protected final ImageManager manager;
	protected final Minecraft mc;

	public RenderOverlay(final ImageManager manager) {
		this.manager = manager;
		this.mc = FMLClientHandler.instance().getClient();
	}

	@SubscribeEvent
	public void onText(final RenderGameOverlayEvent.Text event) {
		if (this.mc.gameSettings.showDebugInfo) {
			final int x = this.mc.objectMouseOver.blockX;
			final int y = this.mc.objectMouseOver.blockY;
			final int z = this.mc.objectMouseOver.blockZ;
			final Block block = this.mc.theWorld.getBlock(x, y, z);
			if (block instanceof BlockSign) {
				final TileEntity tile = this.mc.theWorld.getTileEntity(x, y, z);
				if (tile instanceof TileEntitySign) {
					final SignParser sign = new SignParser((TileEntitySign)tile);
					if (sign.isVaild()) {
						event.left.add("");
						final String id = sign.id();
						event.left.add(I18n.format("signpic.over.id", id));
						final ImageSize signsize = sign.size();
						final Image image = this.manager.get(id);
						final ImageSize imagesize = image.getSize();
						final ImageSize viewsize = sign.size().getAspectSize(imagesize);
						event.left.add(I18n.format("signpic.over.size", signsize.width, signsize.height, imagesize.width, imagesize.height, viewsize.width, viewsize.height));
						event.left.add(I18n.format("signpic.over.status", image.getStatusMessage()));
						final String advmsg = image.advMessage();
						if (advmsg != null)
							event.left.add(I18n.format("signpic.over.advmsg", advmsg));
						event.left.add(I18n.format("signpic.over.raw", sign.text()));
						event.left.add(I18n.format("signpic.over.local", image.getLocal()));
					}
				}
			}
		}
	}
}