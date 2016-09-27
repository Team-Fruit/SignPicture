package com.kamesuta.mc.signpic.information;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.util.ChatBuilder;
import com.kamesuta.mc.signpic.util.Downloader;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class ThreadDownloadMod extends Thread {
	public ThreadDownloadMod() {
		setName("Sign Picture Download File Thread");

		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		final InformationChecker.InfoState state = InformationChecker.state;
		try {
			final String stringurl = state.onlineVersion.remote;
			final String stringlocal = state.onlineVersion.local;
			final String local;
			if (!StringUtils.isEmpty(stringlocal))
				local = stringlocal;
			else
				local = stringurl.substring(stringurl.lastIndexOf("/")+1, stringurl.length());

			ChatBuilder.create("signpic.versioning.startingDownload").setParams(local).useTranslation().useJson().chatClient();

			state.startedDownload = true;

			final HttpUriRequest req = new HttpGet(new URI(state.onlineVersion.remote));
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			final long size = entity.getContentLength();
			final InputStream inputStream = entity.getContent();

			final File f = new File(Client.location.modDir, local + ".dl");
			f.createNewFile();


			final OutputStream outputStream = new CountingOutputStream(new FileOutputStream(f)) {
				private long percent = 0;

				@Override
				protected synchronized void beforeWrite(final int n) {
					super.beforeWrite(n);
					final long now = getByteCount();
					final long percent = now * 100 / size;
					if (percent > this.percent) {
						this.percent = percent;
						ChatBuilder.create("signpic.versioning.downloading").setId(897).useTranslation().setParams(percent, now, size).chatClient();
					}
				}
			};

			IOUtils.copy(inputStream, outputStream);

			outputStream.close();
			inputStream.close();

			final File f1 = new File(Client.location.modDir, local);
			if(!f1.exists())
				f.renameTo(f1);

			if (Client.location.modFile.isFile())
				new ChatBuilder().setId(897).setChat(new ChatComponentTranslation("signpic.versioning.doneDownloadingWithFile", local, Client.location.modFile.getName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))).chatClient();
			else
				new ChatBuilder().setId(897).setChat(new ChatComponentTranslation("signpic.versioning.doneDownloading", local).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))).chatClient();

			Desktop.getDesktop().open(Client.location.modDir.getCanonicalFile());
			state.downloadedFile = true;

			finalize();
		} catch(final Throwable e) {
			Reference.logger.warn("Updater Downloading Error", e);
			new ChatBuilder().setChat(new ChatComponentTranslation("signpic.versioning.error").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED))).chatClient();
			try {
				finalize();
			} catch(final Throwable e1) {
				Reference.logger.error("updater.finalizeerror", e1);
			}
		}
	}
}