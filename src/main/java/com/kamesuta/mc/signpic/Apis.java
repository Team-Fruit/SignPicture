package com.kamesuta.mc.signpic;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kamesuta.mc.signpic.http.shortening.BitlyShortener;
import com.kamesuta.mc.signpic.http.shortening.GooglShortener;
import com.kamesuta.mc.signpic.http.shortening.IShortener;
import com.kamesuta.mc.signpic.http.shortening.ShorteningRequest;
import com.kamesuta.mc.signpic.http.upload.GyazoUpload;
import com.kamesuta.mc.signpic.http.upload.IUploader;
import com.kamesuta.mc.signpic.http.upload.ImgurUpload;
import com.kamesuta.mc.signpic.http.upload.UploadRequest;
import com.kamesuta.mc.signpic.information.Info;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.information.Informations.InfoSource;

public class Apis {
	public static final @Nonnull Apis instance = new Apis();

	private Apis() {
	}

	public static interface URLReplacer {
		@Nonnull
		String replace(@Nonnull String src);
	}

	public final @Nonnull Set<URLReplacer> urlReplacers = Sets.newHashSet();

	public void registerURLReplacer(final @Nonnull URLReplacer replacer) {
		this.urlReplacers.add(replacer);
	}

	public @Nonnull String replaceURL(final @Nonnull String src) {
		String url = src;
		for (final URLReplacer replacer : this.urlReplacers)
			url = replacer.replace(url);
		return url;
	}

	private static final @Nonnull Random rnd = new Random();

	public final @Nonnull MapSetting<ImageUploaderFactory> imageUploaders = new MapSetting<ImageUploaderFactory>() {
		@Override
		public @Nonnull String getConfig() {
			return Config.getConfig().apiUploaderType.get();
		}

		@Override
		public void setConfig(final String setting) {
			Config.getConfig().apiUploaderType.set(setting);
			Config.getConfig().apiUploaderKey.set("");
		};
	};

	public final @Nonnull MapSetting<URLShortenerFactory> urlShorteners = new MapSetting<URLShortenerFactory>() {
		@Override
		public @Nonnull String getConfig() {
			return Config.getConfig().apiShortenerType.get();
		}

		@Override
		public void setConfig(final @Nonnull String setting) {
			Config.getConfig().apiShortenerType.set(setting);
			Config.getConfig().apiShortenerKey.set("");
		};
	};

	public void registerImageUploader(final @Nonnull String name, final @Nonnull ImageUploaderFactory uploader) {
		this.imageUploaders.registerSetting(name, uploader);
	}

	public void registerURLShortener(final @Nonnull String name, final @Nonnull URLShortenerFactory shortener) {
		this.urlShorteners.registerSetting(name, shortener);
	}

	public static abstract class KeySetting extends Setting {
		public KeySetting(final @Nonnull Set<String> keys) {
			for (final String key : keys)
				if (key!=null)
					registerSetting(key);
		}
	}

	public static class UploaderKeySetting extends KeySetting {
		public UploaderKeySetting(final @Nonnull Set<String> keys) {
			super(keys);
		}

		@Override
		public @Nonnull String getConfig() {
			return Config.getConfig().apiUploaderKey.get();
		}

		@Override
		public void setConfig(final @Nonnull String setting) {
			Config.getConfig().apiUploaderKey.set(setting);
		}
	}

	public static class ShorteningKeySetting extends KeySetting {
		public ShorteningKeySetting(final @Nonnull Set<String> keys) {
			super(keys);
		}

		@Override
		public @Nonnull String getConfig() {
			return Config.getConfig().apiShortenerKey.get();
		}

		@Override
		public void setConfig(final @Nonnull String setting) {
			Config.getConfig().apiShortenerKey.set(setting);
		}
	}

	public static abstract class Setting {
		private final @Nonnull Set<String> settings = Sets.newHashSet();

		public void registerSetting(final @Nonnull String name) {
			getSettings().add(name);
		}

		public @Nonnull Set<String> getSettings() {
			return this.settings;
		}

		public @Nullable String getRandom() {
			final int size = getSettings().size();
			if (size>0) {
				final int item = rnd.nextInt(size);
				int i = 0;
				for (final String k : getSettings()) {
					if (i==item)
						return k;
					i = i+1;
				}
			}
			return null;
		}

		public @Nullable String getConfigOrRandom() {
			String cfg = getConfig();
			if (StringUtils.isEmpty(cfg))
				cfg = getRandom();
			return cfg;
		}

		public abstract @Nonnull String getConfig();

		public abstract void setConfig(@Nonnull String setting);
	}

	public static abstract class MapSetting<E> extends Setting {
		private final @Nonnull Map<String, E> settingmap = Maps.newHashMap();

		public void registerSetting(final @Nonnull String name, final @Nonnull E uploader) {
			registerSetting(name);
			this.getSettingMap().put(name, uploader);
		}

		public @Nonnull Map<String, E> getSettingMap() {
			return this.settingmap;
		}

		public @Nullable E solve(final @Nullable String name) {
			return getSettingMap().get(name);
		}
	}

	public static interface ApiFactory {
		@Nonnull
		Set<String> keys();

		@Nonnull
		KeySetting keySettings();
	}

	public static interface ImageUploaderFactory extends ApiFactory {
		IUploader create(@Nonnull UploadRequest upload, @Nonnull String key) throws IOException;
	}

	public static interface URLShortenerFactory extends ApiFactory {
		IShortener create(@Nonnull ShorteningRequest upload, @Nonnull String key) throws IOException;
	}

	public void init() {
		registerImageUploader("Gyazo", new ImageUploaderFactory() {
			@Override
			public @Nonnull Set<String> keys() {
				final Set<String> keys = Sets.newHashSet();
				final InfoSource source = Informations.instance.getSource();
				if (source!=null) {
					final Info.Api api = source.info.apis;
					if (api!=null) {
						final Info.Api.Image image = api.image;
						if (image!=null) {
							final Info.Api.Image.Gyazo gyazo = image.gyazo;
							if (gyazo!=null) {
								final List<Info.Api.Image.Gyazo.Config> configs = gyazo.config;
								if (configs!=null)
									for (final Info.Api.Image.Gyazo.Config config : configs)
										if (config!=null)
											keys.add(config.key);
							}
						}
					}
				}
				return keys;
			}

			@Override
			public @Nonnull IUploader create(final UploadRequest upload, final String key) throws IOException {
				return new GyazoUpload(upload, key);
			}

			@Override
			public @Nonnull KeySetting keySettings() {
				return new UploaderKeySetting(keys());
			}
		});
		registerImageUploader("Imgur", new ImageUploaderFactory() {
			@Override
			public @Nonnull Set<String> keys() {
				final Set<String> keys = Sets.newHashSet();
				final InfoSource source = Informations.instance.getSource();
				if (source!=null) {
					final Info.Api api = source.info.apis;
					if (api!=null) {
						final Info.Api.Image image = api.image;
						if (image!=null) {
							final Info.Api.Image.Imgur imgur = image.imgur;
							if (imgur!=null) {
								final List<Info.Api.Image.Imgur.Config> configs = imgur.config;
								if (configs!=null)
									for (final Info.Api.Image.Imgur.Config config : configs)
										if (config!=null)
											keys.add(config.clientid);
							}
						}
					}
				}
				return keys;
			}

			@Override
			public @Nonnull IUploader create(final @Nonnull UploadRequest upload, final @Nonnull String key) throws IOException {
				return new ImgurUpload(upload, key);
			}

			@Override
			public @Nonnull KeySetting keySettings() {
				return new UploaderKeySetting(keys());
			}
		});
		registerURLShortener("Bitly", new URLShortenerFactory() {
			@Override
			public @Nonnull Set<String> keys() {
				final Set<String> keys = Sets.newHashSet();
				final InfoSource source = Informations.instance.getSource();
				if (source!=null) {
					final Info.Api api = source.info.apis;
					if (api!=null) {
						final Info.Api.Shortener shortener = api.shortener;
						if (shortener!=null) {
							final Info.Api.Shortener.Bitly bitly = shortener.bitly;
							if (bitly!=null) {
								final List<Info.Api.Shortener.Bitly.Config> configs = bitly.config;
								if (configs!=null)
									for (final Info.Api.Shortener.Bitly.Config config : configs)
										if (config!=null)
											keys.add(config.key);
							}
						}
					}
				}
				return keys;
			}

			@Override
			public @Nonnull IShortener create(final @Nonnull ShorteningRequest upload, final @Nonnull String key) throws IOException {
				return new BitlyShortener(upload, key);
			}

			@Override
			public @Nonnull KeySetting keySettings() {
				return new ShorteningKeySetting(keys());
			}
		});
		registerURLShortener("Googl", new URLShortenerFactory() {
			@Override
			public @Nonnull Set<String> keys() {
				final Set<String> keys = Sets.newHashSet();
				final InfoSource source = Informations.instance.getSource();
				if (source!=null) {
					final Info.Api api = source.info.apis;
					if (api!=null) {
						final Info.Api.Shortener shortener = api.shortener;
						if (shortener!=null) {
							final Info.Api.Shortener.Googl googl = shortener.googl;
							if (googl!=null) {
								final List<Info.Api.Shortener.Googl.Config> configs = googl.config;
								if (configs!=null)
									for (final Info.Api.Shortener.Googl.Config config : configs)
										if (config!=null)
											keys.add(config.key);
							}
						}
					}
				}
				return keys;
			}

			@Override
			public @Nonnull IShortener create(final @Nonnull ShorteningRequest upload, final @Nonnull String key) throws IOException {
				return new GooglShortener(upload, key);
			}

			@Override
			public @Nonnull KeySetting keySettings() {
				return new ShorteningKeySetting(keys());
			}
		});
		final Pattern p = Pattern.compile("[^\\w]");
		registerURLReplacer(new URLReplacer() {
			@Override
			public @Nonnull String replace(@Nonnull String src) {
				if (StringUtils.containsIgnoreCase(src, "gyazo.com")) {
					if (!StringUtils.containsIgnoreCase(src, "i.gyazo.com"))
						src = StringUtils.replace(src, "gyazo.com", "i.gyazo.com");
					final String path = StringUtils.substringAfter(src, "gyazo.com/");
					final Matcher m = p.matcher(path);
					if (m.find()) {
						final String querystring = StringUtils.substring(path, 0, m.start());
						final int i = StringUtils.indexOf(path, ".");
						if (i<0||i>StringUtils.length(querystring)) {
							final String pre = StringUtils.substringBefore(src, "gyazo.com/");
							src = pre+"gyazo.com/"+querystring+".png";
						}
					} else
						src += ".png";
				}
				return src;
			}
		});
		registerURLReplacer(new URLReplacer() {
			@Override
			public @Nonnull String replace(@Nonnull String src) {
				if (StringUtils.containsIgnoreCase(src, "imgur.com")) {
					if (!StringUtils.containsIgnoreCase(src, "i.imgur.com"))
						src = StringUtils.replace(src, "imgur.com", "i.imgur.com");
					final String path = StringUtils.substringAfter(src, "imgur.com/");
					final Matcher m = p.matcher(path);
					if (m.find()) {
						final String querystring = StringUtils.substring(path, 0, m.start());
						final int i = StringUtils.indexOf(path, ".");
						if (i<0||i>StringUtils.length(querystring)) {
							final String pre = StringUtils.substringBefore(src, "imgur.com/");
							src = pre+"imgur.com/"+querystring+".png";
						}
					} else
						src += ".png";
				}
				return src;
			}
		});
	}
}
