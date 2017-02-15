package com.kamesuta.mc.signpic.plugin;

import com.kamesuta.mc.signpic.Client;

public class SignLocation {
	public String world;
	public int x;
	public int y;
	public int z;

	public SignLocation(final String world, final int x, final int y, final int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return Client.gson.toJson(this);
	}
}
