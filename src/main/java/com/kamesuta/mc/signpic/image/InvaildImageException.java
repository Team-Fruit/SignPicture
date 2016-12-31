package com.kamesuta.mc.signpic.image;

import java.io.IOException;

import javax.annotation.Nonnull;

public class InvaildImageException extends IOException {
	public InvaildImageException() {
	}

	public InvaildImageException(final @Nonnull String message) {
		super(message);
	}

	public InvaildImageException(final @Nonnull Throwable cause) {
		super(cause);
	}

	public InvaildImageException(final @Nonnull String message, final @Nonnull Throwable cause) {
		super(message, cause);
	}
}
