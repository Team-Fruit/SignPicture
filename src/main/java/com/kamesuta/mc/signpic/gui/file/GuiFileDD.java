package com.kamesuta.mc.signpic.gui.file;

import static org.lwjgl.opengl.GL11.*;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

import org.lwjgl.opengl.Display;

import com.kamesuta.mc.bnnwidget.WBase;
import com.kamesuta.mc.bnnwidget.WEvent;
import com.kamesuta.mc.bnnwidget.component.MChatTextField;
import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Point;
import com.kamesuta.mc.bnnwidget.position.R;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.upload.GyazoUpload;
import com.kamesuta.mc.signpic.http.upload.GyazoUpload.GyazoResult;
import com.kamesuta.mc.signpic.render.RenderHelper;
import com.kamesuta.mc.signpic.state.Progress;

public class GuiFileDD extends WBase {
	protected final JDialog overlay;
	protected Area location;

	public GuiFileDD(final R position, final MChatTextField textField) {
		super(position);
		this.overlay = new JDialog();
		this.overlay.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.overlay.setBounds((Display.getX()+Display.getWidth()-200), (Display.getY()+Display.getHeight()-200), (200), (200));
		this.overlay.setTransferHandler(new TransferHandler() {
			@Override
			public boolean canImport(final TransferSupport support) {
				if (!support.isDrop()) {
					// ドロップ操作でない場合は受け取らない
					return false;
				}
				return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
			}

			/**
			 * ドロップされたファイルを受け取る
			 */
			@Override
			public boolean importData(final TransferSupport support) {
				// 受け取っていいものか確認する
				if (!canImport(support)) {
					return false;
				}

				// ドロップ処理
				final Transferable t = support.getTransferable();
				try {
					// ファイルを受け取る
					final List<?> files = (List<?>) t.getTransferData(DataFlavor.javaFileListFlavor);

					// テキストエリアに表示するファイル名リストを作成する
					for (final Object file : files){
						if (file instanceof File) {
							final File f = (File) file;
							Communicator.instance.communicate(new GyazoUpload(f, new Progress()), new ICommunicateCallback<GyazoResult>() {
								@Override
								public void onDone(final ICommunicateResponse<GyazoResult> res) {
									if (res.isSuccess()) {
										textField.setFocused(false);
										textField.setText(res.getResult().url);
										textField.setFocused(true);
									}
								}
							});
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		});
	}

	@Override
	public void onAdded() {
		this.overlay.setVisible(true);
		this.overlay.setAlwaysOnTop(true);
		this.overlay.setAlwaysOnTop(false);
	}

	@Override
	public void draw(final WEvent ev, final Area pgp, final Point p, final float frame) {
		RenderHelper.startShape();
		glColor4f(0f, 0f, 0f, .8f);
		drawRect(getGuiPosition(pgp));
	}

	@Override
	public boolean onCloseRequest() {
		this.overlay.dispose();
		return true;
	}
}
