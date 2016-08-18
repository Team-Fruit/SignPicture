package com.kamesuta.mc.signpic.image.exception;

import java.io.IOException;

public class InvaildImageException extends IOException {
	public InvaildImageException() {
	}

	public InvaildImageException(final String message) {
		super(message);
	}

	public InvaildImageException(final Throwable cause) {
		super(cause);
	}

	public InvaildImageException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
