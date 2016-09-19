package com.kamesuta.mc.signpic.entry.content;

import net.minecraft.client.resources.I18n;

public class ContentState {
	public Progress progress = new Progress();
	private ContentStateType type = ContentStateType.INIT;
	private String message = "";

	public ContentState setMessage(final String message) {
		this.message = message;
		return this;
	}

	public String getMessage() {
		return this.message;
	}

	public ContentState setType(final ContentStateType type) {
		this.type = type;
		return this;
	}

	public ContentStateType getType() {
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
