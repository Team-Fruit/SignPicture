package com.kamesuta.mc.signpic.state;

import java.io.IOException;
import java.net.URISyntaxException;

import com.kamesuta.mc.signpic.entry.content.ContentCapacityOverException;
import com.kamesuta.mc.signpic.image.InvaildImageException;

import net.minecraft.client.resources.I18n;

public class State implements Progressable {
	private final String name;
	private final Progress progress = new Progress();
	private StateType type = StateType.INIT;
	private String message = "";

	public State(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Progress getProgress() {
		return this.progress;
	}

	public State setMessage(final String message) {
		this.message = message;
		return this;
	}

	public State setErrorMessage(final Throwable throwable) {
		if (throwable!=null) {
			setType(StateType.ERROR);
			try {
				throw throwable;
			} catch (final URISyntaxException e) {
				setMessage(I18n.format("signpic.advmsg.invalidurl"));
			} catch (final ContentCapacityOverException e) {
				setMessage(I18n.format("signpic.advmsg.capacityover"));
			} catch (final InvaildImageException e) {
				setMessage(I18n.format("signpic.advmsg.invalidimage"));
			} catch (final IOException e) {
				setMessage(I18n.format("signpic.advmsg.ioerror", e));
			} catch (final Throwable e) {
				setMessage(I18n.format("signpic.advmsg.unknown", e));
			}
		}
		return this;
	}

	public String getMessage() {
		return this.message;
	}

	public State setType(final StateType type) {
		this.type = type;
		return this;
	}

	public StateType getType() {
		return this.type;
	}

	public String getStateMessage() {
		return I18n.format(this.type.msg, (int) (this.progress.getProgress()*100));
	}
}
