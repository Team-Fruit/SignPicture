package com.kamesuta.mc.signpic.handler;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;
import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.prop.OffsetData;
import com.kamesuta.mc.signpic.attr.prop.SizeData;
import com.kamesuta.mc.signpic.compat.Compat.CompatBlockPos;
import com.kamesuta.mc.signpic.compat.Compat.CompatEntityPlayer;
import com.kamesuta.mc.signpic.compat.Compat.CompatItems;
import com.kamesuta.mc.signpic.compat.Compat.CompatMinecraft;
import com.kamesuta.mc.signpic.compat.Compat.CompatTextFormatting;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatGuiOpenEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatGuiScreenEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatItemTooltipEvent;
import com.kamesuta.mc.signpic.compat.CompatEvents.CompatMouseEvent;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryId.ItemEntryId;
import com.kamesuta.mc.signpic.entry.EntryId.SignEntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.gui.GuiSignOption;
import com.kamesuta.mc.signpic.http.shortening.ShortenerApiUtil;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.preview.SignEntity;
import com.kamesuta.mc.signpic.reflect.lib.ReflectClass;
import com.kamesuta.mc.signpic.reflect.lib.ReflectField;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;

public class SignHandler {
	public static @Nonnull ReflectField<GuiEditSign, TileEntitySign> guiEditSignTileEntity = ReflectClass.fromClass(GuiEditSign.class).getFieldFromType(null, TileEntitySign.class);
	private static @Nonnull Set<INameHandler> handlers = Sets.newHashSet();

	public static void init() {
		handlers.add(new AnvilHandler());
	}

	private boolean isPlaceMode;

	@CoreEvent
	public void onSign(final @Nonnull CompatGuiOpenEvent event) {
		for (final INameHandler handler : handlers)
			if (handler!=null)
				handler.reset();
		final EntryId handSign = CurrentMode.instance.getHandSign();
		final boolean handSignValid = handSign.entry().isValid();
		this.isPlaceMode = CurrentMode.instance.isMode(CurrentMode.Mode.PLACE);
		if (handSignValid||this.isPlaceMode) {
			final EntryId entryId = CurrentMode.instance.getEntryId();
			final GuiScreen gui = event.getGui();
			if (gui instanceof GuiEditSign) {
				event.setCanceled(true);
				final EntryId placeSign = handSignValid ? handSign : entryId;
				if (placeSign.isPlaceable())
					try {
						final TileEntitySign tileSign = guiEditSignTileEntity.get((GuiEditSign) gui);
						if (tileSign!=null) {
							Sign.placeSign(placeSign, tileSign);
							if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE)) {
								CurrentMode.instance.setMode();
								final SignEntity se = Sign.preview;
								if (se.isRenderable()) {
									final TileEntitySign preview = se.getTileEntity();
									final CompatBlockPos previewpos = CompatBlockPos.getTileEntityPos(preview);
									final CompatBlockPos tilepos = CompatBlockPos.getTileEntityPos(tileSign);
									if (previewpos.equals(tilepos)) {
										se.setVisible(false);
										CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);
										CurrentMode.instance.setState(CurrentMode.State.SEE, false);
									}
								}
							}
						} else
							Log.notice(I18n.format("signpic.chat.error.place"));
					} catch (final Exception e) {
						Log.notice(I18n.format("signpic.chat.error.place"));
					}
				else if (CurrentMode.instance.isShortening())
					Log.notice(I18n.format("signpic.gui.notice.shortening"), 1f);
				else
					Log.notice(I18n.format("signpic.gui.notice.toolongplace"), 1f);
			} else {
				boolean b = false;
				for (final INameHandler handler : handlers)
					if (handler!=null)
						b = handler.onOpen(gui, entryId)||b;
				if (b) {
					if (!entryId.isNameable()) {
						final ContentId id = entryId.entry().contentId;
						if (id!=null)
							ShortenerApiUtil.requestShoretning(id);
					}
					if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE)) {
						CurrentMode.instance.setMode();
						Sign.preview.setVisible(false);
						CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);
						CurrentMode.instance.setState(CurrentMode.State.SEE, false);
					}
				}
			}
		}
	}

	@CoreEvent
	public void onTick() {
		if (this.isPlaceMode)
			for (final INameHandler handler : handlers)
				if (handler!=null)
					handler.onTick();
	}

	@CoreEvent
	public void onDraw(final @Nonnull CompatGuiScreenEvent.CompatDrawScreenEvent.CompatPost event) {
		if (this.isPlaceMode) {
			final GuiScreen gui = event.getGui();
			for (final INameHandler handler : handlers)
				if (handler!=null&&gui!=null)
					handler.onDraw(gui);
		}
	}

	@CoreEvent
	public void onClick(final @Nonnull CompatMouseEvent event) {
		if (event.getButtonState()&&Client.mc.gameSettings.keyBindUseItem.getKeyCode()==event.getButton()-100) {
			final CompatEntityPlayer player = CompatMinecraft.getPlayer();
			if (player!=null) {
				ItemStack handItem = player.getHeldItemMainhand();
				if (handItem==null||handItem.getItem()!=CompatItems.SIGN)
					handItem = player.getHeldItemOffhand();
				EntryId handEntry = null;
				if (handItem!=null&&handItem.getItem()==CompatItems.SIGN) {
					handEntry = ItemEntryId.fromItemStack(handItem);
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
						entry = SignEntryId.fromTile(tilesign).entry();
					else if (handEntry!=null)
						entry = handEntry.entry();
					if (entry!=null) {
						event.setCanceled(true);
						WFrame.displayFrame(new GuiSignOption(entry));
						if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE))
							CurrentMode.instance.setMode();
					}
				}
			}
		}
	}

	@CoreEvent
	public void onTooltip(final @Nonnull CompatItemTooltipEvent event) {
		final ItemStack itemStack = event.getItemStack();
		final List<String> tooltip = event.getTooltip();
		if (itemStack.getItem()==CompatItems.SIGN) {
			final ItemEntryId id = ItemEntryId.fromItemStack(itemStack);
			final Entry entry = id.entry();
			if (entry.isValid()) {
				final String raw = !tooltip.isEmpty() ? tooltip.get(0) : "";
				if (id.hasName())
					tooltip.set(0, id.getName());
				else if (entry.contentId!=null)
					tooltip.set(0, I18n.format("signpic.item.sign.desc.named", entry.contentId.getURI()));
				final KeyBinding sneak = Client.mc.gameSettings.keyBindSneak;
				if (!Keyboard.isKeyDown(sneak.getKeyCode()))
					tooltip.add(I18n.format("signpic.item.hold", GameSettings.getKeyDisplayString(sneak.getKeyCode())));
				else {
					final AttrReaders meta = entry.getMeta();
					final SizeData size = meta.sizes.getMovie().get();
					tooltip.add(I18n.format("signpic.item.sign.desc.named.prop.size", size.getWidth(), size.getHeight()));
					final OffsetData offset = meta.offsets.getMovie().get();
					tooltip.add(I18n.format("signpic.item.sign.desc.named.prop.offset", offset.x.offset, offset.y.offset, offset.z.offset));
					// tooltip.add(I18n.format("signpic.item.sign.desc.named.prop.rotation", meta.rotation.compose()));
					if (id.hasName()&&entry.contentId!=null)
						tooltip.add(I18n.format("signpic.item.sign.desc.named.url", entry.contentId.getURI()));
					// tooltip.add(I18n.format("signpic.item.sign.desc.named.meta", meta.compose()));
					tooltip.add(I18n.format("signpic.item.sign.desc.named.raw", raw));
				}
			} else if (Config.getConfig().signTooltip.get()||!Config.getConfig().guiExperienced.get()) {
				final KeyBinding binding = KeyHandler.Keys.KEY_BINDING_GUI.binding;
				final List<KeyBinding> conflict = KeyHandler.getKeyConflict(binding);
				String keyDisplay = GameSettings.getKeyDisplayString(binding.getKeyCode());
				if (!conflict.isEmpty())
					keyDisplay = CompatTextFormatting.RED+keyDisplay;
				tooltip.add(I18n.format("signpic.item.sign.desc", keyDisplay));
				if (!conflict.isEmpty()) {
					tooltip.add(I18n.format("signpic.item.sign.desc.keyconflict", I18n.format("menu.options"), I18n.format("options.controls")));
					for (final KeyBinding key : conflict)
						tooltip.add(I18n.format("signpic.item.sign.desc.keyconflict.key", I18n.format(key.getKeyCategory()), I18n.format(key.getKeyDescription())));
				}
			}
		}
	}
}