package com.kamesuta.mc.signpic;

import java.io.IOException;

import javax.annotation.Nonnull;

public class LoadCanceledException extends IOException {
	public LoadCanceledException() {
	}

	public LoadCanceledException(final @Nonnull String message) {
		super(message);
	}

	public LoadCanceledException(final @Nonnull Throwable cause) {
		super(cause);
	}

	public LoadCanceledException(final @Nonnull String message, final @Nonnull Throwable cause) {
		super(message, cause);
	}
}
