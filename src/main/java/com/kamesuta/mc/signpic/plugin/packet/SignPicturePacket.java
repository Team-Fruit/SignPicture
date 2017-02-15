package com.kamesuta.mc.signpic.plugin.packet;

import javax.annotation.Nonnull;

public class SignPicturePacket {
	public @Nonnull String command;
	public @Nonnull String token;
	public @Nonnull String data;

	public SignPicturePacket(final @Nonnull String command, final @Nonnull String token, final @Nonnull String data) {
		this.command = command;
		this.token = token;
		this.data = data;
	}
}
