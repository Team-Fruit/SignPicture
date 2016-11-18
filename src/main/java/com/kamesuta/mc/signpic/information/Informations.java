package com.kamesuta.mc.signpic.information;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.CoreEvent;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.gui.OverlayFrame;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.download.ModDownload;
import com.kamesuta.mc.signpic.http.download.ModDownload.ModDLResult;
import com.kamesuta.mc.signpic.util.ChatBuilder;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public final class Informations {
	public static final Informations instance = new Informations();

	public static final Version VersionClient = new Version(Reference.VERSION);

	private Informations() {
	}

	public static class InfoSource {
		public Info info;
		public Info.PrivateMsg privateMsg;

		public InfoSource(final Info info) {
			this.info = info;
		}

		public InfoVersion stableVersion() {
			if (this.info.versions!=null)
				return new InfoVersion(this.info.versions.get(Client.mcversion));
			return new InfoVersion();
		}

		public InfoVersion unstableVersion() {
			if (this.info.versions!=null)
				return new InfoVersion(this.info.versions.get(Client.mcversion+"-beta"));
			return new InfoVersion();
		}

		public InfoVersion onlineVersion() {
			final InfoVersion stable = stableVersion();
			final InfoVersion unstable = unstableVersion();
			return Config.instance.informationJoinBeta&&unstable.compare(stable) ? unstable : stable;
		}

		public static boolean equalsVersion(final InfoSource a, final InfoSource b) {
			if (a==null||b==null)
				return false;
			final InfoVersion stable = a.stableVersion();
			final InfoVersion unstable = b.stableVersion();
			if (stable==null||unstable==null)
				return false;
			return stable.equals(b.stableVersion())&&unstable.equals(b.unstableVersion());
		}
	}

	public static class InfoState {
		public boolean triedToWarnPlayer = false;
		public boolean downloading = false;
		public File downloadedFile;

		public boolean isDownloaded() {
			if (this.downloadedFile!=null)
				return this.downloadedFile.exists();
			return false;
		}
	}

	private InfoSource source;
	private InfoState state = new InfoState();

	public void setSource(final InfoSource source) {
		if (!InfoSource.equalsVersion(this.source, source))
			this.state = new InfoState();
		this.source = source;
	}

	public InfoSource getSource() {
		return this.source;
	}

	public InfoState getState() {
		return this.state;
	}

	public void init() {
		check();
	}

	private long lastCheck = -1l;

	public boolean shouldCheck(final long l) {
		return System.currentTimeMillis()-this.lastCheck>l;
	}

	public void checkInterval(final long l) {
		if (shouldCheck(l))
			check();
	}

	public void check() {
		this.lastCheck = System.currentTimeMillis();
		final InformationCheck checker = new InformationCheck();
		checker.setCallback(new ICommunicateCallback() {
			@Override
			public void onDone(final ICommunicateResponse res) {
				if (checker.result!=null)
					setSource(checker.result);
				if (res.getError()!=null)
					Reference.logger.warn("Could not check version information", res.getError());
			}
		});
		Communicator.instance.communicate(checker);
	}

	public boolean isUpdateRequired() {
		if (getSource()!=null)
			return !StringUtils.equals(Reference.VERSION, "${version}")&&getSource().onlineVersion().compare(VersionClient);
		return false;
	}

	public String getUpdateMessage() {
		final InfoVersion online = this.source.onlineVersion();
		if (online.version!=null) {
			final String lang = Client.mc.gameSettings.language;
			final Info.Version version = online.version;
			if (version.message_local!=null&&version.message_local.containsKey(lang))
				return version.message_local.get(lang);
			else if (!StringUtils.isEmpty(version.message))
				return version.message;
		}
		return null;
	}

	public void runUpdate() {
		final Informations.InfoState state = getState();
		final Informations.InfoSource source = getSource();
		final Informations.InfoVersion online = source.onlineVersion();
		if (source!=null&&online!=null&&online.version!=null&&!StringUtils.isEmpty(online.version.remote))
			if (state.isDownloaded()) {
				ChatBuilder.create("signpic.versioning.downloadedAlready").useTranslation().setStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).chatClient();
				OverlayFrame.instance.pane.addNotice1(I18n.format("signpic.gui.notice.versioning.downloadedAlready"), 2f);
				try {
					Desktop.getDesktop().open(Client.location.modDir.getCanonicalFile());
				} catch (final IOException e) {
					Reference.logger.error(e.getMessage(), e);
				}
			} else if (state.downloading) {
				ChatBuilder.create("signpic.versioning.downloadingAlready").useTranslation().setStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).chatClient();
				OverlayFrame.instance.pane.addNotice1(I18n.format("signpic.gui.notice.versioning.downloadingAlready"), 2f);
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

	@CoreEvent
	public void onTick(final ClientTickEvent event) {
		if (event.phase==Phase.END)
			onTick(getSource(), getState());
	}

	public void onTick(final InfoSource source, final InfoState state) {
		final EntityPlayer player = Client.mc.thePlayer;
		if (player!=null&&!state.triedToWarnPlayer&&source!=null) {
			final String lang = Client.mc.gameSettings.language;
			if (source.info!=null&&
					source.info.versions!=null&&
					Config.instance.informationNotice&&
					!StringUtils.equals(Reference.VERSION, "${version}")) {
				final InfoVersion online = source.onlineVersion();

				if (online.compare(VersionClient))
					if (online.version!=null) {
						final Info.Version version = online.version;
						if (version.message_local!=null&&version.message_local.containsKey(lang))
							ChatBuilder.create("signpic.versioning.changelog").useTranslation().setParams(version.message_local.get(lang)).chatClient();
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
								.replace("$new$", version.version)
								.replace("$download$", "{\"action\":\"run_command\",\"value\":\"/signpic-download-latest\"}")
								.replace("$website$", "{\"action\":\"open_url\",\"value\":\""+website+"\"}")
								.replace("$changelog$", "{\"action\":\"open_url\",\"value\":\""+changelog+"\"}")
								.chatClient();
					}
			}
			if (source.privateMsg!=null) {
				final ChatBuilder ctb = new ChatBuilder();
				if (source.privateMsg.message_local!=null&&source.privateMsg.message_local.containsKey(lang))
					ctb.setText(source.privateMsg.message_local.get(lang));
				else if (!StringUtils.isEmpty(source.privateMsg.message))
					ctb.setText(source.privateMsg.message);
				if (source.privateMsg.json)
					ctb.useJson();
				ctb.chatClient();
			}
			getState().triedToWarnPlayer = true;
		}
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

		public Version(final String string) {
			final String[] v = StringUtils.split(string, "\\.");
			this.major = v!=null&&v.length>=1 ? NumberUtils.toInt(v[0], 0) : 0;
			this.minor = v!=null&&v.length>=2 ? NumberUtils.toInt(v[1], 0) : 0;
			this.micro = v!=null&&v.length>=3 ? NumberUtils.toInt(v[2], 0) : 0;
			this.beta = v!=null&&v.length>=4&&StringUtils.equals(v[3], "beta");
		}

		public Version() {
			this(0, 0, 0, false);
		}

		public boolean compare(final Version version) {
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
		public boolean equals(final Object obj) {
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
		public final Info.Version version;

		public InfoVersion(final int major, final int minor, final int micro, final boolean beta, final Info.Version version) {
			super(major, minor, micro, beta);
			this.version = version;
		}

		public InfoVersion(final Info.Version version) {
			super(version!=null ? version.version : "");
			this.version = version;
		}

		public InfoVersion() {
			this(0, 0, 0, false, null);
		}
	}
}