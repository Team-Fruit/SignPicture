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

	public static @Nonnull IModCount<IFilterElement> parse(String str) {
		IModCount<IFilterElement> list = new NotifyCollections.NotifyArrayList<IFilterElement>();
		String[] array = StringUtils.replaceChars(str, '\u3000', ' ').split(" ");
		IFilterElement before = null;
		for (int i = 0; i<array.length; i++) {
			String line = array[i];
			if (line.equals("OR")&&before!=null&&i+1<array.length)
				before = new OrFilterElement(before, parseElement(array[++i]));
			else
				before = parseElement(line);
			if (before!=null)
				list.add(before);
		}
		return list;
	}

	private static @Nullable IFilterElement parseElement(String str) {
		if (str.contains(":")) {
			String[] pair = str.split(":");
			if (pair.length>2) {
				StringBuilder sb = new StringBuilder();
				for (int p = 1; p<pair.length; p++)
					sb.append(pair[p]);
				pair = new String[] { pair[0], sb.toString() };
			}
			String key = pair[0];
			String value = pair[1];
			if (key.equalsIgnoreCase("since")||key.equalsIgnoreCase("until")) {
				try {
					Date date = dateFormat.parse(value);
					if (key.equalsIgnoreCase("since"))
						return new DateFilterElement.AfterDateFilterElement(DateFilterProperty.CREATE, date);
					else
						return new DateFilterElement.BeforeDateFilterElement(DateFilterProperty.CREATE, date);
				} catch (ParseException e) { //NO-OP
				}
			} else {
				StringFilterProperty property = StringFilterProperty.valueOf(key.toUpperCase());
				if (property!=null)
					return new StringFilterElement.EqualsIgnoreCaseStringFilterElement(property, value);
			}
		} else if (str.startsWith("\"")&&str.endsWith("\"")) {
			str = StringUtils.removeStart(StringUtils.removeEnd(str, "\""), "\"");
			return new UniversalFilterElement.EqualsIgnoreCaseUniversalFilterElement(str);
		} else
			return new UniversalFilterElement.ContainsIgnoreCaseUniversalFilterElement(str);
		return null;
	}

}
