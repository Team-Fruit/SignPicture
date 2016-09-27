package com.kamesuta.mc.signpic.util;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.preview.SignEntity;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketUpdateSign;
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
		final NetHandlerPlayClient nethandlerplayclient = Client.mc.getConnection();
		if (nethandlerplayclient != null)
			nethandlerplayclient.sendPacket(new CPacketUpdateSign(sourceentity.getPos(), sourceentity.signText));
		sourceentity.setEditable(true);
	}
}
