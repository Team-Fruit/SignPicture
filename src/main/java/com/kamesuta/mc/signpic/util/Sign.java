package com.kamesuta.mc.signpic.util;

import org.lwjgl.util.Timer;

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
			nethandlerplayclient.addToSendQueue(new C12PacketUpdateSign(sourceentity.xCoord, sourceentity.yCoord, sourceentity.zCoord, sourceentity.signText));
		sourceentity.setEditable(true);
	}

	public static class SendPacketTask {
		public final long limit;
		public final EntryId id;
		public final String[] lines;
		public final TileEntitySign entity;
		public final Timer timer;

		public SendPacketTask(final EntryId id, final TileEntitySign entity) {
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
			if (this.timer.getTime() * 1000 > this.limit) {
				sendPacket();
				return true;
			}
			return false;
		}

		/** Fastest time "possible" estimate for an empty sign. */
		private static long minEditTime = 150;
		/** Minimum time needed to add one extra line (not the first). */
		private static long minLineTime = 50;
		/** Minimum time needed to type a character. */
		private static long minCharTime = 50;

		public static long getExpectedEditTime(final String[] lines, final boolean skipEmpty) {
			long expected = minEditTime;
			int n = 0;
			for (String line : lines){
				if (line != null){
					line = line.trim().toLowerCase();
					if (!line.isEmpty()){
						final int chars = line.length();
						n += 1;
						expected += minCharTime * chars;
					}
				}
			}
			if (skipEmpty && n == 0) {
				return 0;
			}
			if (n > 1){
				expected += minLineTime * n;
			}
			return expected;
		}
	}
}
