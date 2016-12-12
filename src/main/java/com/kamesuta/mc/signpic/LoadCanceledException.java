package com.kamesuta.mc.signpic;

import java.io.IOException;

public class LoadCanceledException extends IOException {
	public LoadCanceledException() {
	}

	public LoadCanceledException(final String message) {
		super(message);
	}

	public LoadCanceledException(final Throwable cause) {
		super(cause);
	}

	public LoadCanceledException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
