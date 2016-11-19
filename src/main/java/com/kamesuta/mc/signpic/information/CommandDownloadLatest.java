package com.kamesuta.mc.signpic.information;

import java.util.concurrent.TimeUnit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandDownloadLatest extends CommandBase {
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
		// ChatBuilder.sendPlayerChat(var1, ChatBuilder.create("signpic.versioning.disabled").setStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
		final long cooldown = TimeUnit.HOURS.toMillis(2l);
		if (Informations.instance.shouldCheck(cooldown))
			Informations.instance.check(new Runnable() {
				@Override
				public void run() {
					Informations.instance.runUpdate();
				}
			});
		else
			Informations.instance.runUpdate();
	}
}