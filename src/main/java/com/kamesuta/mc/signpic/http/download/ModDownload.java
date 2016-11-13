package com.kamesuta.mc.signpic.http.download;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.gui.OverlayFrame;
import com.kamesuta.mc.signpic.http.Communicate;
import com.kamesuta.mc.signpic.http.CommunicateCanceledException;
import com.kamesuta.mc.signpic.http.CommunicateResponse;
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
	protected State status = new State("§6SignPicture Mod Update");
	public ModDLResult result;

	@Override
	public void communicate() {
		final Informations.InfoState state = Informations.instance.getState();
		final Informations.InfoSource source = Informations.instance.getSource();
		final Informations.InfoVersion online = source.onlineVersion();
		InputStream input = null;
		OutputStream output = null;
		try {
			final String stringurl = online.version.remote;
			final String stringlocal = online.version.local;
			final String local;
			if (!StringUtils.isEmpty(stringlocal))
				local = stringlocal;
			else
				local = stringurl.substring(stringurl.lastIndexOf("/")+1, stringurl.length());

			ChatBuilder.create("signpic.versioning.startingDownload").setParams(local).useTranslation().useJson().chatClient();

			OverlayFrame.instance.pane.addNotice1(I18n.format("signpic.gui.notice.downloading", local), 2f);

			state.downloading = true;

			final HttpUriRequest req = new HttpGet(new URI(online.version.remote));
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			this.status.getProgress().overall = entity.getContentLength();
			input = entity.getContent();

			final File f = new File(Client.location.modDir, local+".dl");
			f.createNewFile();
			final File f1 = new File(Client.location.modDir, local);

			output = new CountingOutputStream(new FileOutputStream(f)) {
				@Override
				protected void afterWrite(final int n) throws IOException {
					if (ModDownload.this.canceled) {
						req.abort();
						throw new CommunicateCanceledException();
					}
					ModDownload.this.status.getProgress().setDone(getByteCount());
				}
			};

			IOUtils.copyLarge(input, output);
			IOUtils.closeQuietly(output);
			FileUtils.deleteQuietly(f1);
			if (!f1.exists())
				FileUtils.moveFile(f, f1);

			IChatComponent chat;
			if (Client.location.modFile.isFile())
				chat = ChatBuilder.create("signpic.versioning.doneDownloadingWithFile").useTranslation().setId(897).setParams(local, Client.location.modFile.getName()).setStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)).build();
			else
				chat = ChatBuilder.create("signpic.versioning.doneDownloading").useTranslation().setId(897).setParams(local).setStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)).build();
			OverlayFrame.instance.pane.addNotice1(I18n.format("signpic.gui.notice.downloaded", local), 2f);

			Desktop.getDesktop().open(Client.location.modDir.getCanonicalFile());
			state.downloading = false;
			state.downloadedFile = f1;

			this.result = new ModDLResult(chat);
			onDone(new CommunicateResponse(true, null));
			return;
		} catch (final Throwable e) {
			Reference.logger.warn("Updater Downloading Error", e);
			final IChatComponent chat = new ChatBuilder().setChat(new ChatComponentTranslation("signpic.versioning.error").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))).build();
			this.result = new ModDLResult(chat);
			onDone(new CommunicateResponse(false, e));
			return;
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	@Override
	public State getState() {
		return this.status;
	}

	@Override
	public void cancel() {
		this.canceled = true;
	}

	public static class ModDLResult {
		public final IChatComponent response;

		public ModDLResult(final IChatComponent response) {
			this.response = response;
		}
	}
}