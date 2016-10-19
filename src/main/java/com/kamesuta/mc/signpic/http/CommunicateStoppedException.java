package com.kamesuta.mc.signpic.http;

import java.io.IOException;

public class CommunicateStoppedException extends IOException {
	public CommunicateStoppedException() {
	}

	public CommunicateStoppedException(final String message) {
		super(message);
	}

	public CommunicateStoppedException(final Throwable cause) {
		super(cause);
	}

	public CommunicateStoppedException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
