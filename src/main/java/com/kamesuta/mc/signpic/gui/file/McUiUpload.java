package com.kamesuta.mc.signpic.gui.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kamesuta.mc.signpic.Apis;
import com.kamesuta.mc.signpic.Apis.ImageUploaderFactory;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.EntryIdBuilder;
import com.kamesuta.mc.signpic.gui.GuiSignPicEditor;
import com.kamesuta.mc.signpic.gui.GuiTask;
import com.kamesuta.mc.signpic.gui.OverlayFrame;
import com.kamesuta.mc.signpic.http.Communicator;
import com.kamesuta.mc.signpic.http.ICommunicate;
import com.kamesuta.mc.signpic.http.ICommunicateCallback;
import com.kamesuta.mc.signpic.http.ICommunicateResponse;
import com.kamesuta.mc.signpic.http.upload.UploadResult;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.state.State;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class McUiUpload extends UiUpload {
	public static final McUiUpload instance = new McUiUpload();

	@Override
	protected void initialize() {
		super.initialize();
		this.frame.setAlwaysOnTop(true);
	}

	@Override
	protected BufferedImage getImage(final String path) {
		try {
			return ImageIO.read(Client.mc.getResourceManager().getResource(new ResourceLocation("signpic", path)).getInputStream());
		} catch (final IOException e) {
		}
		return null;
	}

	@Override
	protected String getString(final String id) {
		return I18n.format(id);
	}

	@Override
	protected void apply(final File f) {
		try {
			final ImageUploaderFactory factory = Apis.instance.imageUploader.getConfigSetting();
			final String key = Apis.instance.imageUploaderKey.getConfigSetting();
			if (factory!=null&&key!=null) {
				final State state = new State("Upload");
				final ICommunicate<? extends UploadResult> upload = factory.create(f, state, key);
				state.getMeta().put(GuiTask.HighlightPanel, true);
				state.getMeta().put(GuiTask.ShowPanel, 3);
				Communicator.instance.communicate(upload, new ICommunicateCallback<UploadResult>() {
					@Override
					public void onDone(final ICommunicateResponse<? extends UploadResult> res) {
						if (res.getResult()!=null) {
							OverlayFrame.instance.pane.addNotice1(I18n.format("signpic.gui.notice.uploaded", f.getName()), 2);
							final String url = "";
							if (Client.mc.currentScreen instanceof GuiSignPicEditor) {
								final GuiSignPicEditor editor = (GuiSignPicEditor) Client.mc.currentScreen;
								editor.getTextField().setFocused(true);
								editor.getTextField().setText(url);
								editor.getTextField().setFocused(false);
							} else {
								final EntryId entryId = CurrentMode.instance.getEntryId();
								final EntryIdBuilder signbuilder = new EntryIdBuilder(entryId);
								signbuilder.setURI(url);
								CurrentMode.instance.setEntryId(signbuilder.build());
							}
						}
					}
				});
			}
			if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE))
				close();
		} catch (final FileNotFoundException e) {
		}
	}

	public void setVisible(final boolean b) {
		if (b)
			requestOpen();
		else
			requestClose();
	}
}