package com.kamesuta.mc.signpic.util;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.Client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class ChatBuilder {
	public static final int DefaultId = 877;

	private @Nullable IChatComponent chat = null;
	private @Nullable ChatStyle style = null;
	private @Nonnull String text = "";
	private @Nonnull Object[] params = new Object[0];
	private boolean useTranslation = false;
	private boolean useJson = false;
	private boolean useId = false;
	private final @Nonnull Map<String, String> replace = Maps.newHashMap();
	private int id = -1;

	public @Nonnull IChatComponent build() {
		IChatComponent chat;
		if (this.chat!=null)
			chat = this.chat;
		else if (this.useTranslation&&!this.useJson)
			chat = new ChatComponentTranslation(this.text, this.params);
		else {
			String s;
			if (this.useTranslation)
				s = StatCollector.translateToLocal(this.text);
			else
				s = this.text;

			for (final Map.Entry<String, String> entry : this.replace.entrySet())
				s = StringUtils.replace(s, entry.getKey(), entry.getValue());

			if (this.params.length>0)
				s = String.format(s, this.params);

			if (this.useJson)
				try {
					chat = IChatComponent.Serializer.func_150699_a(s);
				} catch (final Exception e) {
					chat = new ChatComponentText("Invaild Json: "+this.text);
				}
			else
				chat = new ChatComponentText(this.text);
		}
		if (this.style!=null)
			chat.setChatStyle(this.style);
		return chat;
	}

	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.text)) {
			if (this.chat!=null)
				return StringUtils.isEmpty(this.chat.getUnformattedText());
			return true;
		}
		return false;
	}

	public @Nonnull ChatBuilder setId(final int id) {
		this.useId = true;
		this.id = id;
		return this;
	}

	public @Nonnull ChatBuilder setId() {
		setId(DefaultId);
		return this;
	}

	public @Nonnull ChatBuilder setChat(final @Nullable IChatComponent chat) {
		this.chat = chat;
		return this;
	}

	public @Nonnull ChatBuilder setText(final @Nonnull String text) {
		this.text = text;
		return this;
	}

	public @Nonnull ChatBuilder setParams(final @Nonnull Object... params) {
		this.params = params;
		return this;
	}

	public @Nonnull ChatBuilder setStyle(final @Nullable ChatStyle style) {
		this.style = style;
		return this;
	}

	public @Nonnull ChatBuilder useTranslation() {
		this.useTranslation = true;
		return this;
	}

	public @Nonnull ChatBuilder useJson() {
		this.useJson = true;
		return this;
	}

	public @Nonnull ChatBuilder replace(final @Nonnull String from, final @Nonnull String to) {
		this.replace.put(from, to);
		return this;
	}

	public static @Nonnull ChatBuilder create(final @Nonnull String text) {
		return new ChatBuilder().setText(text);
	}

	@SideOnly(Side.CLIENT)
	public void chatClient() {
		if (!isEmpty())
			chatClient(this);
	}

	public void sendPlayer(final @Nonnull ICommandSender target) {
		if (!isEmpty())
			sendPlayer(target, this);
	}

	@SideOnly(Side.CLIENT)
	public static void chatClient(final @Nonnull ChatBuilder chat) {
		final Minecraft mc = Client.mc;
		if (mc.thePlayer!=null) {
			final IChatComponent msg = chat.build();
			if (chat.useId)
				mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(msg, chat.id);
			else
				mc.thePlayer.addChatComponentMessage(msg);
		}
	}

	public static void sendPlayer(final @Nonnull ICommandSender target, final @Nonnull ChatBuilder chat) {
		target.addChatMessage(chat.build());
	}

	@SideOnly(Side.SERVER)
	public static void sendServer(final @Nonnull ChatBuilder chat) {
		final ServerConfigurationManager sender = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager();
		sender.sendChatMsg(chat.build());
	}
}
