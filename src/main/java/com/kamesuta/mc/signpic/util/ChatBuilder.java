package com.kamesuta.mc.signpic.util;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChatBuilder {
	public static final int DefaultId = 877;

	private @Nullable ITextComponent chat = null;
	private @Nullable Style style = null;
	private @Nonnull String text = "";
	private @Nonnull Object[] params = new Object[0];
	private boolean useTranslation = false;
	private boolean useJson = false;
	private boolean useId = false;
	private final @Nonnull Map<String, String> replace = Maps.newHashMap();
	private int id = -1;

	public @Nonnull ITextComponent build() {
		ITextComponent chat;
		if (this.chat!=null)
			chat = this.chat;
		else if (this.useTranslation&&!this.useJson)
			chat = new TextComponentTranslation(this.text, this.params);
		else {
			String s;
			if (this.useTranslation)
				s = translateToLocal(this.text);
			else
				s = this.text;

			for (final Map.Entry<String, String> entry : this.replace.entrySet())
				s = StringUtils.replace(s, entry.getKey(), entry.getValue());

			if (this.params.length>0)
				s = String.format(s, this.params);

			if (this.useJson)
				try {
					chat = ITextComponent.Serializer.jsonToComponent(s);
				} catch (final Exception e) {
					chat = new TextComponentString("Invaild Json: "+this.text);
				}
			else
				chat = new TextComponentString(this.text);
		}
		if (this.style!=null)
			chat.setStyle(this.style);
		return chat;
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings("deprecation")
	public static String translateToLocal(final String text) {
		return net.minecraft.util.text.translation.I18n.translateToLocal(text);
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

	public @Nonnull ChatBuilder setChat(final @Nullable ITextComponent chat) {
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

	public @Nonnull ChatBuilder setStyle(final @Nullable Style style) {
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
			final ITextComponent msg = chat.build();
			if (chat.useId)
				mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(msg, chat.id);
			else
				mc.thePlayer.addChatComponentMessage(msg, false);
		}
	}

	public static void sendPlayer(final @Nonnull ICommandSender target, final @Nonnull ChatBuilder chat) {
		target.addChatMessage(chat.build());
	}

	@SideOnly(Side.SERVER)
	public static void sendServer(final @Nonnull ChatBuilder chat) {
		final PlayerList player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
		player.sendChatMsg(chat.build());
	}
}