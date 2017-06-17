package com.kamesuta.mc.signpic.entry.content;

import java.io.IOException;

import javax.annotation.Nonnull;

public class ContentCapacityOverException extends IOException {
	public ContentCapacityOverException() {
	}

	public ContentCapacityOverException(final @Nonnull String message) {
		super(message);
	}

	public ContentCapacityOverException(final @Nonnull Throwable cause) {
		super(cause);
	}

	public ContentCapacityOverException(final @Nonnull String message, final @Nonnull Throwable cause) {
		super(message, cause);
	}
}
