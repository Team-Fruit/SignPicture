package com.kamesuta.mc.signpic.plugin.search;

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
	PLAYER {
		@Override
		public String get(SignData data) {
			return data.getPlayerName();
		}
	},
	UUID {
		@Override
		public String get(SignData data) {
			return data.getPlayerUUID();
		}
	},
	WORLD {
		@Override
		public String get(SignData data) {
			return data.getWorldName();
		}
	};
}
