package com.kamesuta.mc.signpic.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.util.ChatBuilder;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandHelpers {
	public static void throwWrongUsage(final @Nonnull ICommandSender sender, final @Nonnull IModCommand command) throws WrongUsageException {
		throw new WrongUsageException(I18n.format("signpic.command.help", command.getCommandUsage(sender)));
	}

	public static void processChildCommand(final @Nonnull ICommandSender sender, final @Nonnull SubCommand child, final @Nonnull String[] args) {
		if (!sender.canCommandSenderUseCommand(child.getRequiredPermissionLevel(), child.getFullCommandString()))
			throw new WrongUsageException(I18n.format("signpic.command.noperms"));
		else
			child.processCommand(sender, Arrays.copyOfRange(args, 1, args.length));
	}

	public static @Nullable List<String> completeChildCommand(final @Nonnull ICommandSender sender, final @Nonnull SubCommand child, final @Nonnull String[] args) {
		return child.completeCommand(sender, Arrays.copyOfRange(args, 1, args.length));
	}

	public static void printHelp(final @Nonnull ICommandSender sender, final @Nonnull IModCommand command) {
		final ChatStyle header = new ChatStyle();
		header.setColor(EnumChatFormatting.BLUE);
		ChatBuilder.create("signpic.command."+command.getFullCommandString().replace(" ", ".")+".format").useTranslation().setStyle(header).setParams(command.getFullCommandString()).sendPlayer(sender);
		final ChatStyle body = new ChatStyle();
		body.setColor(EnumChatFormatting.GRAY);
		final List<String> aliases = command.getCommandAliases();
		if (aliases!=null)
			ChatBuilder.create("signpic.command.aliases").useTranslation().setStyle(body).setParams(aliases.toString().replace("[", "").replace("]", "")).sendPlayer(sender);
		ChatBuilder.create("signpic.command.permlevel").useTranslation().setStyle(body).setParams(Integer.valueOf(command.getRequiredPermissionLevel())).sendPlayer(sender);
		ChatBuilder.create("signpic.command."+command.getFullCommandString().replace(" ", ".")+".help").useTranslation().setStyle(body).sendPlayer(sender);
		if (!command.getChildren().isEmpty()) {
			ChatBuilder.create("signpic.command.list").useTranslation().sendPlayer(sender);
			final Iterator<SubCommand> arg3 = command.getChildren().iterator();
			while (arg3.hasNext()) {
				final SubCommand child = arg3.next();
				ChatBuilder.create("signpic.command."+child.getFullCommandString().replace(" ", ".")+".desc").useTranslation().setParams(child.getCommandName()).sendPlayer(sender);
			}
		}

	}

	public static @Nullable List<String> completeCommands(final @Nonnull ICommandSender sender, final @Nonnull IModCommand command, final @Nonnull String[] args) {
		if (args.length>=2) {
			final Iterator<SubCommand> arg2 = command.getChildren().iterator();
			while (arg2.hasNext()) {
				final SubCommand child = arg2.next();
				if (StringUtils.equals(args[0], child.getCommandName()))
					return completeChildCommand(sender, child, args);
			}
			return null;
		}
		final List<String> complete = Lists.newArrayList();
		if (!StringUtils.equals("help", command.getCommandName()))
			complete.add("help");
		final Iterator<SubCommand> arg2 = command.getChildren().iterator();
		while (arg2.hasNext()) {
			final SubCommand child = arg2.next();
			complete.add(child.getCommandName());
		}
		return complete;
	}

	public static boolean processCommands(final @Nonnull ICommandSender sender, final @Nonnull IModCommand command, final @Nonnull String[] args) {
		if (args.length>=1) {
			if (args[0].equals("help")) {
				command.printHelp(sender);
				return true;
			}
			final Iterator<SubCommand> arg2 = command.getChildren().iterator();
			while (arg2.hasNext()) {
				final SubCommand child = arg2.next();
				final String arg = args[0];
				if (arg!=null&&child!=null&&matches(arg, child)) {
					processChildCommand(sender, child, args);
					return true;
				}
			}
		}

		return false;
	}

	public static boolean matches(final @Nonnull String commandName, final @Nonnull IModCommand command) {
		if (commandName.equals(command.getCommandName()))
			return true;
		else {
			final List<String> aliases = command.getCommandAliases();
			if (aliases!=null) {
				final Iterator<String> arg1 = aliases.iterator();
				while (arg1.hasNext()) {
					final String alias = arg1.next();
					if (commandName.equals(alias))
						return true;
				}
			}
			return false;
		}
	}
}