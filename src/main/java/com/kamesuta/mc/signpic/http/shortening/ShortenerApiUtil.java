package com.kamesuta.mc.signpic.http.shortening;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Apis.URLShortenerFactory;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.state.State;

import net.minecraft.client.resources.I18n;

public class ShortenerApiUtil {
	public static boolean shortening(final @Nonnull ShorteningRequest content, final @Nullable Runnable onDone) {
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
							if (url!=null)
								GuiMain.setContentId(url);
							if (onDone!=null)
								onDone.run();
						}
						if (!res.isSuccess())
							Log.notice(I18n.format("signpic.gui.notice.shorteningfailed", res.getError()));
					}
				});
				Communicator.instance.communicate(upload);
			}
			return true;
		} catch (final IOException e) {
			Log.notice(I18n.format("signpic.gui.notice.shorteningfailed", e));
		}
		return false;
	}

	public static boolean shortening(final @Nonnull ShorteningRequest content) {
		return shortening(content, null);
	}

	public static @Nullable URLShortenerFactory getShortenerFactory() {
		return Apis.instance.urlShorteners.solve(Apis.instance.urlShorteners.getConfigOrRandom());
	}

	public static @Nullable String getKey(final @Nullable URLShortenerFactory factory) {
		if (factory!=null)
			return factory.keySettings().getConfigOrRandom();
		return null;
	}

	public static void requestShoretning(final @Nonnull ContentId id) {
		if (!CurrentMode.instance.isShortening()) {
			final String longurl = id.getURI();
			CurrentMode.instance.setShortening(true);
			ShortenerApiUtil.shortening(new ShorteningRequest(longurl, longurl, new State()), new Runnable() {
				@Override
				public void run() {
					CurrentMode.instance.setShortening(false);
				}
			});
		} else
			Log.notice(I18n.format("signpic.gui.notice.shorteningother"));
	}
}
