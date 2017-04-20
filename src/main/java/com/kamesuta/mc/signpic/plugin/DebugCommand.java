package com.kamesuta.mc.signpic.plugin;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.command.SubCommand;
import com.kamesuta.mc.signpic.plugin.gui.GuiManager;

import net.minecraft.command.ICommandSender;

public class DebugCommand extends SubCommand {

	public static List<String> signs = Lists.newArrayList();

	static {
		signs.add("#$i.gyazo.com/b16b63c40f3c7b2e0f344a54493f2b6b.png{L5X5t1}");
		signs.add("#i.imgur.com/zLuPWc6.png{4}");
		signs.add("#$i.gyazo.com/8ace9f9398c097997479924aca3b4d35.png{4x4}");
	}

	public SignData sample;

	public DebugCommand() {
		super("open");
		this.sample = new SignData();
		final Date date = new Date();
		this.sample.setCreateDate(date);
		this.sample.setUpdateDate(date);
		this.sample.setId(1);
		this.sample.setPlayerName("Player");
		this.sample.setPlayerUUID(UUID.randomUUID().toString());
		this.sample.setWorldName("world");
		this.sample.setX(0);
		this.sample.setY(64);
		this.sample.setZ(0);
	}

	@Override
	public void processSubCommand(final ICommandSender sender, final String[] args) {
		final GuiManager gui = new GuiManager(sender.getCommandSenderName(), "100");
		Client.openLater(gui);
		final Random random = new Random();
		for (int i = 0; i<100; i++) {
			this.sample.setSign(signs.get(random.nextInt(signs.size())));
			gui.data(String.valueOf(i), SignData.dateGson.toJson(this.sample));
		}
	}
}
