package com.kamesuta.mc.signpic;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

	public Setting<ImageUploaderFactory> imageUploader = new Setting<Apis.ImageUploaderFactory>() {
		@Override
		public String getConfig() {
			return Config.instance.apiType;
		}

		@Override
		public void setConfig(final String setting) {
			Config.instance.get("Api.Upload", "Type", setting).set(setting);
			Config.instance.save();
		};
	};

	public void registerImageUploader(final String name, final ImageUploaderFactory uploader) {
		this.imageUploader.registerSetting(name, uploader);
	}

	public SetSetting imageUploaderKey = new SetSetting() {
		@Override
		public String getConfig() {
			return Config.instance.apiKey;
		}

		@Override
		public void setConfig(final String setting) {
			Config.instance.get("Api.Upload", "Key", setting).set(setting);
			Config.instance.save();
		};
	};

	public static abstract class Setting<E> {
		public String setting;
		private final Map<String, E> settings = Maps.newHashMap();

		public void registerSetting(final String name, final E uploader) {
			this.getSettings().put(name, uploader);
		}

		public Set<E> getSettingValues() {
			final Set<E> ss = Sets.newHashSet();
			for (final Entry<String, E> s : getSettings().entrySet())
				ss.add(s.getValue());
			return ss;
		}

		public Set<String> getSettingKeys() {
			final Set<String> ss = Sets.newHashSet();
			for (final Entry<String, E> s : getSettings().entrySet())
				ss.add(s.getKey());
			return ss;
		}

		public Map<String, E> getSettings() {
			return this.settings;
		}

		public void setSettings(final Map<String, E> m) {
			getSettings().clear();
			getSettings().putAll(m);
		}

		public E getSetting(final String name) {
			return getSettings().get(name);
		}

		public E getSetting() {
			final Set<Map.Entry<String, E>> entry = getSettings().entrySet();
			final int size = entry.size();
			if (size>0) {
				final int item = rnd.nextInt(size);
				int i = 0;
				for (final Map.Entry<String, E> obj : entry) {
					if (i==item)
						return obj.getValue();
					i = i+1;
				}
			}
			return null;
		}

		public E getConfigSetting() {
			final String cfg = getConfig();
			E factory = null;
			if (!StringUtils.isEmpty(cfg)) {
				factory = getSettings().get(cfg);
				if (factory!=null)
					this.setting = cfg;
			} else
				factory = getSetting();
			return factory;
		}

		public void setSetting(final String setting) {
			this.setting = setting;
			setConfig(setting);
		}

		public void clear() {
			getSettings().clear();
		}

		public abstract String getConfig();

		public abstract void setConfig(String setting);
	}

	public static abstract class SetSetting extends Setting<String> {
		public void registerSetting(final String name) {
			getSettings().put(name, name);
		}

		public Set<String> getSettingStrings() {
			final Set<String> ss = Sets.newHashSet();
			for (final Entry<String, String> s : getSettings().entrySet())
				ss.add(s.getKey());
			return ss;
		}

		public void setSettings(final Collection<String> ss) {
			final Map<String, String> m = getSettings();
			clear();
			for (final String s : ss)
				m.put(s, s);
		}

		@Override
		public String getConfigSetting() {
			final String cfg = getConfig();
			String factory = null;
			if (!StringUtils.isEmpty(cfg))
				factory = cfg;
			else
				factory = getSetting();
			return factory;
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
				return new GyazoUpload(f, s.setName("§eGyazo: "+f.getName()), key);
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
				return new ImgurUpload(f, s.setName("§eImgur: "+f.getName()), key);
			}
		});
	}
}
