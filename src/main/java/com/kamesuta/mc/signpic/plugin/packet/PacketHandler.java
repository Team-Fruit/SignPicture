package com.kamesuta.mc.signpic.plugin.packet;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.Client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class PacketHandler {
	public static @Nonnull PacketHandler instance = new PacketHandler();
	public static @Nonnull FMLEventChannel channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("signpic.list");

	private PacketHandler() {
	}

	public static void init() {
		channel.register(instance);
	}

	@SubscribeEvent
	public void onClientPacket(final @Nonnull FMLNetworkEvent.ClientCustomPacketEvent event) {
		final Packet packet = event.packet.toC17Packet();
		if (packet instanceof C17PacketCustomPayload) {
			final C17PacketCustomPayload pluginmessage = (C17PacketCustomPayload) packet;
			onPacket(pluginmessage.func_149559_c());
		}
	}

	public void onPacket(final @Nonnull String data) {
		final SignPicturePacket packet = Client.gson.fromJson(data, SignPicturePacket.class);
	}

	public void sendPacket(final @Nonnull SignPicturePacket p) {
		final byte[] message = Client.gson.toJson(p).getBytes();
		final ByteBuf data = Unpooled.wrappedBuffer(message);
		final C17PacketCustomPayload packet = new C17PacketCustomPayload("signpic.list", new PacketBuffer(data));
		final FMLProxyPacket pkt = new FMLProxyPacket(packet);
		channel.sendToServer(pkt);
	}
}