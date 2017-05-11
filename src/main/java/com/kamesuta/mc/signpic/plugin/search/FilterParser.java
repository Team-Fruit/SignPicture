package com.kamesuta.mc.signpic.plugin.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.util.NotifyCollections;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;

public class FilterParser {

	public static @Nonnull SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public final @Nonnull String str;

	public FilterParser(String str) {
		this.str = str;
	}

	public IModCount<IFilterElement> parse() {
		IModCount<IFilterElement> list = new NotifyCollections.NotifyArrayList<IFilterElement>();
		String[] array = StringUtils.replaceChars(str, '\u3000', ' ').split(" ");
		for (int i = 0; i<array.length; i++) {
			String line = array[i];
			if (line.contains(":")) {
				String[] pair = line.split(":");
				if (pair.length>2) {
					StringBuilder sb = new StringBuilder();
					for (int p = 1; i<pair.length; p++)
						sb.append(pair[p]);
					pair = new String[] { pair[0], sb.toString() };
				}
				String key = pair[0];
				String value = pair[1];
				if (key.equalsIgnoreCase("since")||key.equalsIgnoreCase("until")) {
					try {
						Date date = dateFormat.parse(value);
						if (key.equalsIgnoreCase("since"))
							list.add(new DateFilterElement.AfterDateFilterElement(DateFilterProperty.CREATE, date));
						else
							list.add(new DateFilterElement.BeforeDateFilterElement(DateFilterProperty.CREATE, date));
					} catch (ParseException e) { //NO-OP
					}
				} else {
					StringFilterProperty property = StringFilterProperty.valueOf(key.toUpperCase());
					if (property!=null)
						list.add(new StringFilterElement.EqualsIgnoreCaseStringFilterElement(property, value));
				}
			} else if (line.startsWith("\"")&&line.endsWith("\"")) {
				line = StringUtils.removeStart(StringUtils.removeEnd(line, "\""), "\"");
				list.add(new UniversalFilterElement.EqualsIgnoreCaseUniversalFilterElement(line));
			} else {
				list.add(new UniversalFilterElement.ContainsIgnoreCaseUniversalFilterElement(line));
			}
		}
		return list;
	}

	public static IModCount<IFilterElement> parse(String str) {
		return new FilterParser(str).parse();
	}
}
