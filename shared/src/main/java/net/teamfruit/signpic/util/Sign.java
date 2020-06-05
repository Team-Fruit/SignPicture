package net.teamfruit.signpic.util;

import javax.annotation.Nonnull;

import org.lwjgl.util.Timer;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.tileentity.TileEntitySign;
import net.teamfruit.bnnwidget.WFrame;
import net.teamfruit.signpic.Client;
import net.teamfruit.signpic.Config;
import net.teamfruit.signpic.compat.Compat.CompatBlockPos;
import net.teamfruit.signpic.compat.Compat.CompatC12PacketUpdateSign;
import net.teamfruit.signpic.compat.Compat.CompatC17PacketCustomPayload;
import net.teamfruit.signpic.compat.Compat.CompatMinecraft;
import net.teamfruit.signpic.compat.Compat.CompatNetHandlerPlayClient;
import net.teamfruit.signpic.compat.Compat.CompatTileEntitySign;
import net.teamfruit.signpic.entry.EntryId;
import net.teamfruit.signpic.entry.EntryId.SignEntryId;
import net.teamfruit.signpic.entry.EntryIdBuilder;
import net.teamfruit.signpic.gui.GuiPAAS;
import net.teamfruit.signpic.preview.SignEntity;

public class Sign {
	public static int maxText = 15*4;

	public static final @Nonnull SignEntity preview = new SignEntity();
	public static @Nonnull EntryIdBuilder builder = new EntryIdBuilder();

	public static void updatePreview(final @Nonnull EntryId entryId) {
		SignEntryId.fromEntryId(entryId).toEntity(preview.getTileEntity());
	}

	public static void sendSign(final @Nonnull EntryId entryId, final @Nonnull TileEntitySign sourceentity) {
		SignEntryId.fromEntryId(entryId).toEntity(sourceentity);
		sourceentity.markDirty();
		final CompatNetHandlerPlayClient nethandlerplayclient = CompatMinecraft.getConnection();
		if (nethandlerplayclient!=null) {
			final CompatBlockPos pos = CompatBlockPos.getTileEntityPos(sourceentity);
			nethandlerplayclient.sendPacket(CompatC12PacketUpdateSign.create(pos, CompatTileEntitySign.getSignText(sourceentity)));
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
		final CompatNetHandlerPlayClient nethandlerplayclient = CompatMinecraft.getConnection();
		if (nethandlerplayclient!=null)
			nethandlerplayclient.sendPacket(CompatC17PacketCustomPayload.create("MC|ItemName", name));
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
			SignEntryId.fromEntryId(id).toEntity(entity);
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
