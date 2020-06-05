package net.teamfruit.bnnwidget.util;

import java.util.Collection;

public interface NotifyCollection<E> extends Collection<E> {
	public int getModCount();
}
