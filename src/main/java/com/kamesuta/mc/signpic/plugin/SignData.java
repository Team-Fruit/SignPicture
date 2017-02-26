package com.kamesuta.mc.signpic.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kamesuta.mc.signpic.Client;

public class SignData extends LocationData {
	public static Gson dateGson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();

	private @Nullable String sign;

	private @Nullable String playerName;

	private @Nullable String playerUUID;

	@Override
	public @Nonnull String toString() {
		return Client.gson.toJson(this);
	}

	public @Nonnull String getSign() {
		if (this.sign!=null)
			return this.sign;
		return this.sign = "";
	}

	public @Nonnull String getPlayerName() {
		if (this.playerName!=null)
			return this.playerName;
		return this.playerName = "";
	}

	public @Nonnull String getPlayerUUID() {
		if (this.playerUUID!=null)
			return this.playerUUID;
		return this.playerUUID = "";
	}

	public void setSign(final @Nonnull String sign) {
		this.sign = sign;
	}

	public void setPlayerName(final @Nonnull String playerName) {
		this.playerName = playerName;
	}

	public void setPlayerUUID(final @Nonnull String playerUUID) {
		this.playerUUID = playerUUID;
	}

}
