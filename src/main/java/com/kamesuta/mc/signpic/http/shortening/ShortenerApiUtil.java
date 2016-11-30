package com.kamesuta.mc.signpic.http.shortening;

import java.io.IOException;

import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Apis.URLShortenerFactory;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;

import net.minecraft.client.resources.I18n;

public class ShortenerApiUtil {
	public static boolean shortening(final ShorteningRequest content, final Runnable onDone) {
		try {
			final URLShortenerFactory factory = getShortenerFactory();
			final String key = getKey(factory);
			if (factory!=null&&key!=null) {
				content.getPendingState().getMeta().put(GuiTask.HighlightPanel, true);
				final IShortener upload = factory.create(content, key);
				upload.setCallback(new ICommunicateCallback() {
					@Override
					public void onDone(final ICommunicateResponse res) {
						if (upload.getShortLink()!=null) {
							final String url = upload.getShortLink();
							if (!GuiMain.setContentId(url))
								Client.notice(I18n.format("signpic.gui.notice.shortened", content.getName()), 2);
							if (onDone!=null)
								onDone.run();
						}
						if (!res.isSuccess())
							Client.notice(I18n.format("signpic.gui.notice.shorteningfailed", res.getError()), 2);
					}
				});
				Communicator.instance.communicate(upload);
			}
			return true;
		} catch (final IOException e) {
			Client.notice(I18n.format("signpic.gui.notice.shorteningfailed", e), 2);
		}
		return false;
	}

	public static boolean shortening(final ShorteningRequest content) {
		return shortening(content, null);
	}

	public static URLShortenerFactory getShortenerFactory() {
		return Apis.instance.urlShorteners.solve(Apis.instance.urlShorteners.getConfigOrRandom());
	}

	public static String getKey(final URLShortenerFactory factory) {
		if (factory!=null)
			return new Apis.KeySetting(factory.keys()).getConfigOrRandom();
		return null;
	}
}
