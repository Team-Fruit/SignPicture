package com.kamesuta.mc.signpic.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class RootCommand extends CommandBase implements IModCommand {
	public static final String ROOT_COMMAND_NAME = "signpic";
	private final SortedSet<SubCommand> children = new TreeSet<SubCommand>(new Comparator<SubCommand>() {
		@Override
		public int compare(final SubCommand o1, final SubCommand o2) {
			return o1.compareTo(o2);
		}
	});

	public void addChildCommand(final SubCommand child) {
		child.setParent(this);
		this.children.add(child);
	}

	@Override
	public SortedSet<SubCommand> getChildren() {
		return this.children;
	}

	@Override
	public String getCommandName() {
		return ROOT_COMMAND_NAME;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public List<String> getCommandAliases() {
		final ArrayList<String> aliases = Lists.newArrayList();
		aliases.add("signpicture");
		return aliases;
	}

	@Override
	public String getCommandUsage(final ICommandSender sender) {
		return "/"+getCommandName()+" help";
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
		if (!CommandHelpers.processCommands(sender, this, args))
			CommandHelpers.throwWrongUsage(sender, this);
	}

	@Override
	public List<String> getTabCompletionOptions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos pos) {
		return CommandHelpers.completeCommands(sender, this, args);
	}

	@Override
	public String getFullCommandString() {
		return getCommandName();
	}

	@Override
	public void printHelp(final ICommandSender sender) {
		CommandHelpers.printHelp(sender, this);
	}
}