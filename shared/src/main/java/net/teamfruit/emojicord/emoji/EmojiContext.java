package net.teamfruit.emojicord.emoji;

import static net.teamfruit.emojicord.emoji.EmojiText.ParseFlag.*;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import net.teamfruit.emojicord.emoji.EmojiText.EmojiTextElement;
import net.teamfruit.emojicord.emoji.EmojiText.ParseFlag;

public class EmojiContext {
	public static final char EMOJI_REPLACE_CHARACTOR = '\u0000';

	public final String text;
	public final Map<Integer, EmojiTextElement> emojis;

	public EmojiContext(final String text, final Map<Integer, EmojiTextElement> emojis) {
		this.text = text;
		this.emojis = emojis;
	}

	public static enum EmojiContextAttribute {
		CHAT_INPUT,
		CHAT_MESSAGE,
	}

	public static class EmojiContextLoader {
		public static EmojiContext getEmojiFormattedString(final String text, final EnumSet<EmojiContextAttribute> attributes) {
			if (!StringUtils.isEmpty(text)) {
				final EnumSet<ParseFlag> flags = EnumSet.of(ESCAPE, ENCODE, ENCODE_ALIAS, PARSE);
				if (attributes.contains(EmojiContextAttribute.CHAT_MESSAGE)||attributes.contains(EmojiContextAttribute.CHAT_INPUT))
					flags.add(ENCODE_UTF);
				final EmojiText emojiText = EmojiText.create(text, flags);
				final EmojiContext context = emojiText.getEmojiContext();
				return context;
			}
			return new EmojiContext("", Maps.newHashMap());
		}
	}

	public static class EmojiContextCache {
		public static final long LIFETIME_SEC = 5;

		public static final EmojiContextCache instance = new EmojiContextCache();

		private EmojiContextCache() {
		}

		private final LoadingCache<Pair<String, EnumSet<EmojiContextAttribute>>, EmojiContext> EMOJI_TEXT_MAP = CacheBuilder.newBuilder()
				.expireAfterAccess(LIFETIME_SEC, TimeUnit.SECONDS)
				.build(new CacheLoader<Pair<String, EnumSet<EmojiContextAttribute>>, EmojiContext>() {
					@Override
					public EmojiContext load(final Pair<String, EnumSet<EmojiContextAttribute>> entry) throws Exception {
						return EmojiContextLoader.getEmojiFormattedString(entry.getLeft(), entry.getRight());
					}
				});

		public @Nonnull EmojiContext getContext(final String text, final EnumSet<EmojiContextAttribute> attributes) {
			return this.EMOJI_TEXT_MAP.getUnchecked(Pair.of(text, attributes));
		}
	}
}
