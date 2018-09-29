package com.kamesuta.mc.signpic.entry;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.kamesuta.mc.signpic.attr.AttrReaders;
import com.kamesuta.mc.signpic.attr.AttrWriters;
import com.kamesuta.mc.signpic.compat.Compat.CompatTextComponent;
import com.kamesuta.mc.signpic.compat.Compat.CompatTileEntitySign;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.mode.CurrentMode;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;

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

		@Override
		public String toString() {
			return String.format("ItemEntryId [name=%s]", this.name);
		}

		public static @Nonnull ItemEntryId fromItemStack(final @Nullable ItemStack itemStack) {
			if (itemStack!=null) {
				final NBTTagCompound nbt = itemStack.getTagCompound();
				if (nbt!=null)
					if (nbt.hasKey("BlockEntityTag", 10)) {
						final NBTTagCompound tag = (NBTTagCompound) nbt.getTag("BlockEntityTag");
						final TileEntitySign tile = new TileEntitySign();
						tile.readFromNBT(tag);
						String name = null;
						if (ItemEntryId.hasName(nbt))
							name = itemStack.getDisplayName();
						return new ItemEntryId(SignEntryId.fromTile(tile), name);
					} else if (ItemEntryId.hasName(nbt)) {
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
	}

	public static class SignEntryId extends EntryId {
		public static final @Nonnull SignEntryId blank = new SignEntryId("", Lists.newArrayList());

		public final List<CompatTextComponent> chats;

		protected SignEntryId(final @Nonnull String id, final @Nonnull List<CompatTextComponent> chats) {
			super(id);
			this.chats = chats;
		}

		@Override
		public String toString() {
			return String.format("SignEntryId [chats=%s]", this.chats);
		}

		public void toEntity(final @Nullable TileEntitySign tile) {
			if (tile!=null)
				CompatTileEntitySign.setSignText(tile, this.chats);
		}

		public static @Nonnull SignEntryId fromEntryId(final @Nullable EntryId entryId) {
			if (entryId==null)
				return blank;
			if (entryId instanceof SignEntryId)
				return (SignEntryId) entryId;
			final String id = entryId.id();
			final int length = StringUtils.length(id);
			final List<String> lines = Lists.newArrayList();
			for (int i = 0; i<4; i++)
				lines.add(StringUtils.substring(id, 15*i, Math.min(15*(i+1), length)));
			final List<CompatTextComponent> clines = Lists.transform(lines, t -> CompatTextComponent.fromText(t));
			return new SignEntryId(id, clines);
		}

		public static @Nonnull SignEntryId fromChats(final @Nullable List<CompatTextComponent> clines) {
			if (clines==null)
				return blank;
			final List<String> lines = Lists.transform(clines, t -> t==null ? null : t.getUnformattedText());
			final String id = StringUtils.join(lines, null);
			return new SignEntryId(id, clines);
		}

		public static @Nonnull SignEntryId fromTile(final @Nullable TileEntitySign tile) {
			if (tile==null)
				return blank;
			return fromChats(CompatTileEntitySign.getSignText(tile));
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

	private boolean hasContentId() {
		return !(StringUtils.isEmpty(id())||StringUtils.containsOnly(id(), "!")||StringUtils.containsOnly(id(), "$"));
	}

	private boolean hasMeta() {
		if (StringUtils.endsWith(id(), "]")&&StringUtils.contains(id(), "[")) {
			final String idstr = StringUtils.substring(id(), 0, StringUtils.lastIndexOf(id(), "["));
			return StringUtils.contains(idstr, ".")&&StringUtils.contains(idstr, "/");
		} else if (hasPrefix()&&StringUtils.endsWith(id(), "}")&&StringUtils.contains(id(), "{"))
			return true;
		return false;
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

	public @Nullable AttrReaders getMeta() {
		final String metasource = getMetaSource();
		if (metasource!=null)
			return new AttrReaders(metasource);
		return null;
	}

	public @Nullable AttrWriters getMetaBuilder() {
		final String metasource = getMetaSource();
		if (metasource!=null)
			return new AttrWriters().parse(metasource);
		return null;
	}

	public boolean isPlaceable() {
		return StringUtils.length(id())<=15*4;
	}

	public boolean isNameable() {
		return StringUtils.length(id())<=40;
	}

	public int getLastLine() {
		return StringUtils.length(id())/15;
	}

	public @Nonnull Entry entry() {
		return EntryManager.instance.get(this);
	}
}
