package net.teamfruit.signpic.http.upload;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.teamfruit.signpic.Apis;
import net.teamfruit.signpic.Log;
import net.teamfruit.signpic.Apis.ImageUploaderFactory;
import net.teamfruit.signpic.gui.GuiMain;
import net.teamfruit.signpic.gui.GuiTask;
import net.teamfruit.signpic.http.Communicator;
import net.teamfruit.signpic.http.ICommunicateCallback;
import net.teamfruit.signpic.http.ICommunicateResponse;

public class UploadApiUtil {
	public static boolean upload(final @Nonnull UploadRequest content, final @Nullable UploadCallback onDone) {
		try {
			final ImageUploaderFactory factory = getUploaderFactory();
			final String key = getKey(factory);
			if (factory!=null&&key!=null) {
				content.getPendingState().getMeta().put(GuiTask.HighlightPanel, true);
				content.getPendingState().getMeta().put(GuiTask.ShowPanel, 3);
				final IUploader upload = factory.create(content, key);
				upload.setCallback(new ICommunicateCallback() {
					@Override
					public void onDone(final @Nonnull ICommunicateResponse res) {
						final String url = upload.getLink();
						if (url!=null) {
							if (!GuiMain.setContentId(url))
								Log.notice(I18n.format("signpic.gui.notice.uploaded", content.getName()));
							if (onDone!=null)
								onDone.onDone(url);
						}
						if (!res.isSuccess())
							Log.notice(I18n.format("signpic.gui.notice.uploadfailed", res.getError()));
					}
				});
				Communicator.instance.communicate(upload);
			}
			return true;
		} catch (final IOException e) {
			Log.notice(I18n.format("signpic.gui.notice.uploadfailed", e));
		}
		return false;
	}

	public static @Nullable ImageUploaderFactory getUploaderFactory() {
		return Apis.instance.imageUploaders.solve(Apis.instance.imageUploaders.getConfigOrRandom());
	}

	public static @Nullable String getKey(final @Nullable ImageUploaderFactory factory) {
		if (factory!=null)
			return factory.keySettings().getConfigOrRandom();
		return null;
	}
}
