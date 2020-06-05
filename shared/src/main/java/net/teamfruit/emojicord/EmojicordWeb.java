package net.teamfruit.emojicord;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import net.teamfruit.emojicord.emoji.DiscordEmojiIdDictionary;
import net.teamfruit.emojicord.emoji.Endpoint;
import net.teamfruit.emojicord.emoji.Models.EmojiDiscordList;
import net.teamfruit.emojicord.emoji.Models.EmojiGateway;
import net.teamfruit.emojicord.util.DataUtils;

public class EmojicordWeb {
	public static final @Nonnull EmojicordWeb instance = new EmojicordWeb();

	private int port = 0;
	private CallbackServerInstance server;
	private final String key = UUID.randomUUID().toString();
	private final AtomicBoolean callbacked = new AtomicBoolean();

	public static class EmojicordWebTokenModel {
		public String token;
	}

	public boolean open() {
		if (this.server==null)
			if (this.port!=0)
				try {
					this.server = new CallbackServerInstance(this::callback, this::checkKey, this.port);
					this.port = this.server.getPort();
				} catch (final IOException e) {
					Log.log.error("Could not open the callback server with port"+this.port, e);
				}
		if (this.server==null)
			try {
				this.server = new CallbackServerInstance(this::callback, this::checkKey);
				this.port = this.server.getPort();
			} catch (final IOException e) {
				Log.log.error("Could not open the callback server with port"+this.port, e);
			}
		if (this.server!=null) {
			pollCallbacked();
			final EmojiGateway.EmojiGatewayApi api = Endpoint.EMOJI_API.api;
			if (api!=null)
				OSUtils.getOSType().openURI(String.format("%s?key=%s&port=%s", api.importings, this.key, this.port));
			return true;
		} else {
			Log.log.warn("Failed to Initialize Web");
			return false;
		}
	}

	public void close() {
		if (this.server!=null)
			this.server.close();
		this.server = null;
	}

	private boolean checkKey(final String key) {
		return StringUtils.equals(this.key, key);
	}

	private void callback(final EmojiDiscordList model) {
		final File dictDir = DiscordEmojiIdDictionary.instance.getDictionaryDirectory();
		final File file = new File(dictDir, String.format("%s.json", model.id));
		DataUtils.saveFile(file, EmojiDiscordList.class, model, "Emoji Data Save");
		this.callbacked.set(true);
	}

	public boolean pollCallbacked() {
		return this.callbacked.getAndSet(false);
	}
}
