package com.kamesuta.mc.signpic.entry.content;

import javax.annotation.Nonnull;

public class RetryCountOverException extends Exception {
	public RetryCountOverException() {
	}

	public RetryCountOverException(final @Nonnull String message) {
		super(message);
	}

	public RetryCountOverException(final @Nonnull Throwable cause) {
		super(cause);
	}

	public RetryCountOverException(final @Nonnull String message, final @Nonnull Throwable cause) {
		super(message, cause);
	}
}
