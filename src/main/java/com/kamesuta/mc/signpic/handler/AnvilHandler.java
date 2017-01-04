package com.kamesuta.mc.signpic.handler;

import java.lang.reflect.Field;

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
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerRepair;

public class AnvilHandler implements INameHandler {
	private static @Nullable Field guiRepairTextField;
	private static @Nullable Field guiRepairContainer;

	private @Nullable GuiScreen repairGuiTask;
	private @Nullable String repairGuiTextFieldCache;

	public AnvilHandler() {
		try {
			final Field[] fields = GuiRepair.class.getDeclaredFields();
			for (final Field field : fields) {
				final Class<?> type = field.getType();
				if (GuiTextField.class.equals(type)) {
					field.setAccessible(true);
					guiRepairTextField = field;
				} else if (ContainerRepair.class.equals(type)) {
					field.setAccessible(true);
					guiRepairContainer = field;
				}
			}
		} catch (final SecurityException e) {
			Log.log.error("Could not hook GuiTextField or ContainerRepair field included in GuiRepair", e);
		}
	}

	@Override
	public void reset() {
		this.repairGuiTask = null;
		this.repairGuiTextFieldCache = null;
	}

	@Override
	public boolean onOpen(final @Nullable GuiScreen gui, final @Nonnull EntryId currentId) {
		this.repairGuiTask = gui;
		return gui instanceof GuiRepair;
	}

	@Override
	public void onTick() {
		if (this.repairGuiTask==null||!(this.repairGuiTask instanceof GuiRepair))
			return;
		final EntryId entryId = CurrentMode.instance.getEntryId();
		if (!entryId.isNameable())
			return;
		final Field fTextField = guiRepairTextField;
		final Field fContainer = guiRepairContainer;
		if (fTextField!=null&&fContainer!=null)
			try {
				final GuiTextField textField = (GuiTextField) fTextField.get(this.repairGuiTask);
				final ContainerRepair containerRepair = (ContainerRepair) fContainer.get(this.repairGuiTask);
				if (textField!=null&&containerRepair!=null) {
					final String text = textField.getText();
					if (!StringUtils.isEmpty(text)&&!StringUtils.equals(this.repairGuiTextFieldCache, text)) {
						final String name = entryId.id();
						Sign.setRepairName(name, textField, containerRepair);
						this.repairGuiTextFieldCache = name;
					}
				}
				return;
			} catch (final Exception e) {
				Log.notice(I18n.format("signpic.chat.error.place"));
			}
		Log.notice(I18n.format("signpic.chat.error.place"));
	}

	protected @Nonnull VMotion o = V.pm(0f).add(Easings.easeOutSine.move(1f, 1f)).add(Easings.easeInSine.move(1f, 0f)).setLoop(true).start();

	@Override
	public void onDraw(final @Nullable GuiScreen gui) {
		if (gui instanceof GuiRepair&&this.repairGuiTask!=null) {
			final int xSize = 176;
			final int ySize = 166;
			final int guiLeft = (gui.width-xSize)/2;
			final int guiTop = (gui.height-ySize)/2;
			OpenGL.glColor4f(1f, 1f, 1f, 1f);
			WRenderer.startTexture();
			WGui.texture().bindTexture(MPanel.background);
			final Area a = new Area(guiLeft-42, guiTop, guiLeft, guiTop+49);
			MPanel.drawBack(a);
			WGui.texture().bindTexture(SignPicLabel.defaultTexture);
			WGui.drawTextureSize(guiLeft-42, guiTop+3, 42, 42);
			if (CurrentMode.instance.isShortening()) {
				final Area b = new Area(guiLeft, guiTop-20, gui.width-guiLeft, guiTop);
				WGui.fontColor(1f, 1f, 1f, this.o.get());
				WGui.drawString(I18n.format("signpic.gui.notice.shortening"), b, WGui.Align.CENTER, WGui.VerticalAlign.MIDDLE, true);
			}
		}
	}
}
