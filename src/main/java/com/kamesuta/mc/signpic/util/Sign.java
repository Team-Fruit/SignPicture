package com.kamesuta.mc.signpic.util;

import javax.annotation.Nonnull;

import org.lwjgl.util.Timer;

import com.kamesuta.mc.bnnwidget.WFrame;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.compat.Compat.CompatC12PacketUpdateSign;
import com.kamesuta.mc.signpic.compat.Compat.CompatC17PacketCustomPayload;
import com.kamesuta.mc.signpic.compat.Compat.CompatTileEntitySign;
import com.kamesuta.mc.signpic.compat.Compat.MovePos;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.gui.GuiPAAS;
import com.kamesuta.mc.signpic.preview.SignEntity;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.tileentity.TileEntitySign;

public class Sign {
	public static int maxText = 15*4;

	public static final @Nonnull SignEntity preview = new SignEntity();
	public static @Nonnull EntryIdBuilder builder = new EntryIdBuilder();

	public static void updatePreview(final @Nonnull EntryId entryId) {
		entryId.toEntity(preview.getTileEntity());
	}

	public static void sendSign(final @Nonnull EntryId entryId, final @Nonnull TileEntitySign sourceentity) {
		entryId.toEntity(sourceentity);
		sourceentity.markDirty();
		final NetHandlerPlayClient nethandlerplayclient = Client.mc.getNetHandler();
		if (nethandlerplayclient!=null) {
			final MovePos pos = MovePos.getTileEntityPos(sourceentity);
			nethandlerplayclient.addToSendQueue(CompatC12PacketUpdateSign.create(pos, CompatTileEntitySign.getSignText(sourceentity)));
		}
		sourceentity.setEditable(true);
	}

	public static void placeSign(final @Nonnull EntryId entryId, final @Nonnull TileEntitySign sourceentity) {
		if (Config.getConfig().multiplayPAAS.get()&&!Client.mc.isSingleplayer())
			WFrame.displayFrame(new GuiPAAS(new SendPacketTask(entryId, sourceentity)));
		else
			sendSign(entryId, sourceentity);
	}

	public static void sendRepairName(final @Nonnull String name) {
		Client.mc.thePlayer.sendQueue.addToSendQueue(CompatC17PacketCustomPayload.create("MC|ItemName", name));
	}

	public static void setRepairName(final @Nonnull String name, final @Nonnull GuiTextField textField, final @Nonnull ContainerRepair containerRepair) {
		textField.setText(name);
		containerRepair.updateItemName(name);
		sendRepairName(name);
	}

	public static class SendPacketTask {
		public final long limit;
		public final @Nonnull EntryId id;
		public final @Nonnull String[] lines;
		public final @Nonnull TileEntitySign entity;
		public final @Nonnull Timer timer;

		public SendPacketTask(final @Nonnull EntryId id, final @Nonnull TileEntitySign entity) {
			this.timer = new Timer();
			this.lines = new String[4];
			id.toStrings(this.lines);
			this.limit = getExpectedEditTime(this.lines, false);
			this.id = id;
			this.entity = entity;
		}

		private void sendPacket() {
			Sign.sendSign(this.id, this.entity);
		}

		public boolean tick() {
			if (this.timer.getTime()*1000>this.limit) {
				sendPacket();
				return true;
			}
			return false;
		}

		private static long getExpectedEditTime(final @Nonnull String[] lines, final boolean skipEmpty) {
			long expected = Config.getConfig().multiplayPAASMinEditTime.get();
			final int minchartime = Config.getConfig().multiplayPAASMinCharTime.get();
			int n = 0;
			for (String line : lines)
				if (line!=null) {
					line = line.trim().toLowerCase();
					if (!line.isEmpty()) {
						final int chars = line.length();
						n += 1;
						expected += minchartime*chars;
					}
				}
			if (skipEmpty&&n==0)
				return 0;
			if (n>1)
				expected += Config.getConfig().multiplayPAASMinLineTime.get()*n;
			return expected;
		}
	}
}
