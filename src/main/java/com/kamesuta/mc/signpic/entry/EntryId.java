package com.kamesuta.mc.signpic.entry;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;

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
		result = prime*result+(this.id==null ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (!(obj instanceof EntryId))
			return false;
		final EntryId other = (EntryId) obj;
		if (this.id==null) {
			if (other.id!=null)
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
		return fromChats(tile.signText);
	}

	public static EntryId fromChats(final IChatComponent[] chats) {
		final StringBuilder stb = new StringBuilder();
		for (final IChatComponent chat : chats)
			if (chat!=null)
				stb.append(chat.getUnformattedText());
		return new EntryId(stb.toString());
	}

	public boolean hasContentId() {
		return !(StringUtils.isEmpty(this.id)||StringUtils.containsOnly(this.id, "!")||StringUtils.containsOnly(this.id, "$"));
	}

	public boolean hasMeta() {
		return StringUtils.endsWith(this.id, "]")&&StringUtils.contains(this.id, "[")||
				hasPrefix()&&StringUtils.endsWith(this.id, "}")&&StringUtils.contains(this.id, "{");
	}

	public boolean hasPrefix() {
		final int i = StringUtils.indexOf(this.id, "#");
		return 0<=i&&i<2;
	}

	public boolean hasPrePrefix() {
		final int i = StringUtils.indexOf(this.id, "#");
		return i==1;
	}

	public String getPrePrefix() {
		if (hasPrePrefix())
			return StringUtils.substring(this.id, 0, 0);
		else
			return "";
	}

	public boolean isValid() {
		return hasContentId()&&hasMeta();
	}

	public ContentId getContentId() {
		if (hasContentId()) {
			String id;
			if (StringUtils.contains(this.id, "["))
				id = StringUtils.substring(this.id, 0, StringUtils.lastIndexOf(this.id, "["));
			else if (hasPrefix()&&StringUtils.contains(this.id, "{"))
				id = StringUtils.substring(this.id, StringUtils.indexOf(this.id, "#")+1, StringUtils.lastIndexOf(this.id, "{"));
			else
				id = this.id;
			return new ContentId(id);
		}
		return null;
	}

	public ImageMeta getMeta() {
		if (hasMeta())
			if (StringUtils.endsWith(this.id, "}"))
				return new ImageMeta().parse(StringUtils.substring(this.id, StringUtils.lastIndexOf(this.id, "{")+1, StringUtils.length(this.id)-1));
			else
				return new ImageMeta().parse(StringUtils.substring(this.id, StringUtils.lastIndexOf(this.id, "[")+1, StringUtils.length(this.id)-1));
		else
			return null;
	}

	public boolean isPlaceable() {
		return StringUtils.length(this.id)<=15*4;
	}

	public void toStrings(final String[] sign) {
		final int length = StringUtils.length(this.id);
		for (int i = 0; i<4; i++)
			sign[i] = StringUtils.substring(this.id, 15*i, Math.min(15*(i+1), length));
	}

	public void toChats(final IChatComponent[] sign) {
		final int length = StringUtils.length(this.id);
		for (int i = 0; i<4; i++)
			sign[i] = new ChatComponentText(StringUtils.substring(this.id, 15*i, Math.min(15*(i+1), length)));
	}

	public int getLastLine() {
		return StringUtils.length(this.id)/15;
	}

	public void toEntity(final TileEntitySign tile) {
		toChats(tile.signText);
	}

	public Entry entry() {
		return EntryManager.instance.get(this);
	}
}
