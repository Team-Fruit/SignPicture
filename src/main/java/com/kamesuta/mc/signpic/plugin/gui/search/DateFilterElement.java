package com.kamesuta.mc.signpic.plugin.gui.search;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.plugin.SignData;
import com.kamesuta.mc.signpic.plugin.gui.search.DateFilterElement.DateFilterProperty;

public abstract class DateFilterElement extends EnumFilterElement<DateFilterProperty> {

	public final @Nonnull Date src;

	public DateFilterElement(final DateFilterProperty property, final Date src) {
		super(property);
		this.src = src;
	}

	public abstract boolean filter(SignData data, Date date);

	protected @Nullable Date get(final SignData data) {
		switch (this.property) {
			case CREATE:
				return data.getCreateDate();
			case UPDATE:
				return data.getUpdateDate();
			default:
				return null;
		}

	}

	public static class AfterDateFilterElement extends DateFilterElement {

		public AfterDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data, final Date date) {
			final Date src = get(data);
			if (src==null)
				return false;
			return src.compareTo(date)>=0;
		}

	}

	public static class BeforeDateFilterElement extends DateFilterElement {

		public BeforeDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data, final Date date) {
			final Date src = get(data);
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
