package com.kamesuta.mc.signpic.plugin.search;

import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.NotifyArrayList;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public class ImplFilterExpression implements FilterExpression {

	private final @Nonnull IModCount<IFilterElement> elements;

	protected final @Nonnull IModCount<SignData> datas;

	public ImplFilterExpression(final @Nonnull IModCount<SignData> datas) {
		this.datas = datas;
		this.elements = new NotifyArrayList<IFilterElement>();
	}

	public ImplFilterExpression(final @Nonnull IModCount<SignData> datas, final IModCount<IFilterElement> elements) {
		this.datas = datas;
		this.elements = elements;
	}

	protected void add(final IFilterElement date) {
		this.elements.add(date);
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#deq(com.kamesuta.mc.signpic.plugin.search.DateFilterProperty, java.util.Date)
	 */
	@Override
	public FilterExpression deq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.EqualsDateFilterElement(property, date));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#dheq(com.kamesuta.mc.signpic.plugin.search.DateFilterProperty, java.util.Date)
	 */
	@Override
	public FilterExpression dheq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.HourEqualsDateFilterElement(property, date));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#ddeq(com.kamesuta.mc.signpic.plugin.search.DateFilterProperty, java.util.Date)
	 */
	@Override
	public FilterExpression ddeq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.DayEqualsDateFilterElement(property, date));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#dmeq(com.kamesuta.mc.signpic.plugin.search.DateFilterProperty, java.util.Date)
	 */
	@Override
	public FilterExpression dmeq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.MonthEqualsDateFilterElement(property, date));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#dyeq(com.kamesuta.mc.signpic.plugin.search.DateFilterProperty, java.util.Date)
	 */
	@Override
	public FilterExpression dyeq(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.YearEqualsDateFilterElement(property, date));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#da(com.kamesuta.mc.signpic.plugin.search.DateFilterProperty, java.util.Date)
	 */
	@Override
	public FilterExpression da(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.AfterDateFilterElement(property, date));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#db(com.kamesuta.mc.signpic.plugin.search.DateFilterProperty, java.util.Date)
	 */
	@Override
	public FilterExpression db(final DateFilterProperty property, final Date date) {
		add(new DateFilterElement.BeforeDateFilterElement(property, date));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#eq(com.kamesuta.mc.signpic.plugin.search.StringFilterProperty, java.lang.String)
	 */
	@Override
	public FilterExpression eq(final StringFilterProperty property, final String str) {
		add(new StringFilterElement.EqualsStringFilterElement(property, str));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#ieq(com.kamesuta.mc.signpic.plugin.search.StringFilterProperty, java.lang.String)
	 */
	@Override
	public FilterExpression ieq(final StringFilterProperty property, final String str) {
		add(new StringFilterElement.EqualsIgnoreCaseStringFilterElement(property, str));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#con(com.kamesuta.mc.signpic.plugin.search.StringFilterProperty, java.lang.String)
	 */
	@Override
	public FilterExpression con(final StringFilterProperty property, final String str) {
		add(new StringFilterElement.ContainsStringFilterElement(property, str));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#icon(com.kamesuta.mc.signpic.plugin.search.StringFilterProperty, java.lang.String)
	 */
	@Override
	public FilterExpression icon(final StringFilterProperty property, final String str) {
		add(new StringFilterElement.ContainsIgnoreCaseStringFilterElement(property, str));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#peq(int, int, int)
	 */
	@Override
	public FilterExpression peq(final int x, final int y, final int z) {
		add(new PosFilterElement.EqualPosFilterElement(x, y, z));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#wp(int, int, int, int, int, int)
	 */
	@Override
	public FilterExpression wp(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
		add(new PosFilterElement.RangePosFilterElement.WithinRangePosFilterElement(x1, y1, z1, x2, y2, z2));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#op(int, int, int, int, int, int)
	 */
	@Override
	public FilterExpression op(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
		add(new PosFilterElement.RangePosFilterElement.OutRangePosFilterElement(x1, y1, z1, x2, y2, z2));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#ueq(java.lang.String)
	 */
	@Override
	public FilterExpression ueq(final String str) {
		add(new UniversalFilterElement.EqualsUniversalFilterElement(str));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#uieq(java.lang.String)
	 */
	@Override
	public FilterExpression uieq(final String str) {
		add(new UniversalFilterElement.EqualsIgnoreCaseUniversalFilterElement(str));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#ucon(java.lang.String)
	 */
	@Override
	public FilterExpression ucon(final String str) {
		add(new UniversalFilterElement.ContainsUniversalFilterElement(str));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#uicon(java.lang.String)
	 */
	@Override
	public FilterExpression uicon(final String str) {
		add(new UniversalFilterElement.ContainsIgnoreCaseUniversalFilterElement(str));
		return this;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#isFiltered()
	 */
	@Override
	public boolean isFiltered() {
		return !this.elements.isEmpty();
	}

	protected int modCache;
	protected @Nullable IModCount<SignData> findCache;

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#findList()
	 */
	@Override
	public IModCount<SignData> findList() {
		if (!isFiltered())
			return new NotifyArrayList<SignData>(this.datas);
		if (this.modCache==this.elements.getModCount()&&this.findCache!=null)
			return this.findCache;
		final IModCount<SignData> list = new NotifyArrayList<SignData>();
		for (final SignData data : this.datas) {
			final EntryId entry = EntryId.from(data.getSign());
			final ContentId content = entry.getContentId();
			if (content!=null)
				for (final IFilterElement element : this.elements) {
					if (element.filter(data, entry, content)) {
						list.add(data);
						break;
					}
				}
		}
		this.modCache = this.elements.getModCount();
		this.findCache = list;
		return list;
	}

	/* (非 Javadoc)
	 * @see com.kamesuta.mc.signpic.plugin.search.FilterExpression#findUnique()
	 */
	@Override
	public @Nullable SignData findUnique() {
		final IModCount<SignData> list = findList();
		if (list.isEmpty())
			return null;
		if (list.size()>1)
			throw new IllegalStateException();
		return list.iterator().next();
	}
}
