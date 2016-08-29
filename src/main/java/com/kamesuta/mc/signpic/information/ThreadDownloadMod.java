package com.kamesuta.mc.signpic.information;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.compress.utils.IOUtils;

import com.kamesuta.mc.signpic.Reference;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class ThreadDownloadMod extends Thread {

	public ThreadDownloadMod() {
		setName("Sign Picture Download File Thread");

		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			final String stringurl = InformationChecker.onlineVersionRemote;
			final String stringlocal = InformationChecker.onlineVersionLocal;
			final String local;
			if (stringlocal!=null && !stringlocal.isEmpty())
				local = stringlocal;
			else
				local = stringurl.substring(stringurl.lastIndexOf("/")+1, stringurl.length());

			sendChat(IChatComponent.Serializer.func_150699_a(String.format(StatCollector.translateToLocal("signpic.versioning.startingDownload"), local)));

			InformationChecker.startedDownload = true;

			final URL url = new URL(InformationChecker.onlineVersionRemote);
			final InputStream webReader = url.openStream();

			final File dir = new File(".", "mods");
			final File f = new File(dir, local + ".dl");
			f.createNewFile();

			final FileOutputStream outputStream = new FileOutputStream(f);

			IOUtils.copy(webReader, outputStream);

			outputStream.close();
			webReader.close();

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
				sendChat(new ChatComponentTranslation("signpic.versioning.doneDownloadingWithFile", local, fname).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
			else
				sendChat(new ChatComponentTranslation("signpic.versioning.doneDownloading", local).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));

			Desktop.getDesktop().open(dir.getCanonicalFile());
			InformationChecker.downloadedFile = true;

			finalize();
		} catch(final Throwable e) {
			Reference.logger.warn("Updater Downloading Error", e);
			sendError();
			try {
				finalize();
			} catch(final Throwable e1) {
				Reference.logger.error("updater.finalizeerror", e1);
			}
		}
	}

	private void sendError() {
		sendChat(new ChatComponentTranslation("signpic.versioning.error").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
	}

	private void sendChat(final IChatComponent c) {
		if(FMLClientHandler.instance().getClient().thePlayer != null)
			FMLClientHandler.instance().getClient().thePlayer.addChatComponentMessage(c);
	}
}