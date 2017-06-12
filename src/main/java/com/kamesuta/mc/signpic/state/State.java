package com.kamesuta.mc.signpic.state;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.kamesuta.mc.signpic.LoadCanceledException;
import com.kamesuta.mc.signpic.Log;
import com.kamesuta.mc.signpic.entry.content.ContentBlockedException;
import com.kamesuta.mc.signpic.entry.content.ContentCapacityOverException;
import com.kamesuta.mc.signpic.entry.content.RetryCountOverException;
import com.kamesuta.mc.signpic.image.InvaildImageException;

import net.minecraft.client.resources.I18n;

public class State {
	private @Nonnull String name = "";
	private @Nonnull Progress progress = new Progress();
	private @Nonnull StateType type = StateType.INIT;
	private @Nonnull String message = "";
	private final @Nonnull Map<String, Object> map = Maps.newHashMap();

	public @Nonnull State setName(final @Nonnull String name) {
		this.name = name;
		return this;
	}

	public @Nonnull String getName() {
		return this.name;
	}

	public @Nonnull State setProgress(final @Nonnull Progress progress) {
		this.progress = progress;
		return this;
	}

	public @Nonnull Progress getProgress() {
		return this.progress;
	}

	public @Nonnull State setMessage(final @Nonnull String message) {
		this.message = message;
		return this;
	}

	public @Nonnull Map<String, Object> getMeta() {
		return this.map;
	}

	public @Nonnull State setErrorMessage(final @Nullable Throwable throwable) {
		if (throwable!=null) {
			setType(StateType.ERROR);
			try {
				throw throwable;
			} catch (final URISyntaxException e) {
				setMessage(I18n.format("signpic.advmsg.invalidurl", e));
			} catch (final LoadCanceledException e) {
				setMessage(I18n.format("signpic.advmsg.loadstopped", e));
			} catch (final RetryCountOverException e) {
				setMessage(I18n.format("signpic.advmsg.retryover", e));
			} catch (final ContentCapacityOverException e) {
				setMessage(I18n.format("signpic.advmsg.capacityover", e));
			} catch (final ContentBlockedException e) {
				setMessage(I18n.format("signpic.advmsg.blocked", e));
			} catch (final InvaildImageException e) {
				setMessage(I18n.format("signpic.advmsg.invalidimage", e));
			} catch (final IOException e) {
				setMessage(I18n.format("signpic.advmsg.ioerror", e));
			} catch (final Throwable e) {
				setMessage(I18n.format("signpic.advmsg.unknown", e));
			}
			Log.dev.error(getMessage(), throwable);
		}
		return this;
	}

	public @Nonnull String getMessage() {
		return this.message;
	}

	public @Nonnull State setType(final @Nonnull StateType type) {
		this.type = type;
		return this;
	}

	public @Nonnull StateType getType() {
		return this.type;
	}

	public @Nonnull String getStateMessage() {
		return I18n.format(this.type.msg, (int) (this.progress.getProgress()*100));
	}
}
