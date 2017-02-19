package com.kamesuta.mc.signpic.entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.signpic.attr.CompoundAttr;
import com.kamesuta.mc.signpic.attr.CompoundAttrBuilder;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.mode.CurrentMode;

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
		result = prime*result+id().hashCode();
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
		if (!id().equals(other.id()))
			return false;
		return true;
	}

	@Override
	public @Nonnull String toString() {
		return String.format("EntryId [id=%s]", id());
	}

	public static @Nonnull EntryId from(final @Nullable String string) {
		if (string!=null&&!StringUtils.isEmpty(string))
			if (StringUtils.equals(string, PreviewEntryId.previewid))
				return PreviewEntryId.instance;
			else
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

	public static class PreviewEntryId extends EntryId {
		public static final @Nonnull String previewid = "{#}";
		public static final @Nonnull PreviewEntryId instance = new PreviewEntryId();

		private PreviewEntryId() {
			super(previewid);
		}

		@Override
		public @Nonnull String id() {
			final EntryId id = CurrentMode.instance.getEntryId();
			if (!(id instanceof PreviewEntryId))
				return id.id();
			return "";
		}

		@Override
		public String toString() {
			return "PreviewEntryId";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime*result+previewid.hashCode();
			return result;
		}

		@Override
		public boolean equals(@Nullable final Object obj) {
			return this==obj;
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
		return !(StringUtils.isEmpty(id())||StringUtils.containsOnly(id(), "!")||StringUtils.containsOnly(id(), "$"));
	}

	private boolean hasMeta() {
		return StringUtils.endsWith(id(), "]")&&StringUtils.contains(id(), "[")&&StringUtils.contains(StringUtils.substring(id(), 0, StringUtils.lastIndexOf(id(), "[")), ".")||
				hasPrefix()&&StringUtils.endsWith(id(), "}")&&StringUtils.contains(id(), "{");
	}

	public boolean isOutdated() {
		return StringUtils.endsWith(id(), "]")&&StringUtils.contains(id(), "[");
	}

	private boolean hasPrefix() {
		final int i = StringUtils.indexOf(id(), "#");
		return 0<=i&&i<2;
	}

	public boolean isValid() {
		return hasContentId()&&hasMeta();
	}

	public @Nullable String getPrePrefix() {
		if (StringUtils.indexOf(id(), "#")==1)
			return StringUtils.substring(id(), 0, 0);
		else
			return null;
	}

	public @Nullable ContentId getContentId() {
		if (hasContentId()) {
			String id;
			if (StringUtils.contains(id(), "["))
				id = StringUtils.substring(id(), 0, StringUtils.lastIndexOf(id(), "["));
			else if (hasPrefix()&&StringUtils.contains(id(), "{"))
				id = StringUtils.substring(id(), StringUtils.indexOf(id(), "#")+1, StringUtils.lastIndexOf(id(), "{"));
			else
				id = id();
			return ContentId.from(id);
		}
		return null;
	}

	public @Nullable String getMetaSource() {
		if (hasMeta())
			if (StringUtils.endsWith(id(), "}"))
				return StringUtils.substring(id(), StringUtils.lastIndexOf(id(), "{")+1, StringUtils.length(id())-1);
			else
				return StringUtils.substring(id(), StringUtils.lastIndexOf(id(), "[")+1, StringUtils.length(id())-1);
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
		return StringUtils.length(id())<=15*4;
	}

	public boolean isNameable() {
		return StringUtils.length(id())<=40;
	}

	public void toStrings(final @Nullable String[] sign) {
		if (sign!=null) {
			final int length = StringUtils.length(id());
			for (int i = 0; i<4; i++)
				sign[i] = StringUtils.substring(id(), 15*i, Math.min(15*(i+1), length));
		}
	}

	public int getLastLine() {
		return StringUtils.length(id())/15;
	}

	public void toEntity(final @Nullable TileEntitySign tile) {
		if (tile!=null)
			toStrings(tile.signText);
	}

	public @Nonnull Entry entry() {
		return EntryManager.instance.get(this);
	}
}
