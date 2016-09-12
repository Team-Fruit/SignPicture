package com.kamesuta.mc.signpic.util;

import com.google.gson.JsonSyntaxException;
import com.kamesuta.mc.signpic.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChatBuilder {
	public static final int DefaultId = 877;

	private ITextComponent chat = null;
	private Style style = null;
	private String text = "";
	private Object[] params = new Object[0];
	private boolean useTranslation = false;
	private boolean useJson = false;
	private boolean useId = false;
	private int id = -1;

	public ChatBuilder() {}

	public ITextComponent build() {
		ITextComponent chat;
		if (this.chat==null) {
			if (this.useTranslation && !this.useJson)
				chat = new TextComponentTranslation(this.text, this.params);
			else {
				String s;
				if (this.useTranslation) {
					s = String.format(translateToLocal(this.text), this.params);
				} else
					s = this.text;

				if (this.useJson)
					try {
						chat = ITextComponent.Serializer.jsonToComponent(s);
					} catch (final JsonSyntaxException e) {
						chat = new TextComponentString("Invaild Json: " + this.text);
					}
				else
					chat = new TextComponentString(this.text);
			}
		} else {
			chat = this.chat;
		}
		if (chat!=null && this.style!=null)
			chat.setStyle(this.style);
		return chat;
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings("deprecation")
	public static String translateToLocal(final String text) {
		return net.minecraft.util.text.translation.I18n.translateToLocal(text);
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

	public ChatBuilder setChat(final ITextComponent chat) {
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

	public ChatBuilder setStyle(final Style style) {
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
			final ITextComponent msg = chat.build();
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
		FMLCommonHandler.instance().getMinecraftServerInstance().addChatMessage(chat.build());
	}
}