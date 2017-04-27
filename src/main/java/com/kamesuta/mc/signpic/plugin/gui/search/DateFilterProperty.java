package com.kamesuta.mc.signpic.plugin.gui.search;

import java.util.Date;

import com.kamesuta.mc.signpic.plugin.SignData;

public enum DateFilterProperty implements IFilterProperty<Date> {
	CREATE {
		@Override
		public Date get(SignData data) {
			return data.getCreateDate();
		}
	},
	UPDATE {
		@Override
		public Date get(SignData data) {
			return data.getUpdateDate();
		}
	};
}
