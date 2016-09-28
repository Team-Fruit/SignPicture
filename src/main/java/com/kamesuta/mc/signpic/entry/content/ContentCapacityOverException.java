package com.kamesuta.mc.signpic.entry.content;

import java.io.IOException;

public class ContentCapacityOverException extends IOException {
	public ContentCapacityOverException() {
	}

	public ContentCapacityOverException(final String message) {
		super(message);
	}

	public ContentCapacityOverException(final Throwable cause) {
		super(cause);
	}

	public ContentCapacityOverException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
