package com.kamesuta.mc.signpic.plugin.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.util.NotifyCollections;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;

public class FilterParser {

	public static @Nonnull SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static @Nonnull IModCount<IFilterElement> parse(final String str) {
		final IModCount<IFilterElement> list = new NotifyCollections.NotifyArrayList<IFilterElement>();
		final String[] array = StringUtils.replaceChars(str, '\u3000', ' ').split(" ");
		IFilterElement before = null;
		for (int i = 0; i<array.length; i++) {
			final String line = array[i];
			if (line.equals("OR")&&before!=null&&i+1<array.length) {
				final IFilterElement element = parseElement(array[++i]);
				before = new OrFilterElement(before, element!=null ? element : FalseFilterElement.INSTANCE);
			} else
				before = parseElement(line);
			list.add(before!=null ? before : FalseFilterElement.INSTANCE);
		}
		return list;
	}

	private static @Nullable IFilterElement parseElement(String str) {
		String[] pair = str.split(":");
		if (str.contains(":")&&pair.length>=2) {
			if (pair.length>2) {
				final StringBuilder sb = new StringBuilder();
				for (int p = 1; p<pair.length; p++)
					sb.append(pair[p]);
				pair = new String[] { pair[0], sb.toString() };
			}
			final String key = pair[0];
			final String value = pair[1];
			if (key.equalsIgnoreCase("since")||key.equalsIgnoreCase("until")) {
				try {
					final Date date = dateFormat.parse(value);
					if (key.equalsIgnoreCase("since"))
						return new DateFilterElement.AfterDateFilterElement(DateFilterProperty.CREATE, date);
					else
						return new DateFilterElement.BeforeDateFilterElement(DateFilterProperty.CREATE, date);
				} catch (final ParseException e) { //NO-OP
				}
			} else {
				try {
					final StringFilterProperty property = StringFilterProperty.valueOf(key.toUpperCase());
					return new StringFilterElement.EqualsIgnoreCaseStringFilterElement(property, value);
				} catch (final IllegalArgumentException e) { //NO-OP
				}
			}
		} else if (str.startsWith("\"")&&str.endsWith("\"")) {
			str = StringUtils.removeStart(StringUtils.removeEnd(str, "\""), "\"");
			return new UniversalFilterElement.EqualsIgnoreCaseUniversalFilterElement(str);
		} else
			return new UniversalFilterElement.ContainsIgnoreCaseUniversalFilterElement(str);
		return null;
	}

}
