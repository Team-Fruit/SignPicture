package com.kamesuta.mc.signpic.util;

import com.google.gson.JsonSyntaxException;
import com.kamesuta.mc.signpic.Client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class ChatBuilder {
	public static final int DefaultId = 877;

	private IChatComponent chat = null;
	private String text = "";
	private Object[] params = new Object[0];
	private boolean useTranslation = false;
	private boolean useJson = false;
	private boolean useId = false;
	private int id = -1;

	public ChatBuilder() {}

	public IChatComponent build() {
		IChatComponent chat;
		if (this.chat==null) {
			if (this.useTranslation && !this.useJson)
				chat = new ChatComponentTranslation(this.text, this.params);
			else {
				String s;
				if (this.useTranslation)
					s = String.format(StatCollector.translateToLocal(this.text), this.params);
				else
					s = this.text;

				if (this.useJson)
					try {
						chat = IChatComponent.Serializer.func_150699_a(s);
					} catch (final JsonSyntaxException e) {
						chat = new ChatComponentText("Invaild Json: " + this.text);
					}
				else
					chat = new ChatComponentText(this.text);
			}
		} else {
			chat = this.chat;
		}
		return chat;
	}

	public ChatBuilder setId(final int id) {
		this.useId = true;
		this.id = id;
		return this;
	}

	public ChatBuilder setId() {
		setId(DefaultId);
		return this;
	}

	public ChatBuilder setChat(final IChatComponent chat) {
		this.chat = chat;
		return this;
	}

	public ChatBuilder setText(final String text) {
		this.text = text;
		return this;
	}

	public ChatBuilder setParams(final Object... params) {
		this.params = params;
		return this;
	}

	public ChatBuilder useTranslation() {
		this.useTranslation = true;
		return this;
	}

	public ChatBuilder useJson() {
		this.useJson = true;
		return this;
	}

	public static ChatBuilder create(final String text) {
		return new ChatBuilder().setText(text);
	}

	@SideOnly(Side.CLIENT)
	public void chatClient() {
		chatClient(this);
	}

	@SideOnly(Side.CLIENT)
	public static void chatClient(final ChatBuilder chat) {
		final Minecraft mc = Client.mc;
		if (mc.thePlayer!=null) {
			final IChatComponent msg = chat.build();
			if (chat.useId)
				mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(msg, chat.id);
			else
				mc.thePlayer.addChatComponentMessage(msg);
		}
	}

	@SideOnly(Side.SERVER)
	public static void sendPlayerChat(final ICommandSender target, final ChatBuilder chat) {
		target.addChatMessage(chat.build());
	}

	@SideOnly(Side.SERVER)
	public static void sendServerChat(final ChatBuilder chat) {
		final ServerConfigurationManager sender = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
		sender.sendChatMsg(chat.build());
	}
}
