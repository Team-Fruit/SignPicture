package com.kamesuta.mc.signpic.command;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public abstract class SubCommand implements IModCommand {
	private final @Nonnull String name;
	private final @Nonnull List<String> aliases = Lists.newArrayList();
	private @Nonnull SubCommand.PermLevel permLevel;
	private @Nonnull IModCommand parent;
	private final @Nonnull SortedSet<SubCommand> children;

	public static enum PermLevel {
		EVERYONE(0), ADMIN(2);

		int permLevel;

		private PermLevel(final int permLevel) {
			this.permLevel = permLevel;
		}
	}

	public SubCommand(final @Nonnull String name) {
		this.permLevel = SubCommand.PermLevel.EVERYONE;

		this.children = new TreeSet<SubCommand>(new Comparator<SubCommand>() {
			@Override
			public int compare(final @Nonnull SubCommand o1, final @Nonnull SubCommand o2) {
				return o1.compareTo(o2);
			}
		});
		this.name = name;
	}

	@Override
	public @Nonnull String getCommandName() {
		return this.name;
	}

	public @Nonnull SubCommand addChildCommand(final @Nonnull SubCommand child) {
		child.setParent(this);
		this.children.add(child);
		return this;
	}

	void setParent(final @Nonnull IModCommand parent) {
		this.parent = parent;
	}

	@Override
	public @Nonnull SortedSet<SubCommand> getChildren() {
		return this.children;
	}

	public void addAlias(final @Nonnull String alias) {
		this.aliases.add(alias);
	}

	@Override
	public @Nonnull List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public @Nonnull List<?> addTabCompletionOptions(final @Nonnull ICommandSender p_71516_1_, final @Nonnull String[] p_71516_2_) {
		return null;
	}

	@Override
	public void processCommand(final @Nonnull ICommandSender sender, final @Nonnull String[] args) {
		if (!CommandHelpers.processCommands(sender, this, args))
			processSubCommand(sender, args);
	}

	public @Nonnull List<String> completeCommand(final @Nonnull ICommandSender sender, final @Nonnull String[] args) {
		return CommandHelpers.completeCommands(sender, this, args);
	}

	public void processSubCommand(final @Nonnull ICommandSender sender, final @Nonnull String[] args) {
		CommandHelpers.throwWrongUsage(sender, this);
	}

	public @Nonnull SubCommand setPermLevel(final @Nonnull SubCommand.PermLevel permLevel) {
		this.permLevel = permLevel;
		return this;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return this.permLevel.permLevel;
	}

	@Override
	public boolean canCommandSenderUseCommand(final @Nonnull ICommandSender sender) {
		return sender.canCommandSenderUseCommand(getRequiredPermissionLevel(), getCommandName());
	}

	@Override
	public boolean isUsernameIndex(final @Nonnull String[] args, final int index) {
		return false;
	}

	@Override
	public @Nonnull String getCommandUsage(final @Nonnull ICommandSender sender) {
		return "/"+getFullCommandString()+" help";
	}

	@Override
	public void printHelp(final @Nonnull ICommandSender sender) {
		CommandHelpers.printHelp(sender, this);
	}

	@Override
	public @Nonnull String getFullCommandString() {
		return this.parent.getFullCommandString()+" "+getCommandName();
	}

	public int compareTo(final @Nonnull ICommand command) {
		return getCommandName().compareTo(command.getCommandName());
	}

	@Override
	public int compareTo(final @Nonnull Object command) {
		return this.compareTo((ICommand) command);
	}
}