package com.kamesuta.mc.signpic.entry.content;

public class ContentBlockedException extends Exception {
	public ContentBlockedException() {
	}

	public ContentBlockedException(final String message) {
		super(message);
	}

	public ContentBlockedException(final Throwable cause) {
		super(cause);
	}

	public ContentBlockedException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
