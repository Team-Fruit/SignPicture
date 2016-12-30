package com.kamesuta.mc.signpic.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class RootCommand extends CommandBase implements IModCommand {
	public static final @Nonnull String ROOT_COMMAND_NAME = "signpic";
	private final @Nonnull SortedSet<SubCommand> children = new TreeSet<SubCommand>(new Comparator<SubCommand>() {
		@Override
		public int compare(final @Nonnull SubCommand o1, final @Nonnull SubCommand o2) {
			return o1.compareTo(o2);
		}
	});

	public void addChildCommand(final @Nonnull SubCommand child) {
		child.setParent(this);
		this.children.add(child);
	}

	@Override
	public @Nonnull SortedSet<SubCommand> getChildren() {
		return this.children;
	}

	@Override
	public @Nonnull String getCommandName() {
		return ROOT_COMMAND_NAME;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public @Nonnull List<String> getCommandAliases() {
		final ArrayList<String> aliases = Lists.newArrayList();
		aliases.add("signpicture");
		return aliases;
	}

	@Override
	public @Nonnull String getCommandUsage(final @Nonnull ICommandSender sender) {
		return "/"+getCommandName()+" help";
	}

	@Override
	public void processCommand(final @Nonnull ICommandSender sender, final @Nonnull String[] args) {
		if (!CommandHelpers.processCommands(sender, this, args))
			CommandHelpers.throwWrongUsage(sender, this);
	}

	@Override
	public @Nonnull List<String> addTabCompletionOptions(final @Nonnull ICommandSender sender, final @Nonnull String[] args) {
		return CommandHelpers.completeCommands(sender, this, args);
	}

	@Override
	public @Nonnull String getFullCommandString() {
		return getCommandName();
	}

	@Override
	public void printHelp(final @Nonnull ICommandSender sender) {
		CommandHelpers.printHelp(sender, this);
	}
}