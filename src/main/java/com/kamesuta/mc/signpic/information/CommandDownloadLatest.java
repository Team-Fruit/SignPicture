package com.kamesuta.mc.signpic.information;
import org.apache.commons.lang3.StringUtils;

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
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public void processCommand(final ICommandSender var1, final String[] var2) {
		if(!ENABLED) {
			var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.disabled").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
		} else {
			final InformationChecker.InfoState state = InformationChecker.state;
			if (state.doneChecking && state.onlineVersion!=null && !StringUtils.isEmpty(state.onlineVersion.remote))
			{
				if(state.downloadedFile)
					var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.downloadedAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				else if(state.startedDownload)
					var1.addChatMessage(new ChatComponentTranslation("signpic.versioning.downloadingAlready").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				else new ThreadDownloadMod();
			}
		}
	}
}