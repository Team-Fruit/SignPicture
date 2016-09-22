package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WGui;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryManager;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.handler.CoreEvent;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class SignPicRender extends WGui {
	public static final ContentId resSign = ContentId.fromResource(new ResourceLocation("textures/items/sign.png"));

	protected final ContentManager manager;

	public SignPicRender(final ContentManager manager) {
		this.manager = manager;
	}

	@CoreEvent
	public void onRender(final RenderWorldLastEvent event) {
		if (CurrentMode.instance.isMode(CurrentMode.Mode.SETPREVIEW))
			Sign.preview.capturePlace();
		if (CurrentMode.instance.isState(CurrentMode.State.PREVIEW)) {
			if (Sign.preview.isRenderable() && Sign.preview.isVisible()) {
				final TileEntitySign tile = Sign.preview.getRenderTileEntity();
				Client.renderer.renderTileEntityAt(tile, tile.xCoord - TileEntityRendererDispatcher.staticPlayerX, tile.yCoord - TileEntityRendererDispatcher.staticPlayerY, tile.zCoord - TileEntityRendererDispatcher.staticPlayerZ, event.partialTicks);
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
					glPushMatrix();
					glTranslatef(5f, 5f, 0f);
					glScalef(2f, 2f, 1f);

					glPushMatrix();
					glScalef(fontrenderer.FONT_HEIGHT, fontrenderer.FONT_HEIGHT, 1f);
					glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					this.manager.get(resSign).image.draw();
					glPopMatrix();

					glTranslatef(fontrenderer.FONT_HEIGHT, 0f, 0f);
					final String str = I18n.format(CurrentMode.instance.getMode().message);
					fontrenderer.drawStringWithShadow(str, 0, 0, 0xffffff);

					glPopMatrix();
				}
			}
	}

	@CoreEvent
	public void onText(final RenderGameOverlayEvent.Text event) {
		if (Client.mc.gameSettings.showDebugInfo) {
			final TileEntitySign tilesign = Client.getTileSignLooking();
			if (tilesign != null) {
				final Entry entry = EntryManager.instance.get(EntryId.fromTile(tilesign));
				if (entry.isValid()) {
					final String uri = entry.contentId.getURI();
					final ImageSize signsize = entry.meta.size;
					final Content content = this.manager.get(new ContentId(uri));
					final ImageSize imagesize = content.image.getSize();
					final ImageSize viewsize = new ImageSize().setAspectSize(entry.meta.size, imagesize);
					final String advmsg = content.state.getMessage();

					event.left.add("");
					event.left.add(I18n.format("signpic.over.sign", entry.id.id()));
					event.left.add(I18n.format("signpic.over.id", uri));
					event.left.add(I18n.format("signpic.over.size", signsize, signsize.width, signsize.height, imagesize.width, imagesize.height, viewsize.width, viewsize.height));
					event.left.add(I18n.format("signpic.over.status", content.state.getMessage()));
					if (advmsg != null)
						event.left.add(I18n.format("signpic.over.advmsg", advmsg));
					if (tilesign.signText != null)
						event.left.add(I18n.format("signpic.over.raw", tilesign.signText[0], tilesign.signText[1], tilesign.signText[2], tilesign.signText[3]));
					event.left.add(I18n.format("signpic.over.local", content.image.getLocal()));
				}
			}
		}
	}
}