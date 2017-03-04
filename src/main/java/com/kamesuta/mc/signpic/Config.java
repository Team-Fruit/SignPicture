package com.kamesuta.mc.signpic;

import java.io.File;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;
import com.kamesuta.mc.bnnwidget.component.MButton;
import com.kamesuta.mc.bnnwidget.component.MPanel;
import com.kamesuta.mc.signpic.image.ImageIOLoader;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public final class Config extends Configuration {
	private static @Nullable Config instance;

	public static @Nonnull Config getConfig() {
		if (instance!=null)
			return instance;
		throw new IllegalStateException("config not initialized");
	}

	public static void init(final @Nonnull File cfgFile) {
		instance = new Config(cfgFile);
	}

	private final @Nonnull File configFile;
	private final @Nonnull Set<IReloadableConfig> configs = Sets.newHashSet();

	public static interface IReloadableConfig {
		void reload();
	}

	public @Nonnull <T extends IReloadableConfig> T registerReload(final @Nonnull T config) {
		this.configs.add(config);
		return config;
	}

	public void reload() {
		for (final IReloadableConfig config : this.configs)
			config.reload();
	}

	public final @Nonnull ConfigProperty<String> signpicDir = propertyString(get("General", "SignpicDir", "").setRequiresMcRestart(true));
	public final @Nonnull ConfigProperty<Boolean> signTooltip = propertyBoolean(get("General", "SignToolTip", false)).setComment("add tooltip line to sign");

	public final @Nonnull ConfigProperty<Integer> imageWidthLimit = propertyInteger(get("Image", "WidthLimit", 512)).setListener(new ConfigListener<Integer>() {
		@Override
		public void onChanged(@Nonnull final Integer value) {
			ImageIOLoader.MAX_SIZE = ImageIOLoader.maxSize(value, Config.this.imageHeightLimit.get());
		}
	});
	public final @Nonnull ConfigProperty<Integer> imageHeightLimit = propertyInteger(get("Image", "HeightLimit", 512)).setListener(new ConfigListener<Integer>() {
		@Override
		public void onChanged(@Nonnull final Integer value) {
			ImageIOLoader.MAX_SIZE = ImageIOLoader.maxSize(Config.this.imageWidthLimit.get(), value);
		};
	});
	public final @Nonnull ConfigProperty<Boolean> imageResizeFast = propertyBoolean(get("Image", "FastResize", false));
	public final @Nonnull ConfigProperty<Boolean> imageAnimationGif = propertyBoolean(get("Image", "AnimateGif", true).setRequiresMcRestart(true));

	public final @Nonnull ConfigProperty<Integer> entryGCtick = propertyInteger(get("Entry", "GCDelayTick", 15*20));

	public final @Nonnull ConfigProperty<Integer> communicateThreads = propertyInteger(get("Http", "HttpThreads", 3).setRequiresMcRestart(true)).setComment("parallel processing number such as Downloading");
	public final @Nonnull ConfigProperty<Integer> communicateDLTimedout = propertyInteger(get("Http", "DownloadTimedout", 15000).setRequiresMcRestart(true)).setComment("milliseconds of max waiting response time. 0 is infinity.");

	public final @Nonnull ConfigProperty<Integer> contentLoadThreads = propertyInteger(get("Content", "LoadThreads", 3).setRequiresMcRestart(true)).setComment("parallel processing number such as Image Loading");
	public final @Nonnull ConfigProperty<Integer> contentMaxByte = propertyInteger(get("Content", "MaxByte", 32*1024*1024)).setComment("limit of size before downloading. 0 is infinity.");
	public final @Nonnull ConfigProperty<Integer> contentGCtick = propertyInteger(get("Content", "GCDelayTick", 15*20)).setComment("delay ticks of Garbage Collection");
	public final @Nonnull ConfigProperty<Integer> contentLoadTick = propertyInteger(get("Content", "LoadStartIntervalTick", 0)).setComment("ticks of Load process starting delay (Is other threads, it does not disturb the operation) such as Downloading, File Loading...");
	public final @Nonnull ConfigProperty<Integer> contentSyncTick = propertyInteger(get("Content", "SyncLoadIntervalTick", 0)).setComment("ticks of Sync process interval (A drawing thread, affects the behavior. Please increase the value if the operation is heavy.) such as Gl Texture Uploading");
	public final @Nonnull ConfigProperty<Integer> contentMaxRetry = propertyInteger(get("Content", "MaxRetry", 3)).setComment("limit of retry count. 0 is infinity.");

	public final @Nonnull ConfigProperty<Boolean> informationNotice = propertyBoolean(get("Version", "Notice", true));
	public final @Nonnull ConfigProperty<Boolean> informationJoinBeta;
	public final @Nonnull ConfigProperty<Boolean> informationUpdateGui = propertyBoolean(get("Version", "UpdateGui", true));
	public final @Nonnull ConfigProperty<Boolean> informationTryNew = propertyBoolean(get("Version", "TryNew", false));

	public final @Nonnull ConfigProperty<Boolean> multiplayPAAS = propertyBoolean(get("Multiplay.PreventAntiAutoSign", "Enable", true));
	/** Fastest time "possible" estimate for an empty sign. */
	public final @Nonnull ConfigProperty<Integer> multiplayPAASMinEditTime = propertyInteger(get("Multiplay.PreventAntiAutoSign.Time", "minEditTime", 150));
	/** Minimum time needed to add one extra line (not the first). */
	public final @Nonnull ConfigProperty<Integer> multiplayPAASMinLineTime = propertyInteger(get("Multiplay.PreventAntiAutoSign.Time", "minLineTime", 50));
	/** Minimum time needed to type a character. */
	public final @Nonnull ConfigProperty<Integer> multiplayPAASMinCharTime = propertyInteger(get("Multiplay.PreventAntiAutoSign.Time", "minCharTime", 50));

	public final @Nonnull ConfigProperty<Boolean> chatpicEnable = propertyBoolean(get("ChatPicture", "Enable", true)).setComment("enable ChatPicture extension");
	public final @Nonnull ConfigProperty<Integer> chatpicLine = propertyInteger(get("ChatPicture", "ImageLines", 4)).setComment("how many lines does image use");
	public final @Nonnull ConfigProperty<Integer> chatpicStackTick = propertyInteger(get("ChatPicture", "StackTicks", 50)).setComment("stack chat lines within interval ticks");

	public final @Nonnull ConfigProperty<Boolean> renderOverlayPanel = propertyBoolean(get("Render", "OverlayPanel", true)).setComment("Overlay signpic!online");
	public final @Nonnull ConfigProperty<Boolean> renderGuiOverlay = propertyBoolean(get("Render", "GuiOverlay", true)).setComment("Overlay on GUI");
	public final @Nonnull ConfigProperty<Boolean> renderUseMipmap = propertyBoolean(get("Render", "Mipmap", true)).setComment("Require OpenGL 3.0 or later");
	public final @Nonnull ConfigProperty<Boolean> renderMipmapTypeNearest = propertyBoolean(get("Render", "MipmapTypeNearest", false)).setComment("true = Nearest, false = Linear");
	public final @Nonnull ConfigProperty<Double> renderSeeOpacity = propertyDouble(get("Render.Opacity", "ViewSign", .5f));
	public final @Nonnull ConfigProperty<Double> renderPreviewFixedOpacity = propertyDouble(get("Render.Opacity", "PreviewFixedSign", .7f));
	public final @Nonnull ConfigProperty<Double> renderPreviewFloatedOpacity = propertyDouble(get("Render.Opacity", "PreviewFloatedSign", .7f*.7f));

	public final @Nonnull ConfigProperty<String> apiUploaderType = propertyString(get("Api.Upload", "Type", ""));
	public final @Nonnull ConfigProperty<String> apiUploaderKey = propertyString(get("Api.Upload", "Key", ""));
	public final @Nonnull ConfigProperty<String> apiShortenerType = propertyString(get("Api.Shortener", "Type", ""));
	public final @Nonnull ConfigProperty<String> apiShortenerKey = propertyString(get("Api.Shortener", "Key", ""));

	public final @Nonnull ConfigProperty<Boolean> debugLog = propertyBoolean(get("Debug", "DebugLog", false)).setComment("Output Debug Log");

	public final @Nonnull ConfigProperty<Boolean> guiExperienced = propertyBoolean(get("Internal", "GuiExperienced", false)).setComment("Have you ever opened SignPicture GUI yet?");

	{
		this.informationTryNew.setListener(new ConfigListener<Boolean>() {
			{
				onChanged(Config.this.informationTryNew.get());
			}

			@Override
			public void onChanged(final Boolean value) {
				MButton.tryNew = value;
				MPanel.tryNew = value;
			}
		});
	}

	private Config(final @Nonnull File configFile) {
		super(configFile);
		this.configFile = configFile;

		addCustomCategoryComment("Entry", "Entry(sign text parse cache) Management");
		addCustomCategoryComment("Content", "Content Data Management");
		addCustomCategoryComment("Multiplay.PreventAntiAutoSign", "Prevent from Anti-AutoSign Plugin such as NoCheatPlus. (ms)");
		addCustomCategoryComment("Api.Upload", "Api Upload Settings");

		final Property joinBeta = get("Version", "JoinBeta", false);
		final String[] v = StringUtils.split(Reference.VERSION, "\\.");
		if (v.length>=4&&StringUtils.equals(v[3], "beta"))
			joinBeta.set(true);
		this.informationJoinBeta = propertyBoolean(joinBeta);
	}

	@Override
	public void save() {
		if (hasChanged())
			super.save();
	}

	@CoreEvent
	public void onConfigChanged(final @Nonnull ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (StringUtils.equals(eventArgs.modID, Reference.MODID)) {
			save();
			reload();
		}
	}

	public @Nonnull String getFilePath() {
		return this.configFile.getPath();
	}

	public @Nonnull ConfigProperty<String> propertyString(final @Nonnull Property property) {
		return registerReload(ConfigProperty.propertyString(this, property));
	}

	public @Nonnull ConfigProperty<Boolean> propertyBoolean(final @Nonnull Property property) {
		return registerReload(ConfigProperty.propertyBoolean(this, property));
	}

	public @Nonnull ConfigProperty<Double> propertyDouble(final @Nonnull Property property) {
		return registerReload(ConfigProperty.propertyDouble(this, property));
	}

	public @Nonnull ConfigProperty<Integer> propertyInteger(final @Nonnull Property property) {
		return registerReload(ConfigProperty.propertyInteger(this, property));
	}

	public static interface ConfigListener<E> {
		void onChanged(@Nonnull E value);
	}

	public static abstract class ConfigProperty<E> implements IReloadableConfig {
		protected final @Nonnull Configuration config;
		protected final @Nonnull Property property;
		private transient @Nonnull E prop;
		private @Nullable ConfigListener<E> listener;

		protected ConfigProperty(final @Nonnull Configuration config, final @Nonnull Property property, final @Nonnull E prop) {
			this.config = config;
			this.property = property;
			this.prop = prop;
		}

		public @Nonnull ConfigProperty<E> setListener(@Nullable final ConfigListener<E> listener) {
			this.listener = listener;
			return this;
		}

		public @Nonnull ConfigProperty<E> setComment(final @Nonnull String comment) {
			this.property.comment = comment;
			return this;
		}

		protected void setProp(final @Nonnull E prop) {
			if (!this.property.requiresMcRestart()) {
				this.prop = prop;
				if (this.listener!=null)
					this.listener.onChanged(prop);
			}
		}

		public @Nonnull E get() {
			return this.prop;
		}

		public abstract @Nonnull ConfigProperty<E> set(@Nonnull E value);

		public abstract @Nonnull ConfigProperty<E> reset();

		public static @Nonnull ConfigProperty<String> propertyString(final @Nonnull Config config, final @Nonnull Property property) {
			return new StringConfigProperty(config, property);
		}

		public static @Nonnull ConfigProperty<Boolean> propertyBoolean(final @Nonnull Config config, final @Nonnull Property property) {
			return new BooleanConfigProperty(config, property);
		}

		public static @Nonnull ConfigProperty<Double> propertyDouble(final @Nonnull Config config, final @Nonnull Property property) {
			return new DoubleConfigProperty(config, property);
		}

		public static @Nonnull ConfigProperty<Integer> propertyInteger(final @Nonnull Config config, final @Nonnull Property property) {
			return new IntegerConfigProperty(config, property);
		}

		private static class StringConfigProperty extends ConfigProperty<String> {
			protected StringConfigProperty(final @Nonnull Configuration config, final @Nonnull Property property) {
				super(config, property, property.getString());
			}

			@Override
			public @Nonnull StringConfigProperty set(final @Nonnull String value) {
				this.property.set(value);
				setProp(value);
				this.config.save();
				return this;
			}

			@Override
			public @Nonnull StringConfigProperty reset() {
				final String p = this.property.getDefault();
				this.property.set(p);
				setProp(p);
				this.config.save();
				return this;
			}

			@Override
			public void reload() {
				setProp(this.property.getString());
			}
		}

		private static class BooleanConfigProperty extends ConfigProperty<Boolean> {
			protected BooleanConfigProperty(final @Nonnull Configuration config, final @Nonnull Property property) {
				super(config, property, property.getBoolean());
			}

			@Override
			public @Nonnull BooleanConfigProperty set(final @Nonnull Boolean value) {
				this.property.set(value);
				setProp(value);
				this.config.save();
				return this;
			}

			@Override
			public @Nonnull BooleanConfigProperty reset() {
				final String p = this.property.getDefault();
				this.property.set(p);
				setProp(this.property.getBoolean());
				this.config.save();
				return this;
			}

			@Override
			public void reload() {
				setProp(this.property.getBoolean());
			}
		}

		private static class DoubleConfigProperty extends ConfigProperty<Double> {
			protected DoubleConfigProperty(final @Nonnull Configuration config, final @Nonnull Property property) {
				super(config, property, property.getDouble());
			}

			@Override
			public @Nonnull DoubleConfigProperty set(final @Nonnull Double value) {
				this.property.set(value);
				setProp(value);
				this.config.save();
				return this;
			}

			@Override
			public @Nonnull DoubleConfigProperty reset() {
				final String p = this.property.getDefault();
				this.property.set(p);
				setProp(this.property.getDouble());
				this.config.save();
				return this;
			}

			@Override
			public void reload() {
				setProp(this.property.getDouble());
			}
		}

		private static class IntegerConfigProperty extends ConfigProperty<Integer> {
			protected IntegerConfigProperty(final @Nonnull Configuration config, final @Nonnull Property property) {
				super(config, property, property.getInt());
			}

			@Override
			public @Nonnull IntegerConfigProperty set(final @Nonnull Integer value) {
				this.property.set(value);
				setProp(value);
				this.config.save();
				return this;
			}

			@Override
			public @Nonnull IntegerConfigProperty reset() {
				final String p = this.property.getDefault();
				this.property.set(p);
				setProp(this.property.getInt());
				this.config.save();
				return this;
			}

			@Override
			public void reload() {
				setProp(this.property.getInt());
			}
		}
	}
}