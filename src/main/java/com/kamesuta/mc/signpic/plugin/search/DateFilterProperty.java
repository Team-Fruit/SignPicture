package com.kamesuta.mc.signpic.plugin.search;

import java.util.Date;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.plugin.SignData;

public enum DateFilterProperty implements IFilterProperty<Date> {
	CREATE {
		@Override
		public @Nullable Date get(final SignData data) {
			return data.getCreateDate();
		}
	},
	UPDATE {
		@Override
		public @Nullable Date get(final SignData data) {
			return data.getUpdateDate();
		}
	};
}
