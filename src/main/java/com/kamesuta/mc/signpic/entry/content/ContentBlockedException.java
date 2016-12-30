package com.kamesuta.mc.signpic.entry.content;

import javax.annotation.Nonnull;

public class ContentBlockedException extends Exception {
	public ContentBlockedException() {
	}

	public ContentBlockedException(final @Nonnull String message) {
		super(message);
	}

	public ContentBlockedException(final @Nonnull Throwable cause) {
		super(cause);
	}

	public ContentBlockedException(final @Nonnull String message, final @Nonnull Throwable cause) {
		super(message, cause);
	}
}
