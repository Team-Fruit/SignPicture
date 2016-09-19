package com.kamesuta.mc.signpic.entry;

import net.minecraft.client.resources.I18n;

public class EntryState {
	public Progress progress = new Progress();
	private EntryStateType type = EntryStateType.INIT;
	private String message = "";

	public EntryState setMessage(final String message) {
		this.message = message;
		return this;
	}

	public String getMessage() {
		return this.message;
	}

	public EntryState setType(final EntryStateType type) {
		this.type = type;
		return this;
	}

	public EntryStateType getType() {
		return this.type;
	}

	public String getStateMessage() {
		return I18n.format(this.type.msg, (int) (this.progress.getProgress()*100));
	}

	public static class Progress {
		public long overall;
		public long done;

		float per() {
			return (float) this.done / (float) this.overall;
		}

		public float getProgress() {
			if (this.overall > 0)
				return Math.max(0, Math.min(1, per()));
			return 0;
		}
	}
}
