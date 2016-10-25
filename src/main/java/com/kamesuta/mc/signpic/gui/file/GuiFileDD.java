package com.kamesuta.mc.signpic.gui.file;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.upload.GyazoUpload;
import com.kamesuta.mc.signpic.http.upload.GyazoUpload.GyazoResult;
import com.kamesuta.mc.signpic.state.State;

public class GuiFileDD {
	public static final GuiFileDD instance = new GuiFileDD();

	protected final JDialog overlay;
	protected Area location;

	private GuiFileDD() {
		this.overlay = new JDialog();
		this.overlay.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.overlay.setName("SignPictureGui");
		this.overlay.setTransferHandler(new TransferHandler() {
			@Override
			public boolean canImport(final TransferSupport support) {
				if (!support.isDrop())
					// ドロップ操作でない場合は受け取らない
					return false;
				return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
			}

			/**
			 * ドロップされたファイルを受け取る
			 */
			@Override
			public boolean importData(final TransferSupport support) {
				// 受け取っていいものか確認する
				if (!canImport(support))
					return false;

				// ドロップ処理
				final Transferable t = support.getTransferable();
				try {
					// ファイルを受け取る
					final List<?> files = (List<?>) t.getTransferData(DataFlavor.javaFileListFlavor);

					// テキストエリアに表示するファイル名リストを作成する
					for (final Object file : files)
						if (file instanceof File) {
							final File f = (File) file;
							final GyazoUpload upload = new GyazoUpload(f, new State("up"));
							upload.getState().getMeta().put("gui.showbar", 3);
							upload.getState().getMeta().put("gui.highlight", true);
							Communicator.instance.communicate(upload, new ICommunicateCallback<GyazoResult>() {
								@Override
								public void onDone(final ICommunicateResponse<GyazoResult> res) {
									if (res.isSuccess()) {
										//										textField.setFocused(false);
										//										textField.setText(res.getResult().url);
										//										textField.setFocused(true);
									}
								}
							});
						}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
		this.overlay.setAlwaysOnTop(true);
		this.overlay.setSize(200, 200);
		this.overlay.setLocationRelativeTo(null);
	}

	public boolean isVisible() {
		return this.overlay.isVisible();
	}

	public void setVisible(final boolean b) {
		if (b)
			this.overlay.setSize(200, 200);
		this.overlay.setVisible(b);
	}
}
