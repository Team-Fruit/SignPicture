package com.kamesuta.mc.signpic.plugin.search;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public enum StringFilterProperty implements DataFilterProperty<String> {
	ID {
		@Override
		public String get(final SignData data, final EntryId entry, final ContentId id) {
			return String.valueOf(data.getId());
		}
	},
	SIGN {
		@Override
		public String get(final SignData data, final EntryId entry, final ContentId id) {
			return data.getSign();
		}
	},
	PLAYER {
		@Override
		public String get(final SignData data, final EntryId entry, final ContentId id) {
			return data.getPlayerName();
		}
	},
	UUID {
		@Override
		public String get(final SignData data, final EntryId entry, final ContentId id) {
			return data.getPlayerUUID();
		}
	},
	WORLD {
		@Override
		public String get(final SignData data, final EntryId entry, final ContentId id) {
			return data.getWorldName();
		}
	},
	URL {
		@Override
		public String get(final SignData data, final EntryId entry, final ContentId content) {
			return content.getURI();
		}
	},
	URI {
		@Override
		public String get(final SignData data, final EntryId entry, final ContentId content) {
			return content.getURI();
		}
	};
}
