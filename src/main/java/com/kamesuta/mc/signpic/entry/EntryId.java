package com.kamesuta.mc.signpic.entry;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class EntryId {
	private final String id;

	public EntryId(final String id) {
		this.id = id;
	}

	public String id() {
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EntryId))
			return false;
		final EntryId other = (EntryId) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("EntryId [id=%s]", this.id);
	}

	public static EntryId from(final String string) {
		return new EntryId(string);
	}

	public static EntryId fromStrings(final String[] strings) {
		return new EntryId(StringUtils.join(strings));
	}

	public static EntryId fromTile(final TileEntitySign tile) {
		return fromStrings(tile.signText);
	}

	public static EntryId fromChats(final IChatComponent[] chats) {
		final StringBuilder stb = new StringBuilder();
		for (final IChatComponent chat : chats) {
			if (chat!=null)
				stb.append(chat.getFormattedText());new ChatComponentText("").getUnformattedText();
		}
		return new EntryId(stb.toString());
	}
}
