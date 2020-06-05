package net.teamfruit.emojicord.emoji;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;

import net.teamfruit.emojicord.emoji.Models.EmojiDiscord;
import net.teamfruit.emojicord.emoji.Models.EmojiDiscordGroup;
import net.teamfruit.emojicord.emoji.Models.EmojiDiscordList;
import net.teamfruit.emojicord.util.DataUtils;

public class DiscordEmojiIdDictionary {
	public static final DiscordEmojiIdDictionary instance = new DiscordEmojiIdDictionary();

	public final ListMultimap<String, EmojiId> dictionary = Multimaps.newListMultimap(Maps.newHashMap(),
			() -> Lists.newArrayList());
	public final List<EmojiDiscordList> groups = Lists.newArrayList();
	public final List<PickerGroup> pickerGroups = Lists.newArrayList();

	private File dictDir;

	public EmojiId get(final String name) {
		final String str = StringUtils.substringBefore(name, "~"); // not substringBeforeLast
		final Optional<EmojiId> result = this.dictionary.get(str).stream().filter(e -> e.node.getUniqueName(str).equals(name)).findFirst();
		return result.orElse(null);
	}

	public Map<String, EmojiId> get() {
		final Map<String, EmojiId> dict = Maps.newHashMap();
		for (final Entry<String, List<EmojiId>> entry : Multimaps.asMap(this.dictionary).entrySet()) {
			final String key = entry.getKey();
			final List<EmojiId> values = entry.getValue();
			if (!values.isEmpty())
				dict.put(key, values.get(0));
			values.stream().filter(e -> e.node.countPrev()>0).forEach(e -> {
				dict.put(e.node.getUniqueName(key), e);
			});
		}
		return dict;
	}

	public void register(final String name, final EmojiId id) {
		final List<EmojiId> list = this.dictionary.get(name);
		if (!list.isEmpty()) {
			final EmojiId last = list.get(list.size()-1);
			id.node.linkPrev(last.node);
		} else {
			final EmojiId stdId = StandardEmojiIdDictionary.instance.nameDictionary.get(name);
			if (stdId!=null)
				id.node.linkPrev(stdId.node);
		}
		list.add(id);
	}

	public void clear() {
		this.dictionary.clear();
		this.groups.clear();
		this.pickerGroups.clear();
	}

	public void init(final File dictDir) {
		this.dictDir = dictDir;
	}

	public File getDictionaryDirectory() {
		return this.dictDir;
	}

	public void loadAll() {
		if (this.dictDir!=null) {
			clear();
			new EmojiDictionaryLoader(this).loadAll(this.dictDir);
		}
	}

	public static class EmojiDictionaryLoader {
		private final DiscordEmojiIdDictionary dictionary;

		public EmojiDictionaryLoader(final DiscordEmojiIdDictionary dictionary) {
			this.dictionary = dictionary;
		}

		public void loadAll(final File dictDir) {
			final File groupsDir = dictDir;
			//final File groupsDir = new File(dictDir, "mappings");
			//final File manifestFile = new File(dictDir, "indexes.json");

			groupsDir.mkdirs();

			final List<EmojiDiscordList> lists = Lists.newArrayList();
			for (final File dictFile : FileUtils.listFiles(groupsDir, new String[] { "json" }, true)) {
				final EmojiDiscordList emojiList = DataUtils.loadFile(dictFile, EmojiDiscordList.class,
						"Discord Emoji Dictionary");
				if (emojiList!=null)
					lists.add(emojiList);
			}

			/*
			final EmojiDiscordIndexFolder listIndex = DataUtils.loadFile(manifestFile, EmojiDiscordIndexFolder.class, null);
			Set<Set<String>> listIndexSample = null;
			if (listIndex!=null)
				try {
					final Map<String, Integer> listMap = IntStream.range(0, listIndex.lists.size())
							.boxed()
							.collect(Collectors.toMap(e -> listIndex.lists.get(e).id, Function.identity()));
					Collections.sort(lists, (a, b) -> {
						final Integer ia = listMap.get(a.id);
						final Integer ib = listMap.get(b.id);
						if (ia==null&&ib==null)
							return 0;
						if (ia==null)
							return 1;
						if (ib==null)
							return -1;
						return ib-ia;
					});
			
					for (final EmojiDiscordList list : lists) {
						final List<EmojiDiscordGroup> groupIndex = list.groups;
						final Map<String, Integer> groupMap = IntStream.range(0, groupIndex.size())
								.boxed()
								.collect(Collectors.toMap(e -> groupIndex.get(e).id, Function.identity()));
						Collections.sort(groupIndex, (a, b) -> {
							final Integer ia = groupMap.get(a.id);
							final Integer ib = groupMap.get(b.id);
							if (ia==null&&ib==null)
								return 0;
							if (ia==null)
								return 1;
							if (ib==null)
								return -1;
							return ib-ia;
						});
					}
			
					listIndexSample = listIndex.lists.stream().map(
							e -> e.groups.stream().map(f -> f.id).collect(Collectors.toSet())).collect(Collectors.toSet());
				} catch (final NullPointerException e) {
					Log.log.error("Manifest File is corrupted. ignored : ", e);
				}
			
			boolean updated = false;
			if (listIndexSample==null)
				updated = true;
			else {
				final Set<Set<String>> listsSample = lists.stream().map(
						e -> e.groups.stream().map(f -> f.id).collect(Collectors.toSet())).collect(Collectors.toSet());
				if (!listsSample.equals(listIndexSample))
					updated = true;
			}
			if (updated) {
				final EmojiDiscordIndexFolder ifolder = new EmojiDiscordIndexFolder();
				ifolder.lists = lists.stream().map(e -> {
					final EmojiDiscordIndexList ilist = new EmojiDiscordIndexList();
					ilist.id = e.id;
					ilist.name = e.name;
					ilist.groups = e.groups.stream().map(f -> {
						final EmojiDiscordIndexGroup igroup = new EmojiDiscordIndexGroup();
						igroup.id = f.id;
						igroup.name = f.name;
						return igroup;
					}).collect(Collectors.toList());
					return ilist;
				}).collect(Collectors.toList());
				DataUtils.saveFile(manifestFile, EmojiDiscordIndexFolder.class, ifolder,
						"Discord Emoji Dictionary Manifest File");
			}
			*/

			final List<PickerGroup> pickerGroups = Lists.newArrayList();
			for (final EmojiDiscordList emojiList : lists)
				if (emojiList!=null&&emojiList.groups!=null)
					for (final EmojiDiscordGroup emojiGroup : emojiList.groups)
						if (emojiGroup!=null&&emojiGroup.emojis!=null) {
							final List<PickerItem> pickerItems = Lists.newArrayList();
							for (final EmojiDiscord emoji : emojiGroup.emojis)
								if (emoji!=null) {
									final EmojiId id = EmojiId.DiscordEmojiId.fromDecimalId(emoji.id);
									if (id!=null) {
										this.dictionary.register(emoji.name, id);
										final String uid = id.node.getUniqueName(emoji.name);
										pickerItems.add(new PickerItem(":"+uid+":", ":"+uid+":", Lists.newArrayList(uid), id));
									}
								}
							if (!pickerItems.isEmpty())
								pickerGroups.add(new PickerGroup(emojiGroup.name, pickerItems));
						}

			this.dictionary.groups.addAll(lists);
			this.dictionary.pickerGroups.addAll(pickerGroups);
		}
	}
}
