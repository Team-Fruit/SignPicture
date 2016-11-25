package com.kamesuta.mc.bnnwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

public class WEvent {
	public final WFrame owner;
	public final Map<String, Object> data;
	public final Map<String, List<WActionListener>> events;

	public WEvent(final WFrame owner) {
		this.owner = owner;
		this.data = new HashMap<String, Object>();
		this.events = new HashMap<String, List<WActionListener>>();
	}

	public void addEventListener(final String command, final WActionListener listener) {
		Validate.notNull(command);
		Validate.notNull(listener);
		final List<WActionListener> actions = getActionsOrCreate(command);
		actions.add(listener);
		this.events.put(command, actions);
	}

	public List<WActionListener> removeEvent(final String command) {
		return this.events.remove(command);
	}

	public boolean removeActionListener(final String command, final WActionListener listener) {
		Validate.notNull(command);
		Validate.notNull(listener);
		final List<WActionListener> actions = this.events.get(command);
		if (actions!=null)
			return actions.remove(listener);
		return false;
	}

	protected List<WActionListener> getActionsOrCreate(final String command) {
		final List<WActionListener> actions = this.events.get(command);
		if (actions!=null)
			return actions;
		else
			return new ArrayList<WActionListener>();
	}

	public void eventDispatch(final String command, final Object... params) {
		if (command!=null) {
			final List<WActionListener> actions = this.events.get(command);
			actionsDispatch(actions, command, params);
		}
	}

	protected void actionsDispatch(final List<WActionListener> actions, final String command, final Object... params) {
		if (actions!=null)
			for (final WActionListener action : actions)
				actionDispatch(action, command, params);
	}

	protected void actionDispatch(final WActionListener action, final String command, final Object... params) {
		if (action!=null)
			action.actionPerformed(command, params);
	}
}