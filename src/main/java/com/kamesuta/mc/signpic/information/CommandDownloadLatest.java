package com.kamesuta.mc.signpic.information;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.download.ModDownload;
import com.kamesuta.mc.signpic.http.download.ModDownload.ModDLResult;
import com.kamesuta.mc.signpic.util.ChatBuilder;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandDownloadLatest extends CommandBase {
	private static final boolean ENABLED = true;

	@Override
	public String getCommandName() {
		return "signpic-download-latest";
	}

	@Override
	public String getCommandUsage(final ICommandSender var1) {
		return "/signpic-download-latest";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public void processCommand(final ICommandSender var1, final String[] var2) {
		if (!ENABLED)
			var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.disabled").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
		else {
			final Informations.InfoState state = Informations.instance.getState();
			final Informations.InfoSource source = Informations.instance.getSource();
			final Informations.InfoVersion online = source.onlineVersion();
			if (source!=null&&online!=null&&online.version!=null&&!StringUtils.isEmpty(online.version.remote))
				if (state.isDownloaded())
					var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.downloadedAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				else if (state.downloading)
					var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.downloadingAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				else {
					final ModDownload downloader = new ModDownload();
					downloader.setCallback(new ICommunicateCallback() {
						@Override
						public void onDone(final ICommunicateResponse res) {
							final ModDLResult result = downloader.result;
							if (result!=null)
								new ChatBuilder().setChat(result.response).chatClient();
						}
					});
					Communicator.instance.communicate(downloader);
				}
		}
	}
}