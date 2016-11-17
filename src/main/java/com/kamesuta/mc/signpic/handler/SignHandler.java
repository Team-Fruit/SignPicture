package com.kamesuta.mc.signpic.handler;

import java.lang.reflect.Field;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.preview.SignEntity;
import com.kamesuta.mc.signpic.util.ChatBuilder;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;

public class SignHandler {
	private static Field f;

	public static void init() {
		try {
			final Field[] fields = GuiEditSign.class.getDeclaredFields();
			for (final Field field : fields)
				if (TileEntitySign.class.equals(field.getType())) {
					Reference.logger.info("Hook the TileEntitySign field included by GuiEditSign");
					field.setAccessible(true);
					f = field;
				}
		} catch (final SecurityException e) {
			Reference.logger.error("Could not hook TileEntitySign field included by GuiEditSign", e);
		}
	}

	@CoreEvent
	public void onSign(final GuiOpenEvent event) {
		if (CurrentMode.instance.isMode(CurrentMode.Mode.PLACE))
			if (event.gui instanceof GuiEditSign)
				if (f!=null)
					try {
						final GuiEditSign ges = (GuiEditSign) event.gui;
						final TileEntitySign tileSign = (TileEntitySign) f.get(ges);
						Sign.placeSign(CurrentMode.instance.getEntryId(), tileSign);
						event.setCanceled(true);
						if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE)) {
							CurrentMode.instance.setMode();
							final SignEntity se = Sign.preview;
							if (se.isRenderable()) {
								final BlockPos preview = se.getTileEntity().getPos();
								final BlockPos tileSignPos = tileSign.getPos();
								if (preview.getX()==tileSignPos.getX()&&preview.getY()==tileSignPos.getY()&&preview.getZ()==tileSignPos.getZ()) {
									Sign.preview.setVisible(false);
									CurrentMode.instance.setState(CurrentMode.State.PREVIEW, false);
									CurrentMode.instance.setState(CurrentMode.State.SEE, false);
								}
							}
						}
					} catch (final Exception e) {
						Reference.logger.error(I18n.format("signpic.chat.error.place"), e);
						ChatBuilder.create("signpic.chat.error.place").setId().useTranslation().chatClient();
					}
				else
					ChatBuilder.create("signpic.chat.error.place").setId().useTranslation().chatClient();
	}

	@CoreEvent
	public void onClick(final MouseEvent event) {
		if (event.buttonstate&&Client.mc.gameSettings.keyBindUseItem.getKeyCode()==event.button-100)
			if (CurrentMode.instance.isMode(CurrentMode.Mode.SETPREVIEW)) {
				Sign.preview.capturePlace();
				event.setCanceled(true);
				CurrentMode.instance.setMode();
				Client.openEditor();
			} else if (CurrentMode.instance.isMode(CurrentMode.Mode.LOAD)) {
				final TileEntitySign tilesign = Client.getTileSignLooking();
				if (tilesign!=null) {
					final Entry entry = EntryId.fromTile(tilesign).entry();
					if (entry.isValid()) {
						CurrentMode.instance.setEntryId(entry.id);
						event.setCanceled(true);
						Client.openEditor();
						if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE))
							CurrentMode.instance.setMode();
					}
				}
			}
	}
}
