package com.kamesuta.mc.signpic.http.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.kamesuta.mc.signpic.state.State;

public abstract class UploadRequest {
	protected final State state;

	public UploadRequest(final State state) {
		this.state = state;
	}

	public State getState(final String format) {
		final State state = getPendingState();
		state.setName(String.format(format, getName()));
		state.getProgress().setOverall(getSize());
		return state;
	}

	public State getPendingState() {
		return this.state;
	}

	public abstract String getName();

	public abstract long getSize();

	public abstract InputStream getStream() throws IOException;

	public static UploadRequest fromStream(final String name, final InputStream stream, final long size, final State state) {
		return new StreamUploadContent(name, stream, size, state);
	}

	public static UploadRequest fromFile(final File file, final State state) {
		return new FileUploadContent(file, state);
	}

	private static class StreamUploadContent extends UploadRequest {
		private final String name;
		private final InputStream stream;
		private final long size;

		public StreamUploadContent(final String name, final InputStream stream, final long size, final State state) {
			super(state);
			this.name = name;
			this.stream = stream;
			this.size = size;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public long getSize() {
			return this.size;
		}

		@Override
		public InputStream getStream() {
			return this.stream;
		}
	}

	private static class FileUploadContent extends UploadRequest {
		private final File file;

		public FileUploadContent(final File file, final State state) {
			super(state);
			this.file = file;
		}

		@Override
		public String getName() {
			return this.file.getName();
		}

		@Override
		public long getSize() {
			return this.file.length();
		}

		@Override
		public InputStream getStream() throws FileNotFoundException {
			return new FileInputStream(this.file);
		}
	}
}
