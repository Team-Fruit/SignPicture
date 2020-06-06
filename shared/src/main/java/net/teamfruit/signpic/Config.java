package net.teamfruit.signpic;

import net.teamfruit.signpic.compat.CompatConfigSpec;

public class Config {
	private static final CompatConfigSpec.Builder BUILDER = new CompatConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final Image IMAGE = new Image(BUILDER);
	public static final Entry ENTRY = new Entry(BUILDER);
	public static final Http HTTP = new Http(BUILDER);
	public static final Content CONTENT = new Content(BUILDER);
	public static final Version VERSION = new Version(BUILDER);
	public static final Multiplay MULTIPLAY = new Multiplay(BUILDER);
	public static final ChatPicture CHATPICTURE = new ChatPicture(BUILDER);
	public static final Render RENDER = new Render(BUILDER);
	public static final Api API = new Api(BUILDER);
	public static final Debug DEBUG = new Debug(BUILDER);
	public static final CompatConfigSpec spec = BUILDER.build();

	public static class General {
		public final CompatConfigSpec.ConfigValue<String> signpicDir;
		public final CompatConfigSpec.ConfigValue<Boolean> signTooltip;

		public General(final CompatConfigSpec.Builder builder) {
			builder
					.comment("General Settings")
					.translation("signpic.config.general")
					.push("General");
			this.signpicDir = builder
					.comment("Location where image cache etc. are saved")
					.translation("signpic.config.general.location")
					.mcRestart()
					.define("Location", "");
			this.signTooltip = builder
					.comment("Add tooltip line to sign")
					.translation("signpic.config.general.tooltip")
					.define("Location", false);
			builder.pop();
		}
	}

	public static class Image {
		public final CompatConfigSpec.ConfigValue<Integer> imageWidthLimit;
		public final CompatConfigSpec.ConfigValue<Integer> imageHeightLimit;
		public final CompatConfigSpec.ConfigValue<Boolean> imageResizeFast;
		public final CompatConfigSpec.ConfigValue<Boolean> imageAnimationGif;

		public Image(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Image Settings")
					.translation("signpic.config.image")
					.push("Image");
			this.imageWidthLimit = builder
					.comment("Image width is resized when it is larger")
					.translation("signpic.config.image.widthlimit")
					.define("WidthLimit", 512);
			this.imageHeightLimit = builder
					.comment("Image width is resized when it is larger")
					.translation("signpic.config.image.heightlimit")
					.define("HeightLimit", 512);
			this.imageResizeFast = builder
					.comment("Use faster algorithm to resize")
					.translation("signpic.config.image.fastresize")
					.define("FastResize", false);
			this.imageAnimationGif = builder
					.comment("Enable/Disable Image Animation")
					.translation("signpic.config.image.animategif")
					.mcRestart()
					.define("AnimateGif", true);
			builder.pop();
		}
	}

	public static class Entry {
		public final CompatConfigSpec.ConfigValue<Integer> entryGCTick;

		public Entry(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Entry(sign text parse cache) Management Settings")
					.translation("signpic.config.entry")
					.push("Entry");
			this.entryGCTick = builder
					.comment("Garbage Collection Life Tick")
					.translation("signpic.config.entry.lifetick")
					.define("LifeTick", 15*20);
			builder.pop();
		}
	}

	public static class Http {
		public final CompatConfigSpec.ConfigValue<Integer> communicateThreads;
		public final CompatConfigSpec.ConfigValue<Integer> communicateDLTimeout;

		public Http(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Http Settings")
					.translation("signpic.config.http")
					.push("Http");
			this.communicateThreads = builder
					.comment("Parallel processing number such as Downloading")
					.translation("signpic.config.http.threads")
					.mcRestart()
					.define("Threads", 3);
			this.communicateDLTimeout = builder
					.comment("Milliseconds of max waiting response time. 0 is infinity.")
					.translation("signpic.config.http.timeout")
					.mcRestart()
					.define("Timeout", 15000);
			builder.pop();
		}
	}

	public static class Content {
		public final CompatConfigSpec.ConfigValue<Integer> contentLoadThreads;
		public final CompatConfigSpec.ConfigValue<Integer> contentMaxByte;
		public final CompatConfigSpec.ConfigValue<Integer> contentGCTick;
		public final CompatConfigSpec.ConfigValue<Integer> contentLoadTick;
		public final CompatConfigSpec.ConfigValue<Integer> contentSyncTick;
		public final CompatConfigSpec.ConfigValue<Integer> contentMaxRetry;

		public Content(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Content Data Management Settings")
					.translation("signpic.config.content")
					.push("Update");
			this.contentLoadThreads = builder
					.comment("Parallel processing number such as Image Loading")
					.translation("signpic.config.content.threads")
					.mcRestart()
					.define("LoadThreads", 3);
			this.contentMaxByte = builder
					.comment("Limit of size before downloading. 0 is infinity.")
					.translation("signpic.config.content.maxbytes")
					.define("MaxByte", 32*1024*1024);
			this.contentGCTick = builder
					.comment("Delay ticks of Garbage Collection")
					.translation("signpic.config.content.lifetick")
					.define("LifeTick", 15*20);
			this.contentLoadTick = builder
					.comment("Ticks of Load process starting delay (Is other threads, it does not disturb the operation) such as Downloading, File Loading...")
					.translation("signpic.config.content.loadtick")
					.define("LoadStartIntervalTick", 0);
			this.contentSyncTick = builder
					.comment("Ticks of Sync process interval (A drawing thread, affects the behavior. Please increase the value if the operation is heavy.) such as Gl Texture Uploading")
					.translation("signpic.config.content.synctick")
					.define("SyncLoadIntervalTick", 0);
			this.contentMaxRetry = builder
					.comment("Limit of retry count. 0 is infinity")
					.translation("signpic.config.content.maxretry")
					.define("MaxRetry", 3);
			builder.pop();
		}
	}

	public static class Version {
		public final CompatConfigSpec.ConfigValue<Boolean> informationNotice;
		public final CompatConfigSpec.ConfigValue<Boolean> informationUpdateGui;

		public Version(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Mod Update Checker Settings")
					.translation("signpic.config.update")
					.push("Update");
			this.informationNotice = builder
					.comment("Show Update Notification")
					.translation("signpic.config.update.notification")
					.define("Notification", true);
			this.informationUpdateGui = builder
					.comment("Show Update Notification in Gui")
					.translation("signpic.config.update.notificationgui")
					.define("NotificationGui", true);
			builder.pop();
		}
	}

	public static class Multiplay {
		public final CompatConfigSpec.ConfigValue<Boolean> multiplayPAAS;
		/** Fastest time "possible" estimate for an empty sign. */
		public final CompatConfigSpec.ConfigValue<Integer> multiplayPAASMinEditTime;
		/** Minimum time needed to add one extra line (not the first). */
		public final CompatConfigSpec.ConfigValue<Integer> multiplayPAASMinLineTime;
		/** Minimum time needed to type a character. */
		public final CompatConfigSpec.ConfigValue<Integer> multiplayPAASMinCharTime;

		public Multiplay(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Multiplayer Settings (e.g. NoCheatPlus)")
					.translation("signpic.config.multiplay")
					.push("Multiplay");
			{
				builder
						.comment("Prevent from Anti-AutoSign Plugin such as NoCheatPlus. (ms)")
						.translation("signpic.config.multiplay.paas")
						.push("PreventAntiAutoSign");
				this.multiplayPAAS = builder
						.comment("Enable/Disable Prevent Anti Auto-Sign Plugin Feature")
						.translation("signpic.config.multiplay.paas.enable")
						.define("Enable", false);
				this.multiplayPAASMinEditTime = builder
						.comment("Min Edit Time")
						.translation("signpic.config.multiplay.paas.edit")
						.define("EditTime", 150);
				this.multiplayPAASMinLineTime = builder
						.comment("Min Line Edit Time")
						.translation("signpic.config.multiplay.paas.lineedit")
						.define("EditLineTime", 50);
				this.multiplayPAASMinCharTime = builder
						.comment("Min Char Edit Time")
						.translation("signpic.config.multiplay.paas.charedit")
						.define("EditCharTime", 50);
				builder.pop();
			}
			builder.pop();
		}
	}

	public static class ChatPicture {
		public final CompatConfigSpec.ConfigValue<Boolean> chatpicEnable;
		public final CompatConfigSpec.ConfigValue<Integer> chatpicLine;
		public final CompatConfigSpec.ConfigValue<Integer> chatpicStackTick;

		public ChatPicture(final CompatConfigSpec.Builder builder) {
			builder
					.comment("SignPicture in Chat Settings")
					.translation("signpic.config.chatpicture")
					.push("ChatPicture");
			this.chatpicEnable = builder
					.comment("Enable ChatPicture extension")
					.translation("signpic.config.chatpicture.enable")
					.define("Enable", true);
			this.chatpicLine = builder
					.comment("How many lines does image use")
					.translation("signpic.config.chatpicture.imagelines")
					.define("ImageLines", 4);
			this.chatpicStackTick = builder
					.comment("Stack chat lines within interval ticks")
					.translation("signpic.config.chatpicture.stackticks")
					.define("StackTicks", 50);
			builder.pop();
		}
	}

	public static class Render {
		public final CompatConfigSpec.ConfigValue<Boolean> renderOverlayPanel;
		public final CompatConfigSpec.ConfigValue<Boolean> renderGuiOverlay;
		public final CompatConfigSpec.ConfigValue<Boolean> renderUseMipmap;
		public final CompatConfigSpec.ConfigValue<Boolean> renderMipmapTypeNearest;
		public final CompatConfigSpec.ConfigValue<Double> renderSeeOpacity;
		public final CompatConfigSpec.ConfigValue<Double> renderPreviewFixedOpacity;
		public final CompatConfigSpec.ConfigValue<Double> renderPreviewFloatedOpacity;

		public Render(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Mod Update Checker Settings")
					.translation("signpic.config.render")
					.push("Render");
			this.renderOverlayPanel = builder
					.comment("Overlay signpic!online")
					.translation("signpic.config.render.overlaypanel")
					.define("OverlayPanel", true);
			this.renderGuiOverlay = builder
					.comment("Overlay on GUI")
					.translation("signpic.config.render.guioverlay")
					.define("GuiOverlay", true);
			this.renderUseMipmap = builder
					.comment("Require OpenGL 3.0 or later")
					.translation("signpic.config.render.minimap")
					.define("Mipmap", true);
			this.renderMipmapTypeNearest = builder
					.comment("true = Nearest, false = Linear")
					.translation("signpic.config.render.minimap.nearest")
					.define("MipmapTypeNearest", false);
			{
				builder
						.comment("Opacity")
						.translation("signpic.config.render.opacity")
						.push("Opacity");
				this.renderSeeOpacity = builder
						.comment("Opacity")
						.translation("signpic.config.render.opacity.sign")
						.define("ViewSign", .5);
				this.renderPreviewFixedOpacity = builder
						.comment("Preview Opacity")
						.translation("signpic.config.render.opacity.sign.preview.fixed")
						.define("PreviewFixedSign", .7);
				this.renderPreviewFloatedOpacity = builder
						.comment("Preview Opacity before right click")
						.translation("signpic.config.render.opacity.sign.preview.floating")
						.define("PreviewFloatedSign", .7 * .7);
				builder.pop();
			}
			builder.pop();
		}
	}

	public static class Api {
		public final CompatConfigSpec.ConfigValue<String> apiUploaderType;
		public final CompatConfigSpec.ConfigValue<String> apiUploaderKey;
		public final CompatConfigSpec.ConfigValue<String> apiShortenerType;
		public final CompatConfigSpec.ConfigValue<String> apiShortenerKey;

		public Api(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Api Upload Settings")
					.translation("signpic.config.api")
					.push("Api");
			{
				builder
						.comment("Uploader Settings")
						.translation("signpic.config.api.uploader")
						.push("Uploader");
				this.apiUploaderType = builder
						.comment("")
						.translation("signpic.config.api.uploader.type")
						.define("Type", "");
				this.apiUploaderKey = builder
						.comment("")
						.translation("signpic.config.api.uploader.key")
						.define("Key", "");
				builder.pop();
			}
			{
				builder
						.comment("Shortener Settings")
						.translation("signpic.config.api.shortener")
						.push("Shortener");
				this.apiShortenerType = builder
						.comment("")
						.translation("signpic.config.api.shortener.type")
						.define("Type", "");
				this.apiShortenerKey = builder
						.comment("")
						.translation("signpic.config.api.shortener.key")
						.define("Key", "");
				builder.pop();
			}
			builder.pop();
		}
	}

	public static class Debug {
		public final CompatConfigSpec.ConfigValue<Boolean> debugLog;

		public Debug(final CompatConfigSpec.Builder builder) {
			builder
					.comment("Debug")
					.translation("signpic.config.debug")
					.push("Debug");
			this.debugLog = builder
					.comment("Output Debug Log")
					.translation("signpic.config.debug.log")
					.define("DebugLog", false);
			builder.pop();
		}
	}
}