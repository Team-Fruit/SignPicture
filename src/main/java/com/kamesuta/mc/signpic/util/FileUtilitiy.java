package com.kamesuta.mc.signpic.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.http.upload.UploadApiUtil;
import com.kamesuta.mc.signpic.http.upload.UploadRequest;
import com.kamesuta.mc.signpic.state.State;

import net.minecraft.client.resources.I18n;

public class FileUtilitiy {
	public static boolean transfer(final Transferable transferable) {
		try {
			if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
				GuiMain.setContentId((String) transferable.getTransferData(DataFlavor.stringFlavor));
			else if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				final List<?> droppedFiles = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
				for (final Object obj : droppedFiles)
					if (obj instanceof File) {
						final File file = (File) obj;
						UploadApiUtil.upload(UploadRequest.fromFile(file, new State()));
					}
			} else if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
				final BufferedImage bi = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
				try {
					final File tmp = Client.location.createCache("paste");
					ImageIO.write(bi, "png", tmp);
					UploadApiUtil.upload(UploadRequest.fromFile(tmp, new State()), new Runnable() {
						@Override
						public void run() {
							FileUtils.deleteQuietly(tmp);
						}
					});
				} catch (final IOException e) {
					Client.notice(I18n.format("signpic.gui.notice.paste.error", e), 2);
					return false;
				}
			} else {
				Client.notice(I18n.format("signpic.gui.notice.paste.typeunsupported"), 2);
				return false;
			}
		} catch (final IOException e) {
			Client.notice(I18n.format("signpic.gui.notice.paste.error", e), 2);
			return false;
		} catch (final UnsupportedFlavorException e) {
			Client.notice(I18n.format("signpic.gui.notice.paste.unsupported", e), 2);
		}
		return true;
	}
}
