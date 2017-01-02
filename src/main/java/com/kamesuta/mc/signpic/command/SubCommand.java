package com.kamesuta.mc.signpic.command;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public abstract class SubCommand implements IModCommand {
	private final @Nonnull String name;
	private final @Nonnull List<String> aliases = Lists.newArrayList();
	private @Nonnull SubCommand.PermLevel permLevel;
	private @Nullable IModCommand parent;
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

		this.children = Sets.newTreeSet(new Comparator<SubCommand>() {
			@Override
			public int compare(final @Nullable SubCommand o1, final @Nullable SubCommand o2) {
				if (o1!=null&&o2!=null)
					return o1.compareTo(o2);
				return 0;
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

	void setParent(final @Nullable IModCommand parent) {
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
	public @Nullable List<?> addTabCompletionOptions(final @Nullable ICommandSender p_71516_1_, final @Nullable String[] p_71516_2_) {
		return null;
	}

	@Override
	public void processCommand(final @Nullable ICommandSender sender, final @Nullable String[] args) {
		if (sender!=null&&args!=null&&!CommandHelpers.processCommands(sender, this, args))
			processSubCommand(sender, args);
	}

	public @Nullable List<String> completeCommand(final @Nonnull ICommandSender sender, final @Nonnull String[] args) {
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
	public boolean canCommandSenderUseCommand(final @Nullable ICommandSender sender) {
		if (sender!=null)
			return sender.canCommandSenderUseCommand(getRequiredPermissionLevel(), getCommandName());
		return false;
	}

	@Override
	public boolean isUsernameIndex(final @Nullable String[] args, final int index) {
		return false;
	}

	@Override
	public @Nonnull String getCommandUsage(final @Nullable ICommandSender sender) {
		return "/"+getFullCommandString()+" help";
	}

	@Override
	public void printHelp(final @Nonnull ICommandSender sender) {
		CommandHelpers.printHelp(sender, this);
	}

	@Override
	public @Nonnull String getFullCommandString() {
		if (this.parent!=null)
			return this.parent.getFullCommandString()+" "+getCommandName();
		return " "+getCommandName();
	}

	public int compareTo(final @Nullable ICommand command) {
		if (command!=null)
			return getCommandName().compareTo(command.getCommandName());
		return 0;
	}

	@Override
	public int compareTo(final @Nullable Object command) {
		return this.compareTo((ICommand) command);
	}
}