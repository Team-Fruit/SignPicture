package com.kamesuta.mc.signpic.plugin;

public class SignData extends SignLocation {
	public String owner_uuid;
	public String owner_name;
	public String sign;
	public long last;

	public SignData(final String world, final int x, final int y, final int z, final String owner_uuid, final String owner_name, final String sign, final long last) {
		super(world, x, y, z);
		this.owner_uuid = owner_uuid;
		this.owner_name = owner_name;
		this.sign = sign;
		this.last = last;
	}
}
