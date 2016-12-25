package com.kamesuta.mc.signpic.entry.content;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.util.ResourceLocation;

public class ContentId {
	private final @Nonnull String id;

	public ContentId(@Nonnull String uri) {
		if (StringUtils.contains(uri, "http://"))
			uri = ""+StringUtils.substring(uri, 7, StringUtils.length(uri));
		else if (StringUtils.contains(uri, "https://"))
			uri = "$"+StringUtils.substring(uri, 8, StringUtils.length(uri));
		this.id = uri;
	}

	public @Nonnull String getID() {
		return this.id;
	}

	public @Nonnull String getURI() {
		if (!StringUtils.startsWith(this.id, "!"))
			if (StringUtils.startsWith(this.id, "$"))
				return "https://"+StringUtils.substring(this.id, 1);
			else if (!StringUtils.startsWith(this.id, "http://")&&!StringUtils.startsWith(this.id, "https://"))
				return "http://"+this.id;
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result+this.id.hashCode();
		return result;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof ContentId))
			return false;
		final ContentId other = (ContentId) obj;
		if (!this.id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public @Nullable String toString() {
		return String.format("ContentId [id=%s]", this.id);
	}

	public boolean isResource() {
		return this.id.startsWith("!");
	}

	public ResourceLocation getResource() {
		return new ResourceLocation(StringUtils.substring(this.id, 1));
	}

	public @Nonnull Content content() {
		return ContentManager.instance.get(this);
	}

	public static ContentId fromResource(final ResourceLocation location) {
		return new ContentId("!"+location.toString());
	}
}
