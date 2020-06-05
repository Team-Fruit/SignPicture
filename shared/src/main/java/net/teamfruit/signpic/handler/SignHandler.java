package net.teamfruit.signpic.handler;

import static net.teamfruit.signpic.reflect.lib.ReflectionUtil.*;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Sets;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.teamfruit.bnnwidget.WFrame;
import net.teamfruit.signpic.Client;
import net.teamfruit.signpic.Config;
import net.teamfruit.signpic.CoreEvent;
import net.teamfruit.signpic.Log;
import net.teamfruit.signpic.attr.AttrReaders;
import net.teamfruit.signpic.attr.prop.OffsetData;
import net.teamfruit.signpic.attr.prop.SizeData;
import net.teamfruit.signpic.compat.Compat.CompatBlockPos;
import net.teamfruit.signpic.compat.Compat.CompatEntityPlayer;
import net.teamfruit.signpic.compat.Compat.CompatItems;
import net.teamfruit.signpic.compat.Compat.CompatMessage;
import net.teamfruit.signpic.compat.Compat.CompatMinecraft;
import net.teamfruit.signpic.compat.Compat.CompatSimpleNetworkWrapper;
import net.teamfruit.signpic.compat.Compat.CompatTextFormatting;
import net.teamfruit.signpic.compat.CompatEvents.CompatGuiOpenEvent;
import net.teamfruit.signpic.compat.CompatEvents.CompatGuiScreenEvent;
import net.teamfruit.signpic.compat.CompatEvents.CompatItemTooltipEvent;
import net.teamfruit.signpic.compat.CompatEvents.CompatMouseEvent;
import net.teamfruit.signpic.entry.Entry;
import net.teamfruit.signpic.entry.EntryId;
import net.teamfruit.signpic.entry.EntryId.ItemEntryId;
import net.teamfruit.signpic.entry.EntryId.SignEntryId;
import net.teamfruit.signpic.entry.content.ContentId;
import net.teamfruit.signpic.gui.GuiSignOption;
import net.teamfruit.signpic.http.shortening.ShortenerApiUtil;
import net.teamfruit.signpic.mode.CurrentMode;
import net.teamfruit.signpic.preview.SignEntity;
import net.teamfruit.signpic.reflect.lib.ReflectClass;
import net.teamfruit.signpic.reflect.lib.ReflectField;
import net.teamfruit.signpic.reflect.lib.ReflectionUtil._Class;
import net.teamfruit.signpic.reflect.lib.ReflectionUtil._Constructor;
import net.teamfruit.signpic.reflect.lib.ReflectionUtil._Field;
import net.teamfruit.signpic.util.Sign;

public class SignHandler {
	public static @Nonnull ReflectField<GuiEditSign, TileEntitySign> guiEditSignTileEntity = ReflectClass.fromClass(GuiEditSign.class).getFieldFromType(null, TileEntitySign.class);
	private static @Nonnull Set<INameHandler> handlers = Sets.newHashSet();

	public static void init() {
		handlers.add(new AnvilHandler());
	}

	private boolean isPlaceMode;

	private static class MoarSignCompat {
		public static MoarSignCompat instance;

		static {
			try {
				instance = new MoarSignCompat();
			} catch (final Exception e) {
				Log.log.error("Failed to initialize MoarSign: ", e);
			}
		}

		public _Class guiMoarSign;
		public _Class tileMoarSign;
		public _Field guiMoarSignTileEntity;
		public _Class packetHandler;
		public _Field packetHandlerInstance;
		public _Class messageMoarSignUpdate;
		public _Constructor messageMoarSignUpdateCtor;

		public MoarSignCompat() throws Exception {
			this.guiMoarSign = _class($("gory_moon.moarsigns.client.interfaces.sign.GuiMoarSign"));
			this.tileMoarSign = _class($("gory_moon.moarsigns.tileentites.TileEntityMoarSign"));
			this.guiMoarSignTileEntity = this.guiMoarSign._psearchfield(null, this.tileMoarSign.$class());
			this.packetHandler = _class($("gory_moon.moarsigns.network.PacketHandler"));
			this.packetHandlerInstance = this.packetHandler._field($("INSTANCE"));
			this.messageMoarSignUpdate = _class($("gory_moon.moarsigns.network.message.MessageSignUpdate"));
			this.messageMoarSignUpdateCtor = this.messageMoarSignUpdate.__constructor(this.tileMoarSign.$class());
		}

		public void placeSign(@Nonnull final EntryId entryId, @Nonnull final Object sourceentity) {
			try {
				SignEntryId.fromEntryId(entryId).toEntity((TileEntitySign) sourceentity);
				((TileEntitySign) sourceentity).markDirty();
				final CompatMessage message = new CompatMessage(this.messageMoarSignUpdateCtor.$new(sourceentity));
				final CompatSimpleNetworkWrapper network = new CompatSimpleNetworkWrapper(this.packetHandlerInstance.$get(null));
				network.sendToServer(message);
			} catch (final Exception e) {
				Log.log.error("Failed to place MoarSign: ", e);
			}
		}
	}

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
			} else if (MoarSignCompat.instance!=null&&MoarSignCompat.instance.guiMoarSign.$class().isInstance(gui)) {
				event.setCanceled(true);
				final EntryId placeSign = handSignValid ? handSign : entryId;
				if (placeSign.isPlaceable())
					try {
						final Object tileSign = MoarSignCompat.instance.guiMoarSignTileEntity.$get(gui);
						if (tileSign!=null) {
							MoarSignCompat.instance.placeSign(placeSign, tileSign);
							if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE)) {
								CurrentMode.instance.setMode();
								final SignEntity se = Sign.preview;
								if (se.isRenderable()) {
									final TileEntitySign preview = se.getTileEntity();
									final CompatBlockPos previewpos = CompatBlockPos.getTileEntityPos(preview);
									final CompatBlockPos tilepos = CompatBlockPos.getTileEntityPos((TileEntity) tileSign);
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
			} else if (Config.getConfig().signTooltip.get()) {
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