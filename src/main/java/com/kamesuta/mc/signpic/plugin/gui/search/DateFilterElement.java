package com.kamesuta.mc.signpic.plugin.gui.search;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.plugin.SignData;

public abstract class DateFilterElement implements IFilterElement {

	public final @Nonnull Date src;

	public DateFilterElement(final Date src) {
		this.src = src;
	}

	public abstract boolean filter(SignData data, DateFilterProperty property, Date date);

	protected @Nullable Date get(final SignData data, final DateFilterProperty property) {
		switch (property) {
			case CREATE:
				return data.getCreateDate();
			case UPDATE:
				return data.getUpdateDate();
			default:
				return null;
		}

	}

	public static class AfterDateFilterElement extends DateFilterElement {

		public AfterDateFilterElement(final Date src) {
			super(src);
		}

		@Override
		public boolean filter(final SignData data, final DateFilterProperty property, final Date date) {
			final Date src = get(data, property);
			if (src==null)
				return false;
			return src.compareTo(date)>=0;
		}

	}

	public static class BeforeDateFilterElement extends DateFilterElement {

		public BeforeDateFilterElement(final Date src) {
			super(src);
		}

		@Override
		public boolean filter(final SignData data, final DateFilterProperty property, final Date date) {
			final Date src = get(data, property);
			if (src==null)
				return false;
			return src.compareTo(date)<=0;
		}

	}

	public static enum DateFilterProperty {
		CREATE,
		UPDATE;
	}
}
