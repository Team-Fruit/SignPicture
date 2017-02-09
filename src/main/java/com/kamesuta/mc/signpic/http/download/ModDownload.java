package com.kamesuta.mc.signpic.http.download;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.LoadCanceledException;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
import com.kamesuta.mc.signpic.information.Info.Version;
import com.kamesuta.mc.signpic.information.Informations;
import com.kamesuta.mc.signpic.state.Progressable;
import com.kamesuta.mc.signpic.state.State;
import com.kamesuta.mc.signpic.util.ChatBuilder;
import com.kamesuta.mc.signpic.util.Downloader;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ModDownload extends Communicate implements Progressable {
	protected boolean canceled;
	protected @Nonnull State status = new State().setName("§6SignPicture Mod Update");
	public @Nullable ModDLResult result;

	@Override
	public void communicate() {
		final Informations.InfoState state = Informations.instance.getState();
		final Informations.InfoSource source = Informations.instance.getSource();
		File tmp = null;
		InputStream input = null;
		OutputStream output = null;
		try {
			setCurrent();
			if (source==null)
				throw new IllegalStateException("No update data available");
			final Informations.InfoVersion online = source.onlineVersion();
			final Version version = online.version;
			if (version==null)
				throw new IllegalStateException("Invalid version data");
			final String stringremote = version.remote;
			final String stringlocal = version.local;
			final String local;
			if (stringlocal!=null&&!StringUtils.isEmpty(stringlocal))
				local = stringlocal;
			else if (stringremote!=null&&!StringUtils.isEmpty(stringremote))
				local = stringremote.substring(stringremote.lastIndexOf("/")+1, stringremote.length());
			else
				throw new IllegalStateException("No update url provided in repository");

			ChatBuilder.create("signpic.versioning.startingDownload").setParams(local).useTranslation().useJson().chatClient();

			Log.notice(I18n.format("signpic.gui.notice.downloading", local));

			state.downloading = true;

			final HttpUriRequest req = new HttpGet(new URI(version.remote));
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			this.status.getProgress().overall = entity.getContentLength();
			input = entity.getContent();

			tmp = Client.getLocation().createCache("modupdate");
			final File f1 = new File(Client.getLocation().modDir, local);

			output = new CountingOutputStream(new FileOutputStream(tmp)) {
				@Override
				protected void afterWrite(final int n) throws IOException {
					if (ModDownload.this.canceled) {
						req.abort();
						throw new LoadCanceledException();
					}
					ModDownload.this.status.getProgress().setDone(getByteCount());
				}
			};

			IOUtils.copyLarge(input, output);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(f1);
			if (!f1.exists())
				FileUtils.moveFile(tmp, f1);

			IChatComponent chat;
			if (Client.getLocation().modFile.isFile())
				chat = ChatBuilder.create("signpic.versioning.doneDownloadingWithFile").useTranslation().setId(897).setParams(local, Client.getLocation().modFile.getName()).setStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)).build();
			else
				chat = ChatBuilder.create("signpic.versioning.doneDownloading").useTranslation().setId(897).setParams(local).setStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)).build();
			Log.notice(I18n.format("signpic.gui.notice.downloaded", local));

			Desktop.getDesktop().open(Client.getLocation().modDir.getCanonicalFile());
			state.downloadedFile = f1;

			this.result = new ModDLResult(chat);
			onDone(new CommunicateResponse(true, null));
			return;
		} catch (final Throwable e) {
			Log.log.warn("Updater Downloading Error", e);
			final IChatComponent chat = new ChatBuilder().setChat(new ChatComponentTranslation("signpic.versioning.error").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))).build();
			this.result = new ModDLResult(chat);
			onDone(new CommunicateResponse(false, e));
			return;
		} finally {
			unsetCurrent();
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(tmp);
			state.downloading = false;
		}
	}

	@Override
	public @Nonnull State getState() {
		return this.status;
	}

	@Override
	public void cancel() {
		this.canceled = true;
		super.cancel();
	}

	public static class ModDLResult {
		public final @Nonnull IChatComponent response;

		public ModDLResult(final @Nonnull IChatComponent response) {
			this.response = response;
		}
	}
}