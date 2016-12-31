package com.kamesuta.mc.signpic.entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.CompoundAttrBuilder;
import com.kamesuta.mc.signpic.entry.content.ContentId;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;

public class EntryId {
	public static final @Nonnull EntryId blank = new EntryId("");

	private final @Nonnull String id;

	protected EntryId(final @Nonnull String id) {
		this.id = id;
	}

	public @Nonnull String id() {
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
		if (!(obj instanceof EntryId))
			return false;
		final EntryId other = (EntryId) obj;
		if (!this.id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public @Nonnull String toString() {
		return String.format("EntryId [id=%s]", this.id);
	}

	public static @Nonnull EntryId from(final @Nullable String string) {
		if (string!=null&&!StringUtils.isEmpty(string))
			return new EntryId(string);
		return blank;
	}

	public static @Nonnull EntryId fromStrings(final @Nullable String[] strings) {
		return from(StringUtils.join(strings));
	}

	public static @Nonnull EntryId fromTile(final @Nullable TileEntitySign tile) {
		if (tile==null)
			return blank;
		return fromStrings(tile.signText);
	}

	public static @Nonnull EntryId fromChats(final @Nullable IChatComponent[] chats) {
		if (chats==null)
			return blank;
		final StringBuilder stb = new StringBuilder();
		for (final IChatComponent chat : chats)
			if (chat!=null)
				stb.append(chat.getUnformattedText());
		return from(stb.toString());
	}

	public static class ItemEntryId extends EntryId {
		public static final @Nonnull ItemEntryId blank = new ItemEntryId(EntryId.blank, null);

		private final @Nullable String name;

		protected ItemEntryId(final @Nonnull EntryId id, final @Nullable String name) {
			super(id.id());
			this.name = name;
		}

		public boolean hasName() {
			return this.name!=null;
		}

		public @Nullable String getName() {
			return this.name;
		}

		public static boolean hasName(final @Nullable NBTTagCompound nbt) {
			if (nbt!=null&&nbt.hasKey("display", 10)) {
				final NBTTagCompound nbttagcompound = nbt.getCompoundTag("display");
				if (nbttagcompound.hasKey("Name", 8))
					return true;
			}
			return false;
		}
	}

	public static @Nonnull ItemEntryId fromItemStack(final @Nullable ItemStack itemStack) {
		if (itemStack!=null) {
			final NBTTagCompound nbt = itemStack.getTagCompound();
			if (nbt!=null)
				if (ItemEntryId.hasName(nbt)) {
					final String name = itemStack.getDisplayName();
					final int index = StringUtils.lastIndexOf(name, "}");
					String itemname = StringUtils.substringAfterLast(name, "}");
					if (StringUtils.isEmpty(itemname))
						itemname = null;
					return new ItemEntryId(from(StringUtils.substring(itemStack.getDisplayName(), 0, index!=StringUtils.INDEX_NOT_FOUND ? index+1 : 0)), itemname);
				}
		}
		return ItemEntryId.blank;
	}

	private boolean hasContentId() {
		return !(StringUtils.isEmpty(this.id)||StringUtils.containsOnly(this.id, "!")||StringUtils.containsOnly(this.id, "$"));
	}

	private boolean hasMeta() {
		return StringUtils.endsWith(this.id, "]")&&StringUtils.contains(this.id, "[")||
				hasPrefix()&&StringUtils.endsWith(this.id, "}")&&StringUtils.contains(this.id, "{");
	}

	private boolean hasPrefix() {
		final int i = StringUtils.indexOf(this.id, "#");
		return 0<=i&&i<2;
	}

	public @Nullable String getPrePrefix() {
		if (StringUtils.indexOf(this.id, "#")==1)
			return StringUtils.substring(this.id, 0, 0);
		else
			return null;
	}

	public @Nullable ContentId getContentId() {
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

	public @Nullable String getMetaSource() {
		if (hasMeta())
			if (StringUtils.endsWith(this.id, "}"))
				return StringUtils.substring(this.id, StringUtils.lastIndexOf(this.id, "{")+1, StringUtils.length(this.id)-1);
			else
				return StringUtils.substring(this.id, StringUtils.lastIndexOf(this.id, "[")+1, StringUtils.length(this.id)-1);
		return null;
	}

	public @Nullable CompoundAttr getMeta() {
		final String metasource = getMetaSource();
		if (metasource!=null)
			return new CompoundAttr(metasource);
		return null;
	}

	public @Nullable CompoundAttrBuilder getMetaBuilder() {
		final String metasource = getMetaSource();
		if (metasource!=null)
			return new CompoundAttrBuilder().parse(metasource);
		return null;
	}

	public boolean isPlaceable() {
		return StringUtils.length(this.id)<=15*4;
	}

	public boolean isNameable() {
		return StringUtils.length(this.id)<=40;
	}

	public void toStrings(final @Nullable String[] sign) {
		if (sign!=null) {
			final int length = StringUtils.length(this.id);
			for (int i = 0; i<4; i++)
				sign[i] = StringUtils.substring(this.id, 15*i, Math.min(15*(i+1), length));
		}
	}

	public int getLastLine() {
		return StringUtils.length(this.id)/15;
	}

	public void toEntity(final @Nullable TileEntitySign tile) {
		if (tile!=null)
			toStrings(tile.signText);
	}

	public @Nonnull Entry entry() {
		return EntryManager.instance.get(this);
	}
}
