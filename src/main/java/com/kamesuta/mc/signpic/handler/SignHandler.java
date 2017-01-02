package com.kamesuta.mc.signpic.handler;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.kamesuta.mc.bnnwidget.WGui;
import com.kamesuta.mc.bnnwidget.WRenderer;
import com.kamesuta.mc.bnnwidget.component.MPanel;
import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.var.V;
import com.kamesuta.mc.bnnwidget.var.VMotion;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryId.ItemEntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.gui.GuiSignOption;
import com.kamesuta.mc.signpic.gui.SignPicLabel;
import com.kamesuta.mc.signpic.http.shortening.ShortenerApiUtil;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.preview.SignEntity;
import com.kamesuta.mc.signpic.render.OpenGL;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class SignHandler {
	private static @Nullable Field guiEditSignTileEntity;
	private static @Nullable Field guiRepairTextField;
	private static @Nullable Field guiRepairContainer;

	public static void init() {
		try {
			final Field[] fields = GuiEditSign.class.getDeclaredFields();
			for (final Field field : fields)
				if (TileEntitySign.class.equals(field.getType())) {
					field.setAccessible(true);
					guiEditSignTileEntity = field;
				}
		} catch (final SecurityException e) {
			Log.log.error("Could not hook TileEntitySign field included in GuiEditSign", e);
		}
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

	private @Nullable GuiRepair repairGuiTask;
	private @Nullable String repairGuiTextFieldCache;
	private boolean isPlaceMode;

	@CoreEvent
	public void onSign(final @Nonnull GuiOpenEvent event) {
		this.repairGuiTask = null;
		this.repairGuiTextFieldCache = null;
		final EntryId handSign = CurrentMode.instance.getHandSign();
		final boolean handSignValid = handSign.entry().isValid();
		this.isPlaceMode = CurrentMode.instance.isMode(CurrentMode.Mode.PLACE);
		if (handSignValid||this.isPlaceMode) {
			final EntryId entryId = CurrentMode.instance.getEntryId();
			if (event.gui instanceof GuiEditSign) {
				event.setCanceled(true);
				final EntryId placeSign = handSignValid ? handSign : entryId;
				if (placeSign.isPlaceable()) {
					if (guiEditSignTileEntity!=null)
						try {
							final TileEntitySign tileSign = (TileEntitySign) guiEditSignTileEntity.get(event.gui);
							Sign.placeSign(placeSign, tileSign);
							if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE)) {
								CurrentMode.instance.setMode();
								final SignEntity se = Sign.preview;
								if (se.isRenderable()) {
									final BlockPos preview = se.getTileEntity().getPos();
									final BlockPos tileSignPos = tileSign.getPos();
									if (preview.getX()==tileSignPos.getX()&&preview.getY()==tileSignPos.getY()&&preview.getZ()==tileSignPos.getZ()) {
										se.setVisible(false);
										CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);
										CurrentMode.instance.setState(CurrentMode.State.SEE, false);
									}
								}
							}
						} catch (final Exception e) {
							Log.notice(I18n.format("signpic.chat.error.place"));
						}
					else
						Log.notice(I18n.format("signpic.chat.error.place"));
				} else if (CurrentMode.instance.isShortening())
					Log.notice(I18n.format("signpic.gui.notice.shortening"), 1f);
				else
					Log.notice(I18n.format("signpic.gui.notice.toolongplace"), 1f);
			} else if (event.gui instanceof GuiRepair) {
				if (!entryId.isNameable()) {
					final ContentId id = entryId.entry().contentId;
					if (id!=null)
						ShortenerApiUtil.requestShoretning(id);
				}
				this.repairGuiTask = (GuiRepair) event.gui;
				if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE)) {
					CurrentMode.instance.setMode();
					Sign.preview.setVisible(false);
					CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);
					CurrentMode.instance.setState(CurrentMode.State.SEE, false);
				}
			}
		}
	}

	@CoreEvent
	public void onTick() {
		if (this.repairGuiTask!=null&&this.isPlaceMode)
			check: {
				final EntryId entryId = CurrentMode.instance.getEntryId();
				if (!entryId.isNameable())
					break check;
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
						break check;
					} catch (final Exception e) {
						Log.notice(I18n.format("signpic.chat.error.place"));
					}
				Log.notice(I18n.format("signpic.chat.error.place"));
			}
	}

	protected @Nonnull VMotion o = V.pm(0f).add(Easings.easeOutSine.move(1f, 1f)).add(Easings.easeInSine.move(1f, 0f)).setLoop(true).start();

	@CoreEvent
	public void onDraw(final @Nonnull GuiScreenEvent.DrawScreenEvent.Post event) {
		if (event.gui instanceof GuiRepair)
			if (this.repairGuiTask!=null&&this.isPlaceMode) {
				final int xSize = 176;
				final int ySize = 166;
				final int guiLeft = (event.gui.width-xSize)/2;
				final int guiTop = (event.gui.height-ySize)/2;
				OpenGL.glColor4f(1f, 1f, 1f, 1f);
				WRenderer.startTexture();
				WGui.texture().bindTexture(MPanel.background);
				final Area a = new Area(guiLeft-42, guiTop, guiLeft, guiTop+49);
				MPanel.drawBack(a);
				WGui.texture().bindTexture(SignPicLabel.defaultTexture);
				WGui.drawTextureSize(guiLeft-42, guiTop+3, 42, 42);
				if (CurrentMode.instance.isShortening()) {
					final Area b = new Area(guiLeft, guiTop-20, event.gui.width-guiLeft, guiTop);
					WGui.fontColor(1f, 1f, 1f, this.o.get());
					WGui.drawString(I18n.format("signpic.gui.notice.shortening"), b, WGui.Align.CENTER, WGui.VerticalAlign.MIDDLE, true);
				}
			}
	}

	@CoreEvent
	public void onClick(final @Nonnull MouseEvent event) {
		if (event.buttonstate&&Client.mc.gameSettings.keyBindUseItem.getKeyCode()==event.button-100) {
			final ItemStack handItem = Client.mc.thePlayer.getCurrentEquippedItem();
			EntryId handEntry = null;
			if (handItem!=null&&handItem.getItem()==Items.sign) {
				handEntry = EntryId.fromItemStack(handItem);
				CurrentMode.instance.setHandSign(handEntry);
			} else
				CurrentMode.instance.setHandSign(EntryId.blank);
			if (CurrentMode.instance.isMode(CurrentMode.Mode.SETPREVIEW)) {
				Sign.preview.capturePlace();
				event.setCanceled(true);
				CurrentMode.instance.setMode();
				Client.openEditor();
			} else if (CurrentMode.instance.isMode(CurrentMode.Mode.OPTION)) {
				final TileEntitySign tilesign = Client.getTileSignLooking();
				Entry entry = null;
				if (tilesign!=null)
					entry = EntryId.fromTile(tilesign).entry();
				else if (handEntry!=null)
					entry = handEntry.entry();
				if (entry!=null&&entry.isValid()) {
					event.setCanceled(true);
					Client.mc.displayGuiScreen(new GuiSignOption(entry));
					if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE))
						CurrentMode.instance.setMode();
				}
			}
		}
	}

	@CoreEvent
	public void onTooltip(final @Nonnull ItemTooltipEvent event) {
		if (event.itemStack.getItem()==Items.sign) {
			final ItemEntryId id = EntryId.fromItemStack(event.itemStack);
			final Entry entry = id.entry();
			if (entry.isValid()) {
				final String raw = !event.toolTip.isEmpty() ? event.toolTip.get(0) : "";
				if (id.hasName())
					event.toolTip.set(0, id.getName());
				else if (entry.contentId!=null)
					event.toolTip.set(0, I18n.format("signpic.item.sign.desc.named", entry.contentId.getURI()));
				final KeyBinding sneak = Client.mc.gameSettings.keyBindSneak;
				if (!Keyboard.isKeyDown(sneak.getKeyCode()))
					event.toolTip.add(I18n.format("signpic.item.hold", GameSettings.getKeyDisplayString(sneak.getKeyCode())));
				else {
					final CompoundAttr meta = entry.getMeta();
					final SizeData size = meta.sizes.getMovie().get();
					event.toolTip.add(I18n.format("signpic.item.sign.desc.named.prop.size", size.getWidth(), size.getHeight()));
					final OffsetData offset = meta.offsets.getMovie().get();
					event.toolTip.add(I18n.format("signpic.item.sign.desc.named.prop.offset", offset.x.offset, offset.y.offset, offset.z.offset));
					// event.toolTip.add(I18n.format("signpic.item.sign.desc.named.prop.rotation", meta.rotation.compose()));
					if (id.hasName()&&entry.contentId!=null)
						event.toolTip.add(I18n.format("signpic.item.sign.desc.named.url", entry.contentId.getURI()));
					// event.toolTip.add(I18n.format("signpic.item.sign.desc.named.meta", meta.compose()));
					event.toolTip.add(I18n.format("signpic.item.sign.desc.named.raw", raw));
				}
			} else if (Config.getConfig().signTooltip.get()||!Config.getConfig().guiExperienced.get()) {
				final KeyBinding binding = KeyHandler.Keys.KEY_BINDING_GUI.binding;
				final List<KeyBinding> conflict = KeyHandler.getKeyConflict(binding);
				String keyDisplay = GameSettings.getKeyDisplayString(binding.getKeyCode());
				if (!conflict.isEmpty())
					keyDisplay = EnumChatFormatting.RED+keyDisplay;
				event.toolTip.add(I18n.format("signpic.item.sign.desc", keyDisplay));
				if (!conflict.isEmpty()) {
					event.toolTip.add(I18n.format("signpic.item.sign.desc.keyconflict", I18n.format("menu.options"), I18n.format("options.controls")));
					for (final KeyBinding key : conflict)
						event.toolTip.add(I18n.format("signpic.item.sign.desc.keyconflict.key", I18n.format(key.getKeyCategory()), I18n.format(key.getKeyDescription())));
				}
			}
		}
	}
}