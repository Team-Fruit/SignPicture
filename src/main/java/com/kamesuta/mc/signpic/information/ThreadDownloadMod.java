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

import com.kamesuta.mc.signpic.Reference;
import com.kamesuta.mc.signpic.util.ChatBuilder;
import com.kamesuta.mc.signpic.util.Downloader;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
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
		try {
			final String stringurl = InformationChecker.onlineVersion.remote;
			final String stringlocal = InformationChecker.onlineVersion.local;
			final String local;
			if (!StringUtils.isEmpty(stringlocal))
				local = stringlocal;
			else
				local = stringurl.substring(stringurl.lastIndexOf("/")+1, stringurl.length());

			ChatBuilder.create("signpic.versioning.startingDownload").setParams(local).useTranslation().useJson().chatClient();

			InformationChecker.startedDownload = true;

			final HttpUriRequest req = new HttpGet(new URI(InformationChecker.onlineVersion.remote));
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			final long size = entity.getContentLength();
			final InputStream inputStream = entity.getContent();

			final File dir = new File(".", "mods");
			final File f = new File(dir, local + ".dl");
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

			final File f1 = new File(dir, local);
			if(!f1.exists())
				f.renameTo(f1);

			String fname = null;
			final ModContainer c = Loader.instance().getIndexedModList().get(Reference.MODID);
			if (c != null) {
				final File m = c.getSource();
				if (m.isFile())
					fname = m.getName();
			}
			if (fname != null)
				new ChatBuilder().setId(897).setChat(new ChatComponentTranslation("signpic.versioning.doneDownloadingWithFile", local, fname).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))).chatClient();
			else
				new ChatBuilder().setId(897).setChat(new ChatComponentTranslation("signpic.versioning.doneDownloading", local).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN))).chatClient();

			Desktop.getDesktop().open(dir.getCanonicalFile());
			InformationChecker.downloadedFile = true;

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