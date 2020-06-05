package net.teamfruit.signpic.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerRepair;
import net.teamfruit.bnnwidget.compat.OpenGL;
import net.teamfruit.bnnwidget.component.MPanel;
import net.teamfruit.bnnwidget.motion.Easings;
import net.teamfruit.bnnwidget.position.Area;
import net.teamfruit.bnnwidget.render.WGui;
import net.teamfruit.bnnwidget.render.WRenderer;
import net.teamfruit.bnnwidget.var.V;
import net.teamfruit.bnnwidget.var.VMotion;
import net.teamfruit.signpic.Log;
import net.teamfruit.signpic.entry.EntryId;
import net.teamfruit.signpic.gui.SignPicLabel;
import net.teamfruit.signpic.mode.CurrentMode;
import net.teamfruit.signpic.reflect.lib.ReflectClass;
import net.teamfruit.signpic.reflect.lib.ReflectField;
import net.teamfruit.signpic.util.Sign;

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
