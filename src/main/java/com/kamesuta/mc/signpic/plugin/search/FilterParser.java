package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.util.NotifyCollections;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;

public class FilterParser {

	public final @Nonnull String str;

	public FilterParser(String str) {
		this.str = str;
	}

	public IModCount<IFilterElement> parse() {
		IModCount<IFilterElement> list = new NotifyCollections.NotifyArrayList<IFilterElement>();
		String[] array = StringUtils.split(StringUtils.replaceChars(str, '\u3000', ' '), StringUtils.SPACE);
		for (String line : array) {
			//TODO
		}
		return list;
	}

	public static IModCount<IFilterElement> parse(String str) {
		return new FilterParser(str).parse();
	}
}
