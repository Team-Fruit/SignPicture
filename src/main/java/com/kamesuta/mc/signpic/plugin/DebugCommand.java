package com.kamesuta.mc.signpic.plugin;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.command.SubCommand;
import com.kamesuta.mc.signpic.plugin.gui.GuiManager;

import net.minecraft.command.ICommandSender;

public class DebugCommand extends SubCommand {

	public DebugCommand() {
		super("open");
	}

	@Override
	public void processSubCommand(final ICommandSender sender, final String[] args) {
		Client.openLater(new GuiManager(Client.mc.currentScreen, sender.getCommandSenderName(), "0"));
	}
}
