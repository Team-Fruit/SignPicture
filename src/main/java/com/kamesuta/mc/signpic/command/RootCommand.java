package com.kamesuta.mc.signpic.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class RootCommand extends CommandBase implements IModCommand {
	public static final @Nonnull String ROOT_COMMAND_NAME = "signpic";
	private final @Nonnull SortedSet<SubCommand> children = Sets.newTreeSet(new Comparator<SubCommand>() {
		@Override
		public int compare(final @Nullable SubCommand o1, final @Nullable SubCommand o2) {
			if (o1!=null&&o2!=null)
				return o1.compareTo(o2);
			return 0;
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
	public @Nullable List<String> getCommandAliases() {
		final ArrayList<String> aliases = Lists.newArrayList();
		aliases.add("signpicture");
		return aliases;
	}

	@Override
	public @Nonnull String getCommandUsage(final @Nullable ICommandSender sender) {
		return "/"+getCommandName()+" help";
	}

	@Override
	public void processCommand(final @Nullable ICommandSender sender, final @Nullable String[] args) {
		if (sender!=null&&args!=null)
			if (!CommandHelpers.processCommands(sender, this, args))
				CommandHelpers.throwWrongUsage(sender, this);
	}

	@Override
	public @Nullable List<String> addTabCompletionOptions(final @Nullable ICommandSender sender, final @Nullable String[] args) {
		if (sender!=null&&args!=null)
			return CommandHelpers.completeCommands(sender, this, args);
		return null;
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