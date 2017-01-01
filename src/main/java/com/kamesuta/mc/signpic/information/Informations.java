package com.kamesuta.mc.signpic.information;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.download.ModDownload;
import com.kamesuta.mc.signpic.http.download.ModDownload.ModDLResult;
import com.kamesuta.mc.signpic.information.Info.PrivateMsg;
import com.kamesuta.mc.signpic.util.ChatBuilder;

import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public final class Informations {
	public static final @Nonnull Informations instance = new Informations();

	public static final @Nonnull Version VersionClient = new Version(Reference.VERSION);

	private Informations() {
	}

	public static class InfoSource {
		public @Nonnull Info info;
		public @Nullable Info.PrivateMsg privateMsg;

		public InfoSource(final @Nonnull Info info) {
			this.info = info;
		}

		public @Nonnull InfoVersion stableVersion() {
			if (this.info.versions!=null)
				return new InfoVersion(this.info.versions.get(Client.mcversion));
			return new InfoVersion();
		}

		public @Nonnull InfoVersion unstableVersion() {
			if (this.info.versions!=null)
				return new InfoVersion(this.info.versions.get(Client.mcversion+"-beta"));
			return new InfoVersion();
		}

		public @Nonnull InfoVersion onlineVersion() {
			final InfoVersion stable = stableVersion();
			final InfoVersion unstable = unstableVersion();
			return Config.getConfig().informationJoinBeta.get()&&unstable.compare(stable) ? unstable : stable;
		}

		public static boolean equalsVersion(final @Nullable InfoSource a, final @Nullable InfoSource b) {
			if (a==null||b==null)
				return false;
			final InfoVersion stable = a.stableVersion();
			final InfoVersion unstable = b.stableVersion();
			return stable.equals(b.stableVersion())&&unstable.equals(b.unstableVersion());
		}
	}

	public static class InfoState {
		public boolean triedToWarnPlayer = false;
		public boolean downloading = false;
		public @Nullable File downloadedFile;

		public boolean isDownloaded() {
			if (this.downloadedFile!=null)
				return this.downloadedFile.exists();
			return false;
		}
	}

	private @Nullable InfoSource source;
	private @Nonnull InfoState state = new InfoState();

	public void setSource(final @Nonnull InfoSource source) {
		if (!InfoSource.equalsVersion(this.source, source))
			this.state = new InfoState();
		this.source = source;
	}

	public @Nullable InfoSource getSource() {
		return this.source;
	}

	public @Nonnull InfoState getState() {
		return this.state;
	}

	public void init() {
		onlineCheck(null);
	}

	private long lastCheck = -1l;

	public boolean shouldCheck(final long l) {
		return System.currentTimeMillis()-this.lastCheck>l;
	}

	public long getLastCheck() {
		return this.lastCheck;
	}

	public void check() {
		getState().triedToWarnPlayer = false;
		if (!isUpdateRequired())
			Log.notice(I18n.format("signpic.versioning.noupdate"));
	}

	public void onlineCheck(final @Nullable Runnable after) {
		this.lastCheck = System.currentTimeMillis();
		final InformationCheck checker = new InformationCheck();
		checker.setCallback(new ICommunicateCallback() {
			@Override
			public void onDone(final ICommunicateResponse res) {
				if (checker.result!=null)
					setSource(checker.result);
				if (res.getError()!=null)
					Log.log.warn("Could not check version information", res.getError());
				if (after!=null)
					after.run();
			}
		});
		Communicator.instance.communicate(checker);
	}

	public boolean isUpdateRequired() {
		final InfoSource source = getSource();
		if (source!=null)
			return !StringUtils.equals(Reference.VERSION, "${version}")&&source.onlineVersion().compare(VersionClient);
		return false;
	}

	public @Nullable String getUpdateMessage() {
		final InfoSource source = getSource();
		if (source!=null) {
			final InfoVersion online = source.onlineVersion();
			if (online.version!=null) {
				final String lang = Client.mc.gameSettings.language;
				final Info.Version version = online.version;
				if (version!=null) {
					final Map<String, String> local = version.message_local;
					if (local!=null&&local.containsKey(lang))
						return local.get(lang);
					else if (!StringUtils.isEmpty(version.message))
						return version.message;
				}
			}
		}
		return null;
	}

	public @Nullable String getUpdateImage() {
		final InfoSource src = this.source;
		if (src!=null) {
			final InfoVersion online = src.onlineVersion();
			final Info.Version version = online.version;
			if (version!=null) {
				final String image = version.image;
				if (image!=null&&!StringUtils.isEmpty(image))
					return image;
			}
		}
		return null;
	}

	public void runUpdate() {
		final Informations.InfoState state = getState();
		final @Nullable Informations.InfoSource source = getSource();
		if (source!=null) {
			final Info.Version version = source.onlineVersion().version;
			if (version!=null&&!StringUtils.isEmpty(version.remote))
				if (state.isDownloaded()) {
					ChatBuilder.create("signpic.versioning.downloadedAlready").useTranslation().setStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).chatClient();
					Log.notice(I18n.format("signpic.gui.notice.versioning.downloadedAlready"));
					try {
						Desktop.getDesktop().open(Client.getLocation().modDir.getCanonicalFile());
					} catch (final IOException e) {
						Log.log.error(e.getMessage(), e);
					}
				} else if (state.downloading) {
					ChatBuilder.create("signpic.versioning.downloadingAlready").useTranslation().setStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).chatClient();
					Log.notice(I18n.format("signpic.gui.notice.versioning.downloadingAlready"));
				} else {
					final ModDownload downloader = new ModDownload();
					downloader.getState().getMeta().put(GuiTask.HighlightPanel, true);
					downloader.getState().getMeta().put(GuiTask.ShowPanel, 3f);
					downloader.setCallback(new ICommunicateCallback() {
						@Override
						public void onDone(final ICommunicateResponse res) {
							final ModDLResult result = downloader.result;
							if (result!=null)
								new ChatBuilder().setChat(result.response).chatClient();
						}
					});
					Communicator.instance.communicate(downloader);
				}
		}
	}

	@CoreEvent
	public void onTick(final @Nonnull ClientTickEvent event) {
		if (event.phase==Phase.END)
			onTick(getSource(), getState());
	}

	public void notice(final @Nullable InfoSource source, final @Nullable InfoState state, final @Nullable EntityPlayer player) {
		if (player!=null&&source!=null&&state!=null) {
			final String lang = Client.mc.gameSettings.language;
			if (
				source.info.versions!=null&&
						!StringUtils.equals(Reference.VERSION, "${version}")
			) {
				final InfoVersion online = source.onlineVersion();

				if (online.compare(VersionClient)) {
					final Info.Version version = online.version;
					if (version!=null&&version.version!=null) {
						final String ver = version.version;
						final Map<String, String> local = version.message_local;
						if (local!=null&&local.containsKey(lang))
							ChatBuilder.create("signpic.versioning.changelog").useTranslation().setParams(local.get(lang)).chatClient();
						else if (!StringUtils.isEmpty(version.message))
							ChatBuilder.create("signpic.versioning.changelog").useTranslation().setParams(version.message).chatClient();

						final String website;
						if (version.website!=null)
							website = version.website;
						else if (source.info.website!=null)
							website = source.info.website;
						else
							website = "https://github.com/Team-Fruit/SignPicture/";

						final String changelog;
						if (version.changelog!=null)
							changelog = version.changelog;
						else if (source.info.changelog!=null)
							changelog = source.info.changelog;
						else
							changelog = "https://github.com/Team-Fruit/SignPicture/releases";

						ChatBuilder.create("signpic.versioning.updateMessage").useTranslation().useJson()
								.replace("$old$", Reference.VERSION)
								.replace("$new$", ver)
								.replace("$download$", "{\"action\":\"run_command\",\"value\":\"/signpic version update\"}")
								.replace("$website$", "{\"action\":\"open_url\",\"value\":\""+website+"\"}")
								.replace("$changelog$", "{\"action\":\"open_url\",\"value\":\""+changelog+"\"}")
								.chatClient();
					}
				}
			}
			final PrivateMsg msg = source.privateMsg;
			if (msg!=null) {
				final ChatBuilder ctb = new ChatBuilder();
				final Map<String, String> local = msg.message_local;
				if (local!=null&&local.containsKey(lang))
					ctb.setText(local.get(lang));
				else {
					final String m = msg.message;
					if (m!=null&&!StringUtils.isEmpty(m))
						ctb.setText(m);
					if (msg.json)
						ctb.useJson();
					ctb.chatClient();
				}
			}
			getState().triedToWarnPlayer = true;
		}
	}

	public void onTick(final @Nullable InfoSource source, final @Nonnull InfoState state) {
		final EntityPlayer player = Client.mc.thePlayer;
		if (Config.getConfig().informationNotice.get()&&!state.triedToWarnPlayer)
			notice(source, state, player);
	}

	public static class Version {
		public final int major;
		public final int minor;
		public final int micro;
		public final boolean beta;

		public Version(final int major, final int minor, final int micro, final boolean beta) {
			this.major = major;
			this.minor = minor;
			this.micro = micro;
			this.beta = beta;
		}

		public Version(final @Nullable String string) {
			final String[] v = StringUtils.split(string, "\\.");
			this.major = v!=null&&v.length>=1 ? NumberUtils.toInt(v[0], 0) : 0;
			this.minor = v!=null&&v.length>=2 ? NumberUtils.toInt(v[1], 0) : 0;
			this.micro = v!=null&&v.length>=3 ? NumberUtils.toInt(v[2], 0) : 0;
			this.beta = v!=null&&v.length>=4&&StringUtils.equals(v[3], "beta");
		}

		public Version() {
			this(0, 0, 0, false);
		}

		public boolean compare(final @Nonnull Version version) {
			return this.major>version.major||
					this.major==version.major&&this.minor>version.minor||
					this.major==version.major&&this.minor==version.minor&&this.micro>version.micro||
					this.major==version.major&&this.minor==version.minor&&this.micro==version.micro&&this.beta&&!version.beta;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime*result+(this.beta ? 1231 : 1237);
			result = prime*result+this.major;
			result = prime*result+this.micro;
			result = prime*result+this.minor;
			return result;
		}

		@Override
		public boolean equals(final @Nullable Object obj) {
			if (this==obj)
				return true;
			if (obj==null)
				return false;
			if (getClass()!=obj.getClass())
				return false;
			final Version other = (Version) obj;
			if (this.beta!=other.beta)
				return false;
			if (this.major!=other.major)
				return false;
			if (this.micro!=other.micro)
				return false;
			if (this.minor!=other.minor)
				return false;
			return true;
		}
	}

	public static class InfoVersion extends Version {
		public final @Nullable Info.Version version;

		public InfoVersion(final int major, final int minor, final int micro, final boolean beta, final @Nullable Info.Version version) {
			super(major, minor, micro, beta);
			this.version = version;
		}

		public InfoVersion(final @Nullable Info.Version version) {
			super(version!=null ? version.version : "");
			this.version = version;
		}

		public InfoVersion() {
			this(0, 0, 0, false, null);
		}
	}
}