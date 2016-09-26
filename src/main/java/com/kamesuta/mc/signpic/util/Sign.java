package com.kamesuta.mc.signpic.util;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.preview.SignEntity;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;

public class Sign {
	public static int maxText = 15*4;

	public static final SignEntity preview = new SignEntity();
	public static EntryIdBuilder builder = new EntryIdBuilder();

	public static void updatePreview(final EntryId entryId) {
		entryId.toEntity(preview.getTileEntity());
	}

	public static void sendSign(final EntryId entryId, final TileEntitySign sourceentity) {
		entryId.toEntity(sourceentity);
		sourceentity.markDirty();
		final NetHandlerPlayClient nethandlerplayclient = Client.mc.getNetHandler();
		if (nethandlerplayclient != null)
			nethandlerplayclient.addToSendQueue(new C12PacketUpdateSign(sourceentity.getPos(), sourceentity.signText));
		sourceentity.setEditable(true);
	}
}