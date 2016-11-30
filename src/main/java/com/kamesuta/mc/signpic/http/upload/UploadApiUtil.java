package com.kamesuta.mc.signpic.http.upload;

import java.io.IOException;

import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Apis.ImageUploaderFactory;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;

import net.minecraft.client.resources.I18n;

public class UploadApiUtil {
	public static boolean upload(final UploadRequest content, final Runnable onDone) {
		try {
			final ImageUploaderFactory factory = getUploaderFactory();
			final String key = getKey(factory);
			if (factory!=null&&key!=null) {
				content.getPendingState().getMeta().put(GuiTask.HighlightPanel, true);
				content.getPendingState().getMeta().put(GuiTask.ShowPanel, 3);
				final IUploader upload = factory.create(content, key);
				upload.setCallback(new ICommunicateCallback() {
					@Override
					public void onDone(final ICommunicateResponse res) {
						if (upload.getLink()!=null) {
							final String url = upload.getLink();
							if (!GuiMain.setContentId(url))
								Client.notice(I18n.format("signpic.gui.notice.uploaded", content.getName()), 2);
							if (onDone!=null)
								onDone.run();
						}
						if (!res.isSuccess())
							Client.notice(I18n.format("signpic.gui.notice.uploadfailed", res.getError()), 2);
					}
				});
				Communicator.instance.communicate(upload);
			}
			return true;
		} catch (final IOException e) {
			Client.notice(I18n.format("signpic.gui.notice.uploadfailed", e), 2);
		}
		return false;
	}

	public static boolean upload(final UploadRequest content) {
		return upload(content, null);
	}

	public static ImageUploaderFactory getUploaderFactory() {
		return Apis.instance.imageUploaders.solve(Apis.instance.imageUploaders.getConfigOrRandom());
	}

	public static String getKey(final ImageUploaderFactory factory) {
		if (factory!=null)
			return new Apis.KeySetting(factory.keys()).getConfigOrRandom();
		return null;
	}
}
