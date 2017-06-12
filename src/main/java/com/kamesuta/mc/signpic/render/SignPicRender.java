package com.kamesuta.mc.signpic.render;

import static com.kamesuta.mc.bnnwidget.render.WRenderer.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId.SignEntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class SignPicRender {
	public static final @Nonnull ResourceLocation resSign = new ResourceLocation("textures/items/sign.png");

	public SignPicRender() {
	}

	@CoreEvent
	public void onRender(final @Nonnull RenderWorldLastEvent event) {
		float opacity = Config.getConfig().renderPreviewFixedOpacity.get().floatValue();
		if (CurrentMode.instance.isMode(CurrentMode.Mode.SETPREVIEW)||CurrentMode.instance.isMode(CurrentMode.Mode.PLACE)) {
			Sign.preview.capturePlace();
			opacity = Config.getConfig().renderPreviewFloatedOpacity.get().floatValue();
		}
		if (CurrentMode.instance.isState(CurrentMode.State.PREVIEW))
			if (Sign.preview.isRenderable()&&Sign.preview.isVisible()) {
				final TileEntitySign tile = Sign.preview.getRenderTileEntity();
				final BlockPos pos = tile.getPos();
				Client.renderer.renderSignPictureBase(tile, pos.getX()-TileEntityRendererDispatcher.staticPlayerX, pos.getY()-TileEntityRendererDispatcher.staticPlayerY, pos.getZ()-TileEntityRendererDispatcher.staticPlayerZ, event.getPartialTicks(), -1, opacity);
			}
	}

	@CoreEvent
	public void onDraw(final @Nonnull RenderGameOverlayEvent.Post event) {
		if (event.getType()==ElementType.EXPERIENCE)
			if (CurrentMode.instance.isMode())
				if ((int) (System.currentTimeMillis()/500)%2==0) {
					final FontRenderer fontrenderer = font();

					WRenderer.startTexture();
					OpenGL.glPushMatrix();
					OpenGL.glTranslatef(5f, 5f, 0f);
					OpenGL.glScalef(2f, 2f, 1f);

					OpenGL.glPushMatrix();
					OpenGL.glScalef(fontrenderer.FONT_HEIGHT, fontrenderer.FONT_HEIGHT, 1f);
					OpenGL.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

					texture().bindTexture(resSign);
					WRenderer.startTexture();
					RenderHelper.drawRectTexture(GL_QUADS);

					OpenGL.glPopMatrix();

					OpenGL.glTranslatef(fontrenderer.FONT_HEIGHT, 0f, 0f);
					final String str = I18n.format(CurrentMode.instance.getMode().message);
					fontrenderer.drawStringWithShadow(str, 0, 0, 0xffffff);

					OpenGL.glPopMatrix();
				}
	}

	@CoreEvent
	public void onText(final @Nonnull RenderGameOverlayEvent.Text event) {
		if (Client.mc.gameSettings.showDebugInfo) {
			final TileEntitySign tilesign = Client.getTileSignLooking();
			if (tilesign!=null) {
				final Entry entry = SignEntryId.fromTile(tilesign).entry();
				if (entry.isValid()) {
					final AttrReaders meta = entry.getMeta();
					final SizeData signsize = meta.sizes.getMovie().get();

					final ArrayList<String> left = event.getLeft();

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