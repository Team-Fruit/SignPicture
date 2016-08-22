package com.kamesuta.mc.guiwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

public class GuiEvent {
	public final Map<String, List<GuiActionListener>> events;

	public GuiEvent() {
		this.events = new HashMap<String, List<GuiActionListener>>();
	}

	public void addEventListener(final String command, final GuiActionListener listener) {
		Validate.notNull(command);
		Validate.notNull(listener);
		final List<GuiActionListener> actions = getActionsOrCreate(command);
		actions.add(listener);
		this.events.put(command, actions);
	}

	public List<GuiActionListener> removeEvent(final String command) {
		return this.events.remove(command);
	}

	public boolean removeActionListener(final String command, final GuiActionListener listener) {
		Validate.notNull(command);
		Validate.notNull(listener);
		final List<GuiActionListener> actions = this.events.get(command);
		if (actions != null)
			return actions.remove(listener);
		return false;
	}

	protected List<GuiActionListener> getActionsOrCreate(final String command) {
		final List<GuiActionListener> actions = this.events.get(command);
		if (actions != null)
			return actions;
		else
			return new ArrayList<GuiActionListener>();
	}

	public void eventDispatch(final String command, final Object... params) {
		if (command != null) {
			final List<GuiActionListener> actions = this.events.get(command);
			actionsDispatch(actions, command, params);
		}
	}

	protected void actionsDispatch(final List<GuiActionListener> actions, final String command, final Object... params) {
		if (actions != null) {
			for (final GuiActionListener action : actions) {
				actionDispatch(action, command, params);
			}
		}
	}

	protected void actionDispatch(final GuiActionListener action, final String command, final Object... params) {
		if (action != null)
			action.actionPerformed(command, params);
	}
}
