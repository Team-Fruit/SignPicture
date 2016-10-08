package com.kamesuta.mc.signpic.image;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.entry.content.ContentCapacityOverException;
import com.kamesuta.mc.signpic.entry.content.ContentDownloader;
import com.kamesuta.mc.signpic.entry.content.ContentManager;
import com.kamesuta.mc.signpic.state.ContentStateType;

import net.minecraft.client.resources.I18n;

public class RemoteImage extends Image {
	protected ImageTextures texture;
	protected File local;

	public RemoteImage(final Content content) {
		super(content);
		this.local = Client.location.localLocation(content.id);
	}

	private int processing = 0;

	@Override
	public void onInit() {
		ContentManager.instance.asyncqueue.offer(this);
	}

	@Override
	public void onAsyncProcess() {
		try {
			new ContentDownloader(this.content, Client.location).onAsyncProcess();
			this.texture = new ImageIOLoader(this.content, Client.location).load();
			ContentManager.instance.divisionqueue.offer(this);
		} catch (final URISyntaxException e) {
			this.content.state.setType(ContentStateType.ERROR);
			this.content.state.setMessage(I18n.format("signpic.advmsg.invalidurl"));
		} catch (final ContentCapacityOverException e) {
			this.content.state.setType(ContentStateType.ERROR);
			this.content.state.setMessage(I18n.format("signpic.advmsg.capacityover"));
		} catch (final InvaildImageException e) {
			this.content.state.setType(ContentStateType.ERROR);
			this.content.state.setMessage(I18n.format("signpic.advmsg.invalidimage"));
		} catch (final IOException e) {
			this.content.state.setType(ContentStateType.ERROR);
			this.content.state.setMessage(I18n.format("signpic.advmsg.ioerror", e));
		} catch (final Exception e) {
			this.content.state.setType(ContentStateType.ERROR);
			this.content.state.setMessage(I18n.format("signpic.advmsg.unknown", e));
		}
	}

	@Override
	public boolean onDivisionProcess() {
		final List<ImageTexture> texs = this.texture.getAll();
		if (this.processing < (this.content.state.getProgress().overall = texs.size())) {
			final ImageTexture tex = texs.get(this.processing);
			tex.load();
			this.processing++;
			this.content.state.setType(ContentStateType.LOADING);
			this.content.state.getProgress().done = this.processing;
			return false;
		} else {
			this.content.state.setType(ContentStateType.AVAILABLE);
			this.content.state.getProgress().done = this.content.state.getProgress().overall;
			return true;
		}
	}

	@Override
	public void onCollect() {
		if (this.texture!=null)
			this.texture.delete();
	}

	@Override
	public IImageTexture getTexture() throws IllegalStateException {
		return getTextures().get();
	}

	public ImageTextures getTextures() {
		if (this.content.state.getType() == ContentStateType.AVAILABLE)
			return this.texture;
		else
			throw new IllegalStateException("Not Available");
	}

	@Override
	public String getLocal() {
		if (this.local != null)
			return "File:" + this.local.getName();
		else
			return "None";
	}

	@Override
	public String toString() {
		return String.format("RemoteImage[%s]", this.content.id);
	}
}
