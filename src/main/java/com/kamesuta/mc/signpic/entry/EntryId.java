package com.kamesuta.mc.signpic.entry;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.image.meta.ImageMeta;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;

public class EntryId {
	public static final EntryId blank = new EntryId("");

	private final String id;

	protected EntryId(final String id) {
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
		if (StringUtils.isEmpty(string))
			return blank;
		return new EntryId(string);
	}

	public static EntryId fromStrings(final String[] strings) {
		return from(StringUtils.join(strings));
	}

	public static EntryId fromTile(final TileEntitySign tile) {
		if (tile==null)
			return blank;
		return fromStrings(tile.signText);
	}

	public static EntryId fromChats(final IChatComponent[] chats) {
		if (chats==null)
			return blank;
		final StringBuilder stb = new StringBuilder();
		for (final IChatComponent chat : chats)
			if (chat!=null)
				stb.append(chat.getUnformattedText());
		return from(stb.toString());
	}

	public static EntryId fromItemStack(final ItemStack itemStack) {
		if (itemStack==null)
			return blank;
		final String name = itemStack.getDisplayName();
		final int index = StringUtils.lastIndexOf(name, "}");
		return from(StringUtils.substring(itemStack.getDisplayName(), 0, index!=StringUtils.INDEX_NOT_FOUND ? index+1 : 0));
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

	public String getMetaSource() {
		if (hasMeta())
			if (StringUtils.endsWith(this.id, "}"))
				return StringUtils.substring(this.id, StringUtils.lastIndexOf(this.id, "{")+1, StringUtils.length(this.id)-1);
			else
				return StringUtils.substring(this.id, StringUtils.lastIndexOf(this.id, "[")+1, StringUtils.length(this.id)-1);
		else
			return null;
	}

	public ImageMeta getMeta() {
		final String metasource = getMetaSource();
		if (metasource!=null)
			return new ImageMeta(metasource);
		return null;
	}

	public boolean isPlaceable() {
		return StringUtils.length(this.id)<=15*4;
	}

	public boolean isNameable() {
		return StringUtils.length(this.id)<=40;
	}

	public void toStrings(final String[] sign) {
		final int length = StringUtils.length(this.id);
		for (int i = 0; i<4; i++)
			sign[i] = StringUtils.substring(this.id, 15*i, Math.min(15*(i+1), length));
	}

	public int getLastLine() {
		return StringUtils.length(this.id)/15;
	}

	public void toEntity(final TileEntitySign tile) {
		toStrings(tile.signText);
	}

	public Entry entry() {
		return EntryManager.instance.get(this);
	}
}
