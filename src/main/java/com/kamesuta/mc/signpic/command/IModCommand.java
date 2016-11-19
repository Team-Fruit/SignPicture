package com.kamesuta.mc.signpic.command;

import java.util.List;
import java.util.SortedSet;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public interface IModCommand extends ICommand {
	String getFullCommandString();

	@Override
	List<String> getCommandAliases();

	int getRequiredPermissionLevel();

	SortedSet<SubCommand> getChildren();

	void printHelp(ICommandSender arg0);
}