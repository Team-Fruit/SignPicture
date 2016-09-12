package com.kamesuta.mc.signpic.render;

import com.kamesuta.mc.bnnwidget.WGui;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.handler.CoreEvent;
import com.kamesuta.mc.signpic.image.Image;
import com.kamesuta.mc.signpic.image.ImageManager;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class SignPicRender extends WGui {
	public static final ResourceLocation resSign = new ResourceLocation("textures/items/sign.png");

	protected final ImageManager manager;

	public SignPicRender(final ImageManager manager) {
		this.manager = manager;
	}

	@CoreEvent
	public void onRender(final RenderWorldLastEvent event) {
		if (CurrentMode.instance.isState(CurrentMode.State.PREVIEW)) {
			if (CurrentMode.instance.getSign().preview.isRenderable()) {
				final TileEntitySign tile = CurrentMode.instance.getSign().updatePreview().preview.getRenderTileEntity();
				final BlockPos pos = tile.getPos();
				Client.renderer.renderTileEntityAt(tile, pos.getX() - TileEntityRendererDispatcher.staticPlayerX, pos.getY() - TileEntityRendererDispatcher.staticPlayerY, pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ, event.partialTicks, -1);
			}
		}
	}

	@CoreEvent
	public void onDraw(final RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.EXPERIENCE)
			if (CurrentMode.instance.isMode()) {
				if ((int)(System.currentTimeMillis()/500)%2==0) {
					final FontRenderer fontrenderer = font();

					RenderHelper.startTexture();
					GlStateManager.pushMatrix();
					GlStateManager.translate(5f, 5f, 0f);
					GlStateManager.scale(2f, 2f, 1f);

					GlStateManager.pushMatrix();
					GlStateManager.scale(fontrenderer.FONT_HEIGHT, fontrenderer.FONT_HEIGHT, 1f);
					this.manager.get(resSign).draw();
					GlStateManager.popMatrix();

					GlStateManager.translate(fontrenderer.FONT_HEIGHT, 0f, 0f);
					final String str = I18n.format(CurrentMode.instance.getMode().message);
					fontrenderer.drawStringWithShadow(str, 0, 0, 0xffffff);

					GlStateManager.popMatrix();
				}
			}
	}

	@CoreEvent
	public void onText(final RenderGameOverlayEvent.Text event) {
		if (Client.mc.gameSettings.showDebugInfo) {
			final TileEntitySign tilesign = Client.getTileSignLooking();
			if (tilesign != null) {
				final Sign sign = new Sign().parseSignEntity(tilesign);
				if (sign.isVaild()) {
					final String id = sign.getURL();
					final ImageSize signsize = sign.meta.size;
					final Image image = this.manager.get(id);
					final ImageSize imagesize = image.getSize();
					final ImageSize viewsize = new ImageSize().setAspectSize(sign.meta.size, imagesize);
					final String advmsg = image.advMessage();

					event.left.add("");
					event.left.add(I18n.format("signpic.over.sign", sign.text()));
					event.left.add(I18n.format("signpic.over.id", id));
					event.left.add(I18n.format("signpic.over.size", signsize, signsize.width, signsize.height, imagesize.width, imagesize.height, viewsize.width, viewsize.height));
					event.left.add(I18n.format("signpic.over.status", image.getStatusMessage()));
					if (advmsg != null)
						event.left.add(I18n.format("signpic.over.advmsg", advmsg));
					if (tilesign.signText != null)
						event.left.add(I18n.format("signpic.over.raw", tilesign.signText[0], tilesign.signText[1], tilesign.signText[2], tilesign.signText[3]));
					event.left.add(I18n.format("signpic.over.local", image.getLocal()));
				}
			}
		}
	}
}