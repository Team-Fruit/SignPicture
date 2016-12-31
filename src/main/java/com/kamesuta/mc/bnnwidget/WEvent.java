package com.kamesuta.mc.bnnwidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;

public class WEvent {
	public final @Nonnull WFrame owner;
	public final @Nonnull Map<String, Object> data;
	public final @Nonnull Map<String, List<WActionListener>> events;

	public WEvent(final @Nonnull WFrame owner) {
		this.owner = owner;
		this.data = new HashMap<String, Object>();
		this.events = new HashMap<String, List<WActionListener>>();
	}

	public void addEventListener(final @Nonnull String command, final @Nonnull WActionListener listener) {
		Validate.notNull(command);
		Validate.notNull(listener);
		final List<WActionListener> actions = getActionsOrCreate(command);
		actions.add(listener);
		this.events.put(command, actions);
	}

	public @Nullable List<WActionListener> removeEvent(final @Nonnull String command) {
		return this.events.remove(command);
	}

	public boolean removeActionListener(final @Nonnull String command, final @Nonnull WActionListener listener) {
		Validate.notNull(command);
		Validate.notNull(listener);
		final List<WActionListener> actions = this.events.get(command);
		if (actions!=null)
			return actions.remove(listener);
		return false;
	}

	protected @Nonnull List<WActionListener> getActionsOrCreate(final @Nonnull String command) {
		final List<WActionListener> actions = this.events.get(command);
		if (actions!=null)
			return actions;
		else
			return Lists.newArrayList();
	}

	public void eventDispatch(final @Nullable String command, final @Nonnull Object... params) {
		if (command!=null) {
			final List<WActionListener> actions = this.events.get(command);
			actionsDispatch(actions, command, params);
		}
	}

	protected void actionsDispatch(final @Nullable List<WActionListener> actions, final @Nonnull String command, final @Nonnull Object... params) {
		if (actions!=null)
			for (final WActionListener action : actions)
				actionDispatch(action, command, params);
	}

	protected void actionDispatch(final @Nullable WActionListener action, final @Nonnull String command, final @Nonnull Object... params) {
		if (action!=null)
			action.actionPerformed(command, params);
	}
}
