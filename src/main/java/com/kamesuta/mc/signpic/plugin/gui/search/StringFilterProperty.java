package com.kamesuta.mc.signpic.plugin.gui.search;

import com.kamesuta.mc.signpic.plugin.SignData;

public enum StringFilterProperty implements IFilterProperty<String> {
	ID {
		@Override
		public String get(SignData data) {
			return String.valueOf(data.getId());
		}
	},
	SIGN {
		@Override
		public String get(SignData data) {
			return data.getSign();
		}
	},
	PLAYERNAME {
		@Override
		public String get(SignData data) {
			return data.getPlayerName();
		}
	},
	PLAYERUUID {
		@Override
		public String get(SignData data) {
			return data.getPlayerUUID();
		}
	},
	WORLDNAME {
		@Override
		public String get(SignData data) {
			return data.getWorldName();
		}
	};
}
