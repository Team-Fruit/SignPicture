package com.kamesuta.mc.signpic.entry;

import net.minecraft.util.ResourceLocation;

public class EntryPath {
	private final String path;

	public EntryPath(final String path) {
		this.path = path;
	}

	public String path() {
		return this.path;
	}

	public boolean isResource() {
		return this.path.startsWith("!");
	}

	public ResourceLocation getResource() {
		return new ResourceLocation(this.path.substring(1));
	}

	public static EntryPath fromResource(final ResourceLocation location) {
		return new EntryPath("!" + location.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EntryPath))
			return false;
		final EntryPath other = (EntryPath) obj;
		if (this.path == null) {
			if (other.path != null)
				return false;
		} else if (!this.path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("EntryPath [path=%s]", this.path);
	}
}
