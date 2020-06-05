package net.teamfruit.emojicord.emoji;

import java.io.File;
import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.util.ResourceLocation;
import net.teamfruit.emojicord.Locations;
import net.teamfruit.emojicord.Reference;
import net.teamfruit.emojicord.util.Base64Utils;

public abstract class EmojiId {
	public final EmojiIdNode node = new EmojiIdNode();

	public abstract String getId();

	public abstract String getType();

	public File getCache() {
		return new File(Locations.instance.getCacheDirectory(), String.format("%s/%s", getType(), getCacheName()));
	}

	public abstract String getRemote();

	public String getCacheName() {
		return getId();
	}

	public ResourceLocation getResourceLocation() {
		return new ResourceLocation(Reference.MODID, String.format("textures/emojis/%s/%s", getType(), getCacheName()));
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getType());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof EmojiId))
			return false;
		final EmojiId other = (EmojiId) obj;
		return Objects.equals(getId(), other.getId())&&Objects.equals(getType(), other.getType());
	}

	@Override
	public String toString() {
		return "EmojiId [id="+getId()+", type="+getType()+"]";
	}

	public class EmojiIdNode extends Node<EmojiId> {
		public EmojiIdNode() {
			super(EmojiId.this);
		}

		public String getUniqueName(final String name) {
			final int count = countPrev();
			if (count>0)
				return name+"~"+count;
			return name;
		}
	}

	public static class Node<T> {
		public final T value;
		public Node<T> prev;

		public Node(final T value) {
			this.value = value;
		}

		public void linkPrev(final Node<T> insert) {
			this.prev = insert;
		}

		public int countPrev() {
			int count = 0;
			if (this.prev!=null)
				for (Node<T> i = this; i.prev!=null; i = i.prev)
					count++;
			return count;
		}
	}

	public static class StandardEmojiId extends EmojiId {
		private final String url;
		private final String cache;

		public StandardEmojiId(final String url, final String cache) {
			this.url = url;
			this.cache = cache;
		}

		@Override
		public String getId() {
			return this.url;
		}

		@Override
		public String getCacheName() {
			return StringUtils.replace(this.cache, ":", ".");
		}

		@Override
		public String getType() {
			return "standard";
		}

		@Override
		public String getRemote() {
			return getId();
		}

		public static @Nullable EmojiId fromAlias(final String id) {
			return StandardEmojiIdDictionary.instance.aliasDictionary.get(id);
		}

		public static @Nullable EmojiId fromUtf(final String surrogates) {
			return StandardEmojiIdDictionary.instance.utfDictionary.get(surrogates);
		}
	}

	public static class DiscordEmojiId extends EmojiId {
		private final long id;

		private DiscordEmojiId(final long id) {
			this.id = id;
		}

		@Override
		public String getId() {
			return Long.toString(this.id);
		}

		@Override
		public String getType() {
			return "discord";
		}

		@Override
		public String getRemote() {
			return "https://cdn.discordapp.com/emojis/"+getId();
		}

		public String getEncodedId() {
			return Base64Utils.encode(this.id);
		}

		public static @Nullable EmojiId fromDecimalId(final long id) {
			return new DiscordEmojiId(id);
		}

		public static @Nullable EmojiId fromDecimalId(final String id) {
			if (!NumberUtils.isDigits(id))
				return null;
			return DiscordEmojiId.fromDecimalId(NumberUtils.toLong(id));
		}

		public static @Nullable EmojiId fromEncodedId(final String id) {
			try {
				return new DiscordEmojiId(Base64Utils.decode(id));
			} catch (final IllegalArgumentException e) {
			}
			return null;
		}
	}
}
