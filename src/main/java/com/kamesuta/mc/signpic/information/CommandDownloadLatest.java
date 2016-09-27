package com.kamesuta.mc.signpic.information;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

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
	public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender var1, final String[] var2) throws CommandException {
		if(!ENABLED) {
			var1.addChatMessage(new TextComponentTranslation("signpic.versioning.disabled").setStyle(new Style().setColor(TextFormatting.RED)));
		} else {
			final InformationChecker.InfoState state = InformationChecker.state;
			if (state.doneChecking && state.onlineVersion!=null && !StringUtils.isEmpty(state.onlineVersion.remote))
			{
				if(state.downloadedFile)
					var1.addChatMessage(new TextComponentTranslation("signpic.versioning.downloadedAlready").setStyle(new Style().setColor(TextFormatting.RED)));
				else if(state.startedDownload)
					var1.addChatMessage(new TextComponentTranslation("signpic.versioning.downloadingAlready").setStyle(new Style().setColor(TextFormatting.RED)));
				else new ThreadDownloadMod();
			}
		}
	}
}