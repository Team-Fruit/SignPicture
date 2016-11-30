package com.kamesuta.mc.signpic.gui.file;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Apis.ImageUploaderFactory;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.gui.GuiMain;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.upload.IUploader;
import com.kamesuta.mc.signpic.http.upload.UploadContent;
import com.kamesuta.mc.signpic.state.State;

import net.minecraft.client.resources.I18n;

public class FileUtilitiy {
	public static boolean upload(final UploadContent content, final Runnable onDone) {
		try {
			final ImageUploaderFactory factory = getUploaderFactory();
			final String key = getKey(factory);
			if (factory!=null&&key!=null) {
				content.getPendingState().getMeta().put(GuiTask.HighlightPanel, true);
				content.getPendingState().getMeta().put(GuiTask.ShowPanel, 3);
				final IUploader upload = factory.create(content, key);
				upload.setCallback(new ICommunicateCallback() {
					@Override
					public void onDone(final ICommunicateResponse res) {
						if (upload.getLink()!=null) {
							final String url = upload.getLink();
							if (!GuiMain.setContentId(url))
								Client.notice(I18n.format("signpic.gui.notice.uploaded", content.getName()), 2);
							if (onDone!=null)
								onDone.run();
						}
						if (!res.isSuccess())
							Client.notice(I18n.format("signpic.gui.notice.uploadfailed", res.getError()), 2);
					}
				});
				Communicator.instance.communicate(upload);
			}
			return true;
		} catch (final IOException e) {
			Client.notice(I18n.format("signpic.gui.notice.uploadfailed", e), 2);
		}
		return false;
	}

	public static boolean transfer(final Transferable transferable) {
		try {
			if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
				GuiMain.setContentId((String) transferable.getTransferData(DataFlavor.stringFlavor));
			else if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				final List<?> droppedFiles = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
				for (final Object obj : droppedFiles)
					if (obj instanceof File) {
						final File file = (File) obj;
						FileUtilitiy.upload(UploadContent.fromFile(file, new State()));
					}
			} else if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
				final BufferedImage bi = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
				try {
					final File tmp = Client.location.createCache("paste");
					ImageIO.write(bi, "png", tmp);
					FileUtilitiy.upload(UploadContent.fromFile(tmp, new State()), new Runnable() {
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

	public static boolean upload(final UploadContent content) {
		return upload(content, null);
	}

	public static ImageUploaderFactory getUploaderFactory() {
		return Apis.instance.imageUploaders.solve(Apis.instance.imageUploaders.getConfigOrRandom());
	}

	public static String getKey(final ImageUploaderFactory factory) {
		return new Apis.KeySetting(factory.keys()).getConfigOrRandom();
	}
}
