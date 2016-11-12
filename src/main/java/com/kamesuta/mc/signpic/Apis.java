package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

	private static final Random rnd = new Random();

	public final MapSetting<ImageUploaderFactory> imageUploader = new MapSetting<Apis.ImageUploaderFactory>() {
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
		this.imageUploader.registerSetting(name, uploader);
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
	}
}
