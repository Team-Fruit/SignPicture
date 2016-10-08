package com.kamesuta.mc.signpic.state;

import net.minecraft.client.resources.I18n;

public class ContentState implements Progressable {
	private final Progress progress = new Progress();
	private ContentStateType type = ContentStateType.INIT;
	private String message = "";

	@Override
	public Progress getProgress() {
		return this.progress;
	}

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
}
