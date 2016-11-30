package com.kamesuta.mc.signpic.http;

import java.io.IOException;

public class CommunicateCanceledException extends IOException {
	public CommunicateCanceledException() {
	}

	public CommunicateCanceledException(final String message) {
		super(message);
	}

	public CommunicateCanceledException(final Throwable cause) {
		super(cause);
	}

	public CommunicateCanceledException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
