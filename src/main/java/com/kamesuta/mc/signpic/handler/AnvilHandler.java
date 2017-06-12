package com.kamesuta.mc.signpic.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.component.MPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.render.OpenGL;
import com.kamesuta.mc.bnnwidget.render.WGui;
import com.kamesuta.mc.bnnwidget.render.WRenderer;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.gui.SignPicLabel;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.reflect.lib.ReflectClass;
import com.kamesuta.mc.signpic.reflect.lib.ReflectField;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerRepair;

public class AnvilHandler implements INameHandler {
	private static @Nonnull ReflectField<GuiRepair, GuiTextField> guiRepairTextField = ReflectClass.fromClass(GuiRepair.class).getFieldFromType(null, GuiTextField.class);
	private static @Nonnull ReflectField<GuiRepair, ContainerRepair> guiRepairContainer = ReflectClass.fromClass(GuiRepair.class).getFieldFromType(null, ContainerRepair.class);

	private @Nullable GuiRepair repairGuiTask;
	private @Nullable String repairGuiTextFieldCache;

	public AnvilHandler() {
	}

	@Override
	public void reset() {
		this.repairGuiTask = null;
		this.repairGuiTextFieldCache = null;
	}

	@Override
	public boolean onOpen(final @Nullable GuiScreen gui, final @Nonnull EntryId currentId) {
		if (gui instanceof GuiRepair) {
			this.repairGuiTask = (GuiRepair) gui;
			return true;
		}
		return false;
	}

	@Override
	public void onTick() {
		if (this.repairGuiTask==null||!(this.repairGuiTask instanceof GuiRepair))
			return;
		final EntryId entryId = CurrentMode.instance.getEntryId();
		if (!entryId.isNameable())
			return;
		try {
			final GuiTextField textField = guiRepairTextField.get(this.repairGuiTask);
			final ContainerRepair containerRepair = guiRepairContainer.get(this.repairGuiTask);
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
			WRenderer.texture().bindTexture(MPanel.background);
			final Area a = Area.abs(guiLeft-42, guiTop, guiLeft, guiTop+49);
			MPanel.drawBack(a);
			WRenderer.texture().bindTexture(SignPicLabel.defaultTexture);
			WGui.drawTexture(Area.size(guiLeft-42, guiTop+3, 42, 42), null, null);
			if (CurrentMode.instance.isShortening()) {
				final Area b = Area.abs(guiLeft, guiTop-20, gui.width-guiLeft, guiTop);
				OpenGL.glColor4f(1f, 1f, 1f, this.o.get());
				WGui.drawString(I18n.format("signpic.gui.notice.shortening"), b, WGui.Align.CENTER, WGui.VerticalAlign.MIDDLE, true);
			}
		}
	}
}
