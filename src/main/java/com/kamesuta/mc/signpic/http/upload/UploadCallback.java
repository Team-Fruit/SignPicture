package com.kamesuta.mc.signpic.http.upload;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;

public interface UploadCallback {
	public static final @Nonnull UploadCallback copyOnDone = new UploadCallback() {
		@Override
		public void onDone(final String str) {
			GuiScreen.setClipboardString(str);
		}
	};

	public static final @Nullable UploadCallback nothingOnDone = null;

	void onDone(@Nonnull String str);
}
