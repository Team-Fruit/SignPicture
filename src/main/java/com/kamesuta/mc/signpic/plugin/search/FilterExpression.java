package com.kamesuta.mc.signpic.plugin.search;

import java.util.Date;

import javax.annotation.Nullable;

import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;
import com.kamesuta.mc.signpic.plugin.SignData;

public interface FilterExpression {

	/**
	 * Date equals
	 * @param property
	 * @param date
	 * @return this
	 */
	FilterExpression deq(DateFilterProperty property, Date date);

	/**
	 * Date Hour equals
	 * @param property
	 * @param date
	 * @return
	 */
	FilterExpression dheq(DateFilterProperty property, Date date);

	/**
	 * Date Day equals
	 * @param property
	 * @param date
	 * @return
	 */
	FilterExpression ddeq(DateFilterProperty property, Date date);

	/**
	 * Date Month equals
	 * @param property
	 * @param date
	 * @return
	 */
	FilterExpression dmeq(DateFilterProperty property, Date date);

	/**
	 * Date year equals
	 * @param property
	 * @param date
	 * @return
	 */
	FilterExpression dyeq(DateFilterProperty property, Date date);

	/**
	 * Date after
	 * @param property
	 * @param date
	 * @return
	 */
	FilterExpression da(DateFilterProperty property, Date date);

	/**
	 * Date before
	 * @param property
	 * @param date
	 * @return
	 */
	FilterExpression db(DateFilterProperty property, Date date);

	/**
	 * String equals
	 * @param property
	 * @param str
	 * @return this
	 */
	FilterExpression eq(StringFilterProperty property, String str);

	/**
	 * String equalsIgnoreCase
	 * @param property
	 * @param str
	 * @return this
	 */
	FilterExpression ieq(StringFilterProperty property, String str);

	/**
	 * String contains
	 * @param property
	 * @param str
	 * @return this
	 */
	FilterExpression con(StringFilterProperty property, String str);

	/**
	 * String containsIgnoreCase
	 * @param property
	 * @param str
	 * @return this
	 */
	FilterExpression icon(StringFilterProperty property, String str);

	/**
	 * Block pos equals
	 * @param x
	 * @param y
	 * @param z
	 * @return this
	 */
	FilterExpression peq(int x, int y, int z);

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
	FilterExpression wp(int x1, int y1, int z1, int x2, int y2, int z2);

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
	FilterExpression op(int x1, int y1, int z1, int x2, int y2, int z2);

	/**
	 * Universal equals
	 * @param property
	 * @param str
	 * @return this
	 */
	FilterExpression ueq(String str);

	/**
	 * Universal equalsIgnoreCase
	 * @param property
	 * @param str
	 * @return this
	 */
	FilterExpression uieq(String str);

	/**
	 * Universal contains
	 * @param property
	 * @param str
	 * @return this
	 */
	FilterExpression ucon(String str);

	/**
	 * Universal containsIgnoreCase
	 * @param property
	 * @param str
	 * @return this
	 */
	FilterExpression uicon(String str);

	boolean isFiltered();

	/**
	 * 検索
	 * @return 条件に一致したデータリスト
	 */
	IModCount<SignData> findList();

	/**
	 * 検索
	 * @return 条件に一致したデータ
	 * @throws IllegalStateException 検索条件に一致する項目が複数存在する場合
	 */
	@Nullable
	SignData findUnique();

}