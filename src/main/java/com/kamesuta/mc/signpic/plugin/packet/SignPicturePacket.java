package com.kamesuta.mc.signpic.plugin.packet;

public class SignPicturePacket {
	public String command;
	public String token;
	public String data;

	public SignPicturePacket(final String command, final String token, final String data) {
		this.command = command;
		this.token = token;
		this.data = data;
	}
}
