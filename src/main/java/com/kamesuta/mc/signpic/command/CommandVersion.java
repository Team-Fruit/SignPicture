package com.kamesuta.mc.signpic.command;

import java.util.concurrent.TimeUnit;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.information.Informations;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;

public class CommandVersion extends SubCommand {
	public CommandVersion() {
		super("version");
		addChildCommand(new CommandVersion.CommandVersionCheck());
		addChildCommand(new CommandVersion.CommandVersionUpdate());
		setPermLevel(PermLevel.EVERYONE);
	}

	private static class CommandVersionCheck extends SubCommand {
		private CommandVersionCheck() {
			super("check");
			setPermLevel(PermLevel.EVERYONE);
		}

		@Override
		public void processSubCommand(final ICommandSender sender, final String[] args) {
			final long cooldown = TimeUnit.HOURS.toMillis(2l);
			if (Informations.instance.shouldCheck(cooldown)) {
				Client.notice(I18n.format("signpic.versioning.check.start"), 2f);
				Informations.instance.check(null);
			} else {
				final long d = System.currentTimeMillis()-Informations.instance.getLastCheck();
				Client.notice(I18n.format("signpic.versioning.check.cooldown", TimeUnit.MILLISECONDS.toHours(d)));
			}
		}
	}

	private static class CommandVersionUpdate extends SubCommand {
		private CommandVersionUpdate() {
			super("update");
			setPermLevel(PermLevel.EVERYONE);
		}

		@Override
		public void processSubCommand(final ICommandSender sender, final String[] args) {
			// ChatBuilder.sendPlayerChat(var1, ChatBuilder.create("signpic.versioning.disabled").setStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			final long cooldown = TimeUnit.HOURS.toMillis(2l);
			if (Informations.instance.shouldCheck(cooldown))
				Informations.instance.check(new Runnable() {
					@Override
					public void run() {
						if (Informations.instance.isUpdateRequired())
							Informations.instance.runUpdate();
						else
							Client.notice(I18n.format("signpic.versioning.noupdate"));
					}
				});
			else if (Informations.instance.isUpdateRequired())
				Informations.instance.runUpdate();
			else
				Client.notice(I18n.format("signpic.versioning.noupdate"));
		}
	}
}