package com.kamesuta.mc.signpic.gui.file;

import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.http.upload.UploadContent;
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
	protected void transfer(final Transferable transferable) {
		if (FileUtilitiy.transfer(transferable))
			if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE))
				close();
	}

	@Override
	protected void apply(final File f) {
		if (FileUtilitiy.upload(UploadContent.fromFile(f, new State())))
			if (!CurrentMode.instance.isState(CurrentMode.State.CONTINUE))
				close();
	}

	public void setVisible(final boolean b) {
		if (b)
			requestOpen();
		else
			requestClose();
	}
}