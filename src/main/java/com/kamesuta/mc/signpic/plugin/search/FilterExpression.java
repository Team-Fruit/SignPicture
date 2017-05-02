package com.kamesuta.mc.signpic.plugin.search;

import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.NotifyArrayList;
import com.kamesuta.mc.signpic.plugin.SignData;

public class FilterExpression {

	private final @Nonnull IModCount<IFilterElement> elements;

	protected final @Nonnull List<SignData> datas;

	public FilterExpression(final @Nonnull List<SignData> datas) {
		this.datas = datas;
		this.elements = new NotifyArrayList<IFilterElement>();
	}

	public FilterExpression(final @Nonnull List<SignData> datas, final IModCount<IFilterElement> elements) {
		this.datas = datas;
		this.elements = elements;
	}

	protected void add(final IFilterElement date) {
		this.elements.add(date);
	}

	/**
	 * Date equals
	 * @param property
	 * @param date
	 * @return this
	 */
	public FilterExpression deq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.EqualsDateFilterElement(property, date));
		return this;
	}

	/**
	 * Date Hour equals
	 * @param property
	 * @param date
	 * @return
	 */
	public FilterExpression dheq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.HourEqualsDateFilterElement(property, date));
		return this;
	}

	/**
	 * Date Day equals
	 * @param property
	 * @param date
	 * @return
	 */
	public FilterExpression ddeq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.DayEqualsDateFilterElement(property, date));
		return this;
	}

	/**
	 * Date Month equals
	 * @param property
	 * @param date
	 * @return
	 */
	public FilterExpression dmeq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.MonthEqualsDateFilterElement(property, date));
		return this;
	}

	/**
	 * Date year equals
	 * @param property
	 * @param date
	 * @return
	 */
	public FilterExpression dyeq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.YearEqualsDateFilterElement(property, date));
		return this;
	}

	/**
	 * Date after
	 * @param property
	 * @param date
	 * @return
	 */
	public FilterExpression da(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.AfterDateFilterElement(property, date));
		return this;
	}

	/**
	 * Date before
	 * @param property
	 * @param date
	 * @return
	 */
	public FilterExpression db(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.BeforeDateFilterElement(property, date));
		return this;
	}

	/**
	 * String equals
	 * @param property
	 * @param str
	 * @return this
	 */
	public FilterExpression eq(final StringFilterProperty property, final String str) {
		add(new StringFilterElement.EqualsStringFilterElement(property, str));
		return this;
	}

	/**
	 * String equalsIgnoreCase
	 * @param property
	 * @param str
	 * @return this
	 */
	public FilterExpression ieq(final StringFilterProperty property, final String str) {
		add(new StringFilterElement.EqualsIgnoreCaseStringFilterElement(property, str));
		return this;
	}

	/**
	 * String contains
	 * @param property
	 * @param str
	 * @return this
	 */
	public FilterExpression con(final StringFilterProperty property, final String str) {
		add(new StringFilterElement.ContainsStringFilterElement(property, str));
		return this;
	}

	/**
	 * String containsIgnoreCase
	 * @param property
	 * @param str
	 * @return this
	 */
	public FilterExpression icon(final StringFilterProperty property, final String str) {
		add(new StringFilterElement.ContainsIgnoreCaseStringFilterElement(property, str));
		return this;
	}

	/**
	 * Block pos equals
	 * @param x
	 * @param y
	 * @param z
	 * @return this
	 */
	public FilterExpression peq(final int x, final int y, final int z) {
		add(new PosFilterElement.EqualPosFilterElement(x, y, z));
		return this;
	}

	/**
	 * Block pos within range
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return this
	 */
	public FilterExpression wp(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
		add(new PosFilterElement.RangePosFilterElement.WithinRangePosFilterElement(x1, y1, z1, x2, y2, z2));
		return this;
	}

	/**
	 * Block pos out range
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return
	 */
	public FilterExpression op(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
		add(new PosFilterElement.RangePosFilterElement.OutRangePosFilterElement(x1, y1, z1, x2, y2, z2));
		return this;
	}

	/**
	 * Universal equals
	 * @param property
	 * @param str
	 * @return this
	 */
	public FilterExpression ueq(final String str) {
		add(new UniversalFilterElement.EqualsUniversalFilterElement(str));
		return this;
	}

	/**
	 * Universal equalsIgnoreCase
	 * @param property
	 * @param str
	 * @return this
	 */
	public FilterExpression uieq(final String str) {
		add(new UniversalFilterElement.EqualsIgnoreCaseUniversalFilterElement(str));
		return this;
	}

	/**
	 * Universal contains
	 * @param property
	 * @param str
	 * @return this
	 */
	public FilterExpression ucon(final String str) {
		add(new UniversalFilterElement.ContainsUniversalFilterElement(str));
		return this;
	}

	/**
	 * Universal containsIgnoreCase
	 * @param property
	 * @param str
	 * @return this
	 */
	public FilterExpression uicon(final String str) {
		add(new UniversalFilterElement.ContainsIgnoreCaseUniversalFilterElement(str));
		return this;
	}

	public boolean isFiltered() {
		return !this.elements.isEmpty();
	}

	protected int modCache;
	protected @Nullable IModCount<SignData> findCache;

	/**
	 * 検索
	 * @return 条件に一致したデータリスト
	 */
	public IModCount<SignData> findList() {
		if (!isFiltered())
			return new NotifyArrayList<SignData>(this.datas);
		if (this.modCache==this.elements.getModCount()&&this.findCache!=null)
			return this.findCache;
		final IModCount<SignData> list = new NotifyArrayList<SignData>();
		for (final SignData data : this.datas) {
			for (final IFilterElement element : this.elements) {
				if (element.filter(data)) {
					list.add(data);
					break;
				}
			}
		}
		this.modCache = this.elements.getModCount();
		this.findCache = list;
		return list;
	}

	/**
	 * 検索
	 * @return 条件に一致したデータ
	 * @throws IllegalStateException 検索条件に一致する項目が複数存在する場合
	 */
	public @Nullable SignData findUnique() {
		final IModCount<SignData> list = findList();
		if (list.isEmpty())
			return null;
		if (list.size()>1)
			throw new IllegalStateException();
		return list.iterator().next();
	}
}
