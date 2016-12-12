package com.kamesuta.mc.signpic;

import java.io.File;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

public final class Config extends Configuration {
	public static Config instance;

	private final File configFile;
	private final Set<IReloadableConfig> configs = Sets.newHashSet();

	public static interface IReloadableConfig {
		void reload();
	}

	public <T extends IReloadableConfig> T registerReload(final T config) {
		this.configs.add(config);
		return config;
	}

	public void reload() {
		for (final IReloadableConfig config : this.configs)
			config.reload();
	}

	public final ConfigProperty<String> signpicDir = propertyString(get("General", "SignpicDir", "").setRequiresMcRestart(true));
	public final ConfigProperty<Boolean> signTooltip = propertyBoolean(get("General", "SignToolTip", false)).setComment("add tooltip line to sign");

	public final ConfigProperty<Integer> imageWidthLimit = propertyInteger(get("Image", "WidthLimit", 512).setRequiresMcRestart(true));
	public final ConfigProperty<Integer> imageHeightLimit = propertyInteger(get("Image", "HeightLimit", 512).setRequiresMcRestart(true));
	public final ConfigProperty<Boolean> imageAnimationGif = propertyBoolean(get("Image", "AnimateGif", true).setRequiresMcRestart(true));

	public final ConfigProperty<Integer> entryGCtick = propertyInteger(get("Entry", "GCDelayTick", 15*20));

	public final ConfigProperty<Integer> communicateThreads = propertyInteger(get("Http", "HttpThreads", 3).setRequiresMcRestart(true)).setComment("parallel processing number such as Downloading");
	public final ConfigProperty<Integer> communicateDLTimedout = propertyInteger(get("Http", "DownloadTimedout", 15000).setRequiresMcRestart(true)).setComment("milliseconds of max waiting response time. 0 is infinity.");

	public final ConfigProperty<Integer> contentLoadThreads = propertyInteger(get("Content", "LoadThreads", 3).setRequiresMcRestart(true)).setComment("parallel processing number such as Image Loading");
	public final ConfigProperty<Integer> contentMaxByte = propertyInteger(get("Content", "MaxByte", 32*1024*1024)).setComment("limit of size before downloading. 0 is infinity.");
	public final ConfigProperty<Integer> contentGCtick = propertyInteger(get("Content", "GCDelayTick", 15*20)).setComment("delay ticks of Garbage Collection");
	public final ConfigProperty<Integer> contentLoadTick = propertyInteger(get("Content", "LoadStartIntervalTick", 0)).setComment("ticks of Load process starting delay (Is other threads, it does not disturb the operation) such as Downloading, File Loading...");
	public final ConfigProperty<Integer> contentSyncTick = propertyInteger(get("Content", "SyncLoadIntervalTick", 0)).setComment("ticks of Sync process interval (A drawing thread, affects the behavior. Please increase the value if the operation is heavy.) such as Gl Texture Uploading");
	public final ConfigProperty<Integer> contentMaxRetry = propertyInteger(get("Content", "MaxRetry", 3)).setComment("limit of retry count. 0 is infinity.");

	public final ConfigProperty<Boolean> informationNotice = propertyBoolean(get("Version", "Notice", true));
	public final ConfigProperty<Boolean> informationJoinBeta;
	public final ConfigProperty<Boolean> informationUpdateGui = propertyBoolean(get("Version", "UpdateGui", true));
	public final ConfigProperty<Boolean> informationTryNew = propertyBoolean(get("Version", "TryNew", false));

	public final ConfigProperty<Boolean> multiplayPAAS = propertyBoolean(get("Multiplay.PreventAntiAutoSign", "Enable", true));
	/** Fastest time "possible" estimate for an empty sign. */
	public final ConfigProperty<Integer> multiplayPAASMinEditTime = propertyInteger(get("Multiplay.PreventAntiAutoSign.Time", "minEditTime", 150));
	/** Minimum time needed to add one extra line (not the first). */
	public final ConfigProperty<Integer> multiplayPAASMinLineTime = propertyInteger(get("Multiplay.PreventAntiAutoSign.Time", "minLineTime", 50));
	/** Minimum time needed to type a character. */
	public final ConfigProperty<Integer> multiplayPAASMinCharTime = propertyInteger(get("Multiplay.PreventAntiAutoSign.Time", "minCharTime", 50));

	public final ConfigProperty<Boolean> renderOverlayPanel = propertyBoolean(get("Render", "OverlayPanel", true)).setComment("Overlay signpic!online");
	public final ConfigProperty<Boolean> renderGuiOverlay = propertyBoolean(get("Render", "GuiOverlay", true)).setComment("Overlay on GUI");
	public final ConfigProperty<Boolean> renderUseMipmap = propertyBoolean(get("Render", "Mipmap", true)).setComment("Require OpenGL 3.0 or later");
	public final ConfigProperty<Boolean> renderMipmapTypeNearest = propertyBoolean(get("Render", "MipmapTypeNearest", false)).setComment("true = Nearest, false = Linear");
	public final ConfigProperty<Double> renderSeeOpacity = propertyDouble(get("Render.Opacity", "ViewSign", .5f));
	public final ConfigProperty<Double> renderPreviewFixedOpacity = propertyDouble(get("Render.Opacity", "PreviewFixedSign", .7f));
	public final ConfigProperty<Double> renderPreviewFloatedOpacity = propertyDouble(get("Render.Opacity", "PreviewFloatedSign", .7f*.7f));

	public final ConfigProperty<String> apiUploaderType = propertyString(get("Api.Upload", "Type", ""));
	public final ConfigProperty<String> apiUploaderKey = propertyString(get("Api.Upload", "Key", ""));
	public final ConfigProperty<String> apiShortenerType = propertyString(get("Api.Shortener", "Type", ""));
	public final ConfigProperty<String> apiShortenerKey = propertyString(get("Api.Shortener", "Key", ""));

	public final ConfigProperty<Boolean> debugLog = propertyBoolean(get("Debug", "DebugLog", false)).setComment("Output Debug Log");

	public final ConfigProperty<Boolean> guiExperienced = propertyBoolean(get("Internal", "GuiExperienced", false)).setComment("Have you ever opened SignPicture GUI yet?");

	public Config(final File configFile) {
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
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (StringUtils.equals(eventArgs.modID, Reference.MODID)) {
			save();
			reload();
		}
	}

	public String getFilePath() {
		return this.configFile.getPath();
	}

	public ConfigProperty<String> propertyString(final Property property) {
		return registerReload(ConfigProperty.propertyString(this, property));
	}

	public ConfigProperty<Boolean> propertyBoolean(final Property property) {
		return registerReload(ConfigProperty.propertyBoolean(this, property));
	}

	public ConfigProperty<Double> propertyDouble(final Property property) {
		return registerReload(ConfigProperty.propertyDouble(this, property));
	}

	public ConfigProperty<Integer> propertyInteger(final Property property) {
		return registerReload(ConfigProperty.propertyInteger(this, property));
	}

	public static abstract class ConfigProperty<E> implements IReloadableConfig {
		protected final Configuration config;
		protected final Property property;
		private transient E prop;

		protected ConfigProperty(final Configuration config, final Property property, final E prop) {
			this.config = config;
			this.property = property;
			this.prop = prop;
		}

		public ConfigProperty<E> setComment(final String comment) {
			this.property.comment = comment;
			return this;
		}

		protected void setProp(final E prop) {
			if (!this.property.requiresMcRestart())
				this.prop = prop;
		}

		public E get() {
			return this.prop;
		}

		public abstract ConfigProperty<E> set(E value);

		public abstract ConfigProperty<E> reset();

		public static ConfigProperty<String> propertyString(final Config config, final Property property) {
			return new StringConfigProperty(config, property);
		}

		public static ConfigProperty<Boolean> propertyBoolean(final Config config, final Property property) {
			return new BooleanConfigProperty(config, property);
		}

		public static ConfigProperty<Double> propertyDouble(final Config config, final Property property) {
			return new DoubleConfigProperty(config, property);
		}

		public static ConfigProperty<Integer> propertyInteger(final Config config, final Property property) {
			return new IntegerConfigProperty(config, property);
		}

		private static class StringConfigProperty extends ConfigProperty<String> {
			protected StringConfigProperty(final Configuration config, final Property property) {
				super(config, property, property.getString());
			}

			@Override
			public StringConfigProperty set(final String value) {
				this.property.set(value);
				setProp(value);
				this.config.save();
				return this;
			}

			@Override
			public StringConfigProperty reset() {
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
			protected BooleanConfigProperty(final Configuration config, final Property property) {
				super(config, property, property.getBoolean());
			}

			@Override
			public BooleanConfigProperty set(final Boolean value) {
				this.property.set(value);
				setProp(value);
				this.config.save();
				return this;
			}

			@Override
			public BooleanConfigProperty reset() {
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
			protected DoubleConfigProperty(final Configuration config, final Property property) {
				super(config, property, property.getDouble());
			}

			@Override
			public DoubleConfigProperty set(final Double value) {
				this.property.set(value);
				setProp(value);
				this.config.save();
				return this;
			}

			@Override
			public DoubleConfigProperty reset() {
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
			protected IntegerConfigProperty(final Configuration config, final Property property) {
				super(config, property, property.getInt());
				property.getType();
			}

			@Override
			public IntegerConfigProperty set(final Integer value) {
				this.property.set(value);
				setProp(value);
				this.config.save();
				return this;
			}

			@Override
			public IntegerConfigProperty reset() {
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