package com.kamesuta.mc.signpic.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.WGui;
import com.kamesuta.mc.bnnwidget.WRenderer;
import com.kamesuta.mc.bnnwidget.component.MPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.gui.SignPicLabel;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import tconstruct.TConstruct;
import tconstruct.tools.gui.ToolStationGui;
import tconstruct.util.network.ToolStationPacket;

public class ToolStationHandler implements INameHandler {
	private @Nullable GuiScreen toolGuiTask;
	private @Nullable String toolGuiTextFieldCache;

	@Override
	public void reset() {
		this.toolGuiTask = null;
		this.toolGuiTextFieldCache = null;
	}

	@Override
	public boolean onOpen(final @Nullable GuiScreen gui, final @Nonnull EntryId currentId) {
		this.toolGuiTask = gui;
		return gui instanceof ToolStationGui;
	}

	@Override
	public void onTick() {
		final GuiScreen guiScreen = this.toolGuiTask;
		if (this.toolGuiTask==null||!(guiScreen instanceof ToolStationGui))
			return;
		final EntryId entryId = CurrentMode.instance.getEntryId();
		if (!entryId.isNameable())
			return;
		try {
			final ToolStationGui gui = (ToolStationGui) guiScreen;
			final String text = gui.text.getText();
			if (!StringUtils.equals(this.toolGuiTextFieldCache, text)) {
				final String name = entryId.id();
				gui.text.setText(name);
				gui.logic.setToolname(name);
				TConstruct.packetPipeline.sendToServer(new ToolStationPacket(gui.logic.xCoord, gui.logic.yCoord, gui.logic.zCoord, name));
				this.toolGuiTextFieldCache = name;
			}
			return;
		} catch (final Throwable e) {
			Log.notice(I18n.format("signpic.chat.error.place"));
		}
	}

	protected @Nonnull VMotion o = V.pm(0f).add(Easings.easeOutSine.move(1f, 1f)).add(Easings.easeInSine.move(1f, 0f)).setLoop(true).start();

	@Override
	public void onDraw(final @Nullable GuiScreen gui) {
		if (gui instanceof ToolStationGui&&this.toolGuiTask!=null) {
			final int xSize = 176;
			final int ySize = 166;
			final int guiLeft = (gui.width-xSize)/2;
			final int guiTop = (gui.height-ySize)/2;
			OpenGL.glColor4f(1f, 1f, 1f, 1f);
			WRenderer.startTexture();
			WGui.texture().bindTexture(MPanel.background);
			final Area a = new Area(guiLeft-42, guiTop+ySize-49, guiLeft, guiTop+ySize);
			MPanel.drawBack(a);
			WGui.texture().bindTexture(SignPicLabel.defaultTexture);
			WGui.drawTextureSize(guiLeft-42, guiTop+ySize-49+3, 42, 42);
			if (CurrentMode.instance.isShortening()) {
				final Area b = new Area(guiLeft, guiTop-20, gui.width-guiLeft, guiTop);
				WGui.fontColor(1f, 1f, 1f, this.o.get());
				WGui.drawString(I18n.format("signpic.gui.notice.shortening"), b, WGui.Align.CENTER, WGui.VerticalAlign.MIDDLE, true);
			}
		}
	}
}
