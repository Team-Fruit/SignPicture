package com.kamesuta.mc.signpic.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.util.ChatBuilder;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class CommandHelpers {
	public static void throwWrongUsage(final ICommandSender sender, final IModCommand command) throws WrongUsageException {
		throw new WrongUsageException(I18n.format("signpic.command.help", command.getCommandUsage(sender)));
	}

	public static void processChildCommand(final ICommandSender sender, final SubCommand child, final String[] args) throws CommandException {
		if (!sender.canCommandSenderUseCommand(child.getRequiredPermissionLevel(), child.getFullCommandString()))
			throw new WrongUsageException(I18n.format("signpic.command.noperms"));
		else
			child.execute(FMLClientHandler.instance().getServer(), sender, Arrays.copyOfRange(args, 1, args.length));
	}

	public static List<String> completeChildCommand(final ICommandSender sender, final SubCommand child, final String[] args) {
		return child.completeCommand(sender, Arrays.copyOfRange(args, 1, args.length));
	}

	public static void printHelp(final ICommandSender sender, final IModCommand command) {
		final Style header = new Style();
		header.setColor(TextFormatting.BLUE);
		ChatBuilder.create("signpic.command."+command.getFullCommandString().replace(" ", ".")+".format").useTranslation().setStyle(header).setParams(command.getFullCommandString()).sendPlayer(sender);
		final Style body = new Style();
		body.setColor(TextFormatting.GRAY);
		ChatBuilder.create("signpic.command.aliases").useTranslation().setStyle(body).setParams(command.getCommandAliases().toString().replace("[", "").replace("]", "")).sendPlayer(sender);
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

	public static List<String> completeCommands(final ICommandSender sender, final IModCommand command, final String[] args) {
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

	public static boolean processCommands(final ICommandSender sender, final IModCommand command, final String[] args) throws CommandException {
		if (args.length>=1) {
			if (args[0].equals("help")) {
				command.printHelp(sender);
				return true;
			}
			final Iterator<SubCommand> arg2 = command.getChildren().iterator();
			while (arg2.hasNext()) {
				final SubCommand child = arg2.next();
				if (matches(args[0], child)) {
					processChildCommand(sender, child, args);
					return true;
				}
			}
		}

		return false;
	}

	public static boolean matches(final String commandName, final IModCommand command) {
		if (commandName.equals(command.getCommandName()))
			return true;
		else {
			if (command.getCommandAliases()!=null) {
				final Iterator<String> arg1 = command.getCommandAliases().iterator();
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