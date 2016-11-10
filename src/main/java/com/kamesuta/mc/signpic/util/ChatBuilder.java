package com.kamesuta.mc.signpic.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChatBuilder {
	public static final int DefaultId = 877;

	private IChatComponent chat = null;
	private ChatStyle style = null;
	private String text = "";
	private Object[] params = new Object[0];
	private boolean useTranslation = false;
	private boolean useJson = false;
	private boolean useId = false;
	private final Map<String, String> replace = Maps.newHashMap();
	private int id = -1;

	public ChatBuilder() {
	}

	public IChatComponent build() {
		IChatComponent chat;
		if (this.chat==null) {
			if (this.useTranslation&&!this.useJson)
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
						chat = IChatComponent.Serializer.jsonToComponent(s);
					} catch (final Exception e) {
						chat = new ChatComponentText("Invaild Json: "+this.text);
					}
				else
					chat = new ChatComponentText(this.text);
			}
		} else {
			chat = this.chat;
		}
		if (chat!=null&&this.style!=null)
			chat.setChatStyle(this.style);
		return chat;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(this.text)&&(this.chat==null||StringUtils.isEmpty(this.chat.getUnformattedText()));
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

	public ChatBuilder setStyle(final ChatStyle style) {
		this.style = style;
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

	public ChatBuilder replace(final String from, final String to) {
		this.replace.put(from, to);
		return this;
	}

	public static ChatBuilder create(final String text) {
		return new ChatBuilder().setText(text);
	}

	@SideOnly(Side.CLIENT)
	public void chatClient() {
		if (!isEmpty())
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