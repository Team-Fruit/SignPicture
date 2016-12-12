package com.kamesuta.mc.signpic.entry.content;

public class RetryCountOverException extends Exception {
	public RetryCountOverException() {
	}

	public RetryCountOverException(final String message) {
		super(message);
	}

	public RetryCountOverException(final Throwable cause) {
		super(cause);
	}

	public RetryCountOverException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
