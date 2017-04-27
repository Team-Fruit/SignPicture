package com.kamesuta.mc.signpic.plugin.gui.search;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.time.DateUtils;

import com.kamesuta.mc.signpic.plugin.SignData;

public abstract class DateFilterElement extends EnumFilterElement<Date, DateFilterProperty> {

	public final @Nonnull Date date;

	public DateFilterElement(final DateFilterProperty property, final Date src) {
		super(property);
		this.date = src;
	}

	public static class EqualsDateFilterElement extends DateFilterElement {

		public EqualsDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			final Date src = this.property.get(data);
			if (src==null)
				return false;
			return src.equals(this.date);
		}

	}

	public static class HourEqualsDateFilterElement extends DateFilterElement {

		public HourEqualsDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			final Date src = this.property.get(data);
			if (src==null)
				return false;
			return DateUtils.truncate(src, Calendar.HOUR_OF_DAY).equals(DateUtils.truncate(this.date, Calendar.HOUR_OF_DAY));
		}

	}

	public static class DayEqualsDateFilterElement extends DateFilterElement {

		public DayEqualsDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			final Date src = this.property.get(data);
			if (src==null)
				return false;
			return DateUtils.truncate(src, Calendar.DAY_OF_MONTH).equals(DateUtils.truncate(this.date, Calendar.DAY_OF_MONTH));
		}

	}

	public static class MonthEqualsDateFilterElement extends DateFilterElement {

		public MonthEqualsDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			final Date src = this.property.get(data);
			if (src==null)
				return false;
			return DateUtils.truncate(src, Calendar.MONTH).equals(DateUtils.truncate(this.date, Calendar.MONTH));
		}

	}

	public static class YearEqualsDateFilterElement extends DateFilterElement {

		public YearEqualsDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			final Date src = this.property.get(data);
			if (src==null)
				return false;
			return DateUtils.truncate(src, Calendar.YEAR).equals(DateUtils.truncate(this.date, Calendar.YEAR));
		}

	}

	public static class AfterDateFilterElement extends DateFilterElement {

		public AfterDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			final Date src = this.property.get(data);
			if (src==null)
				return false;
			return src.compareTo(this.date)>=0;
		}

	}

	public static class BeforeDateFilterElement extends DateFilterElement {

		public BeforeDateFilterElement(final DateFilterProperty property, final Date src) {
			super(property, src);
		}

		@Override
		public boolean filter(final SignData data) {
			final Date src = this.property.get(data);
			if (src==null)
				return false;
			return src.compareTo(this.date)<=0;
		}

	}

}
