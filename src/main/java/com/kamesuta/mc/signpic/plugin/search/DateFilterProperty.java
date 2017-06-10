package com.kamesuta.mc.signpic.plugin.search;

import java.util.Date;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public enum DateFilterProperty implements DataFilterProperty<Date> {
	CREATE {
		@Override
		public @Nullable Date get(final SignData data, final EntryId entry, final ContentId id) {
			return data.getCreateDate();
		}
	},
	UPDATE {
		@Override
		public @Nullable Date get(final SignData data, final EntryId entry, final ContentId id) {
			return data.getUpdateDate();
		}
	};
}
