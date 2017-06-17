package com.kamesuta.mc.signpic.http.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import com.kamesuta.mc.signpic.state.State;

public abstract class UploadRequest {
	protected final @Nonnull State state;

	public UploadRequest(final @Nonnull State state) {
		this.state = state;
	}

	public State getState(final @Nonnull String format) {
		final State state = getPendingState();
		state.setName(String.format(format, getName()));
		state.getProgress().setOverall(getSize());
		return state;
	}

	public @Nonnull State getPendingState() {
		return this.state;
	}

	public abstract @Nonnull String getName();

	public abstract long getSize();

	public abstract @Nonnull InputStream getStream() throws IOException;

	public static @Nonnull UploadRequest fromStream(final @Nonnull String name, final @Nonnull InputStream stream, final long size, final @Nonnull State state) {
		return new StreamUploadContent(name, stream, size, state);
	}

	public static @Nonnull UploadRequest fromFile(final @Nonnull File file, final @Nonnull State state) {
		return new FileUploadContent(file, state);
	}

	private static class StreamUploadContent extends UploadRequest {
		private final @Nonnull String name;
		private final @Nonnull InputStream stream;
		private final long size;

		public StreamUploadContent(final @Nonnull String name, final @Nonnull InputStream stream, final long size, final @Nonnull State state) {
			super(state);
			this.name = name;
			this.stream = stream;
			this.size = size;
		}

		@Override
		public @Nonnull String getName() {
			return this.name;
		}

		@Override
		public long getSize() {
			return this.size;
		}

		@Override
		public @Nonnull InputStream getStream() {
			return this.stream;
		}
	}

	private static class FileUploadContent extends UploadRequest {
		private final @Nonnull File file;

		public FileUploadContent(final @Nonnull File file, final @Nonnull State state) {
			super(state);
			this.file = file;
		}

		@Override
		public @Nonnull String getName() {
			return this.file.getName();
		}

		@Override
		public long getSize() {
			return this.file.length();
		}

		@Override
		public @Nonnull InputStream getStream() throws FileNotFoundException {
			return new FileInputStream(this.file);
		}
	}
}
