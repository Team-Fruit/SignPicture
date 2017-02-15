package com.kamesuta.mc.signpic.plugin;

import javax.annotation.Nonnull;

public class SignData extends SignLocation {
	public @Nonnull String owner_uuid;
	public @Nonnull String owner_name;
	public @Nonnull String sign;
	public long last;

	public SignData(final @Nonnull String world, final int x, final int y, final int z, final @Nonnull String owner_uuid, final @Nonnull String owner_name, final @Nonnull String sign, final long last) {
		super(world, x, y, z);
		this.owner_uuid = owner_uuid;
		this.owner_name = owner_name;
		this.sign = sign;
		this.last = last;
	}
}
