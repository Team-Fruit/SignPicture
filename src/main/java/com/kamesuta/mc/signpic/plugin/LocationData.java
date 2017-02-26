package com.kamesuta.mc.signpic.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LocationData extends DomainData {

	private @Nullable String worldName;

	private @Nullable Integer x;

	private @Nullable Integer y;

	private @Nullable Integer z;

	public @Nonnull String getWorldName() {
		if (this.worldName!=null)
			return this.worldName;
		return this.worldName = "";
	}

	public @Nonnull Integer getX() {
		if (this.x!=null)
			return this.x;
		return this.x = 0;
	}

	public @Nonnull Integer getY() {
		if (this.y!=null)
			return this.y;
		return this.y = 0;
	}

	public @Nonnull Integer getZ() {
		if (this.z!=null)
			return this.z;
		return this.z = 0;
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
}
