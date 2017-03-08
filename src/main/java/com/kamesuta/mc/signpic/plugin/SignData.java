package com.kamesuta.mc.signpic.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SignData extends DomainData {

	public static @Nonnull Gson dateGson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();

	private @Nullable String sign;

	private @Nullable String playerName;

	private @Nullable String playerUUID;

	private @Nullable String worldName;

	private int x;

	private int y;

	private int z;

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

	public @Nonnull String getSign() {
		if (this.sign!=null)
			return this.sign;
		return this.sign = "";
	}

	public @Nonnull String getWorldName() {
		if (this.worldName!=null)
			return this.worldName;
		return this.worldName = "";
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public void setPlayerName(final @Nonnull String playerName) {
		this.playerName = playerName;
	}

	public void setPlayerUUID(final @Nonnull String playerUUID) {
		this.playerUUID = playerUUID;
	}

	public void setSign(final @Nonnull String sign) {
		this.sign = sign;
	}

	public void setWorldName(final @Nonnull String worldName) {
		this.worldName = worldName;
	}

	public void setX(final @Nonnull Integer x) {
		this.x = x;
	}

	public void setY(final @Nonnull Integer y) {
		this.y = y;
	}

	public void setZ(final @Nonnull Integer z) {
		this.z = z;
	}

	@Override
	public @Nonnull String toString() {
		return dateGson.toJson(this);
	}
}
