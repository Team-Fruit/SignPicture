package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kamesuta.mc.signpic.http.upload.GyazoUpload;
import com.kamesuta.mc.signpic.http.upload.IUploader;
import com.kamesuta.mc.signpic.http.upload.ImgurUpload;
import com.kamesuta.mc.signpic.information.Info;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.state.State;

public class Apis {
	public static final Apis instance = new Apis();

	private Apis() {
	}

	public static interface URLReplacer {
		String replace(String src);
	}

	public final Set<URLReplacer> urlReplacers = Sets.newHashSet();

	public void registerURLReplacer(final URLReplacer replacer) {
		this.urlReplacers.add(replacer);
	}

	public String replaceURL(final String src) {
		String url = src;
		for (final URLReplacer replacer : this.urlReplacers)
			url = replacer.replace(url);
		return url;
	}

	private static final Random rnd = new Random();

	public final MapSetting<ImageUploaderFactory> imageUploaders = new MapSetting<Apis.ImageUploaderFactory>() {
		@Override
		public String getConfig() {
			return Config.instance.apiType;
		}

		@Override
		public void setConfig(final String setting) {
			Config.instance.get("Api.Upload", "Type", "").set(setting);
			Config.instance.get("Api.Upload", "Key", "").set("");
			Config.instance.save();
		};
	};

	public void registerImageUploader(final String name, final ImageUploaderFactory uploader) {
		this.imageUploaders.registerSetting(name, uploader);
	}

	public static class KeySetting extends Setting {
		public KeySetting() {
		}

		public KeySetting(final Set<String> keys) {
			if (keys!=null)
				for (final String key : keys)
					registerSetting(key);
		}

		@Override
		public String getConfig() {
			return Config.instance.apiKey;
		}

		@Override
		public void setConfig(final String setting) {
			Config.instance.get("Api.Upload", "Key", "").set(setting);
			Config.instance.save();
		};
	}

	public static abstract class Setting {
		private final Set<String> settings = Sets.newHashSet();

		public void registerSetting(final String name) {
			getSettings().add(name);
		}

		public Set<String> getSettings() {
			return this.settings;
		}

		public String getRandom() {
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

		public String getConfigOrRandom() {
			String cfg = getConfig();
			if (StringUtils.isEmpty(cfg))
				cfg = getRandom();
			return cfg;
		}

		public abstract String getConfig();

		public abstract void setConfig(String setting);
	}

	public static abstract class MapSetting<E> extends Setting {
		private final Map<String, E> settingmap = Maps.newHashMap();

		public void registerSetting(final String name, final E uploader) {
			registerSetting(name);
			this.getSettingMap().put(name, uploader);
		}

		public Map<String, E> getSettingMap() {
			return this.settingmap;
		}

		public E solve(final String name) {
			return getSettingMap().get(name);
		}
	}

	public static interface ImageUploaderFactory {
		IUploader create(File f, State s, String key) throws IOException;

		Set<String> keys();
	}

	public void init() {
		registerImageUploader("Gyazo", new ImageUploaderFactory() {
			@Override
			public Set<String> keys() {
				final Set<String> keys = Sets.newHashSet();
				List<Info.Api.Image.Gyazo.Config> configs = null;
				try {
					configs = Informations.instance.getSource().info.apis.image.gyazo.config;
					for (final Info.Api.Image.Gyazo.Config config : configs)
						if (config!=null)
							keys.add(config.clientid);
				} catch (final NullPointerException e) {
				}
				return keys;
			}

			@Override
			public IUploader create(final File f, final State s, final String key) throws IOException {
				return new GyazoUpload(f, s, key);
			}
		});
		registerImageUploader("Imgur", new ImageUploaderFactory() {
			@Override
			public Set<String> keys() {
				final Set<String> keys = Sets.newHashSet();
				List<Info.Api.Image.Imgur.Config> configs = null;
				try {
					configs = Informations.instance.getSource().info.apis.image.imgur.config;
					for (final Info.Api.Image.Imgur.Config config : configs)
						if (config!=null)
							keys.add(config.clientid);
				} catch (final NullPointerException e) {
				}
				return keys;
			}

			@Override
			public IUploader create(final File f, final State s, final String key) throws IOException {
				return new ImgurUpload(f, s, key);
			}
		});
		final Pattern p = Pattern.compile("[^\\w]");
		registerURLReplacer(new URLReplacer() {
			@Override
			public String replace(String src) {
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
			public String replace(String src) {
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
