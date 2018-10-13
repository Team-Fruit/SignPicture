package com.kamesuta.mc.signpic.render;

import static com.kamesuta.mc.bnnwidget.render.WRenderer.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.compat.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.compat.Compat.CompatBlockPos;
import com.kamesuta.mc.signpic.compat.Compat.CompatFontRenderer;
import com.kamesuta.mc.signpic.compat.Compat.CompatMinecraft;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatRenderGameOverlayEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatRenderGameOverlayEvent.CompatElementType;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatRenderWorldLastEvent;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId.SignEntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class SignPicRender {
	public static final @Nonnull ResourceLocation resSign = new ResourceLocation("textures/items/sign.png");

	public SignPicRender() {
	}

	@CoreEvent
	public void onRender(final @Nonnull CompatRenderWorldLastEvent event) {
		float opacity = Config.getConfig().renderPreviewFixedOpacity.get().floatValue();
		if (CurrentMode.instance.isMode(CurrentMode.Mode.SETPREVIEW)||CurrentMode.instance.isMode(CurrentMode.Mode.PLACE)) {
			Sign.preview.capturePlace();
			opacity = Config.getConfig().renderPreviewFloatedOpacity.get().floatValue();
		}
		if (CurrentMode.instance.isState(CurrentMode.State.PREVIEW))
			if (Sign.preview.isRenderable()&&Sign.preview.isVisible()) {
				final TileEntitySign tile = Sign.preview.getRenderTileEntity();
				final CompatBlockPos pos = CompatBlockPos.getTileEntityPos(tile);
				Client.rendererTile.renderSignPictureBase(tile, pos.getX()-TileEntityRendererDispatcher.staticPlayerX, pos.getY()-TileEntityRendererDispatcher.staticPlayerY, pos.getZ()-TileEntityRendererDispatcher.staticPlayerZ, event.getPartialTicks(), -1, opacity);
			}
	}

	@CoreEvent
	public void onDraw(final @Nonnull CompatRenderGameOverlayEvent.CompatPost event) {
		if (event.getType()==CompatElementType.EXPERIENCE)
			if (CurrentMode.instance.isMode())
				if ((int) (System.currentTimeMillis()/500)%2==0) {
					final CompatFontRenderer fontrenderer = CompatMinecraft.getFontRenderer();

					WRenderer.startTexture();
					OpenGL.glPushMatrix();
					OpenGL.glTranslatef(5f, 5f, 0f);
					OpenGL.glScalef(2f, 2f, 1f);

					OpenGL.glPushMatrix();
					OpenGL.glScalef(fontrenderer.getFontRendererObj().FONT_HEIGHT, fontrenderer.getFontRendererObj().FONT_HEIGHT, 1f);
					OpenGL.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

					texture().bindTexture(resSign);
					WRenderer.startTexture();
					RenderHelper.drawRectTexture(GL_QUADS);

					OpenGL.glPopMatrix();

					OpenGL.glTranslatef(fontrenderer.getFontRendererObj().FONT_HEIGHT, 0f, 0f);
					final String str = I18n.format(CurrentMode.instance.getMode().message);
					fontrenderer.drawStringWithShadow(str, 0, 0, 0xffffff);

					OpenGL.glPopMatrix();
				}
	}

	@CoreEvent
	public void onText(final @Nonnull CompatRenderGameOverlayEvent.CompatText event) {
		if (Client.mc.gameSettings.showDebugInfo) {
			final TileEntitySign tilesign = Client.getTileSignLooking();
			if (tilesign!=null) {
				final Entry entry = SignEntryId.fromTile(tilesign).entry();
				if (entry.isValid()) {
					final AttrReaders meta = entry.getMeta();
					final SizeData signsize = meta.sizes.getMovie().get();
					final List<String> left = event.getLeft();

					left.add("");
					left.add(I18n.format("signpic.over.sign", entry.id.id()));
					if (entry.contentId!=null)
						left.add(I18n.format("signpic.over.id", entry.contentId.getURI()));
					final Content content = entry.getContent();
					final SizeData imagesize = content!=null ? content.image.getSize() : SizeData.UnknownSize;
					final SizeData viewsize = signsize.aspectSize(imagesize);
					left.add(I18n.format("signpic.over.size", signsize, signsize.getWidth(), signsize.getHeight(), imagesize.getWidth(), imagesize.getHeight(), viewsize.getWidth(), viewsize.getHeight()));
					if (content!=null)
						left.add(I18n.format("signpic.over.status", content.state.getStateMessage()));
					if (entry.isNotSupported())
						left.add(I18n.format("signpic.over.advmsg", I18n.format("signpic.state.format.unsupported")));
					else if (content!=null) {
						final String advmsg = content.state.getMessage();
						left.add(I18n.format("signpic.over.advmsg", advmsg));
					}
					if (tilesign.signText!=null)
						left.add(I18n.format("signpic.over.raw", tilesign.signText[0], tilesign.signText[1], tilesign.signText[2], tilesign.signText[3]));
					if (content!=null)
						left.add(I18n.format("signpic.over.local", content.image.getLocal()));
				}
			}
		}
	}
}