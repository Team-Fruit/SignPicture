package com.kamesuta.mc.signpic.plugin.search;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.motion.Easings;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections;
import com.kamesuta.mc.bnnwidget.util.NotifyCollections.IModCount;
import com.kamesuta.mc.signpic.attr.prop.AnimationData.RSNeed;

public class FilterParser {

	public static @Nonnull SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static @Nonnull IModCount<FilterElement> parse(final String str) {
		final IModCount<FilterElement> list = new NotifyCollections.NotifyArrayList<FilterElement>();
		final String[] array = StringUtils.replaceChars(str, '\u3000', ' ').split(" ");
		FilterElement before = null;
		for (int i = 0; i<array.length; i++) {
			final String line = array[i];
			if (line.equals("OR")&&before!=null&&i+1<array.length) {
				final FilterElement element = parseElement(array[++i]);
				before = new OrFilterElement(before, element!=null ? element : FalseFilterElement.INSTANCE);
			} else
				before = parseElement(line);
			list.add(before!=null ? before : FalseFilterElement.INSTANCE);
		}
		return list;
	}

	private static @Nullable FilterElement parseElement(String str) {
		String[] pair = str.split(":");
		if (str.contains(":")&&pair.length>=2) {
			if (pair.length>2) {
				final StringBuilder sb = new StringBuilder();
				for (int p = 1; p<pair.length; p++)
					sb.append(pair[p]);
				pair = new String[] { pair[0], sb.toString() };
			}
			final String key = pair[0].toUpperCase();
			final String value = pair[1];
			try {
				if (key.equalsIgnoreCase("since")||key.equalsIgnoreCase("until")) {
					final Date date = dateFormat.parse(value);
					if (key.equalsIgnoreCase("since"))
						return new DateFilterElement.AfterDateFilterElement(DateFilterProperty.CREATE, date);
					else
						return new DateFilterElement.BeforeDateFilterElement(DateFilterProperty.CREATE, date);
				} else if (StringUtils.containsIgnoreCase(key, "size")) {
					if (StringUtils.containsIgnoreCase(key, "less"))
						return new SizeDataAttrFilterElement.LessSizeDataAttrFilterElement(SizeDataAttrFilterProperty.SIZE, Float.valueOf(value));
					if (StringUtils.containsIgnoreCase(key, "greater"))
						return new SizeDataAttrFilterElement.GreaterSizeDataAttrFilterElement(SizeDataAttrFilterProperty.SIZE, Float.valueOf(value));
					return new SizeDataAttrFilterElement.EqualSizeDataAttrFilterElement(SizeDataAttrFilterProperty.SIZE, Float.valueOf(value));
				} else if (StringUtils.containsIgnoreCase(key, "offset")) {
					if (StringUtils.containsIgnoreCase(key, "less"))
						return new OffsetDataAttrFilterElement.LessOffsetDataAttrFilterElement(OffsetDataAttrFilterProperty.OFFSET, Float.valueOf(value));
					if (StringUtils.containsIgnoreCase(key, "greater"))
						return new OffsetDataAttrFilterElement.GreaterOffsetDataAttrFilterElement(OffsetDataAttrFilterProperty.OFFSET, Float.valueOf(value));
					return new OffsetDataAttrFilterElement.EqualOffsetDataAttrFilterElement(OffsetDataAttrFilterProperty.OFFSET, Float.valueOf(value));
				} else if (StringUtils.containsIgnoreCase(key, "animation")) {
					if (StringUtils.containsIgnoreCase(key, "easing"))
						return new AnimationDataFilterElement.EasingAnimationDataFilterElement(AnimationDataAttrFilterProperty.ANIMATION, Easings.fromString(value));
					if (StringUtils.containsIgnoreCase(key, "rs")||StringUtils.containsIgnoreCase(key, "rsneed"))
						return new AnimationDataFilterElement.RSNeedAnimationDataFilterElement(AnimationDataAttrFilterProperty.ANIMATION, RSNeed.valueOf(value));
					return new AnimationDataFilterElement.BooleanAnimationDataFilterElement(AnimationDataAttrFilterProperty.ANIMATION, BooleanUtils.toBoolean(value));
				} else if (StringUtils.containsIgnoreCase(key, "rotation"))
					return new BooleanRotationDataAttrFilterElement(RotationDataAttrFilterProperty.ROTATION, BooleanUtils.toBoolean(value));
				else
					return new StringFilterElement.EqualsIgnoreCaseStringFilterElement(StringFilterProperty.valueOf(key), value);
			} catch (final Exception e) { //NO-OP
			}
		} else if (str.startsWith("\"")&&str.endsWith("\"")) {
			str = StringUtils.removeStart(StringUtils.removeEnd(str, "\""), "\"");
			return new UniversalStringFilterElement.EqualsIgnoreCaseUniversalFilterElement(str);
		} else
			return new UniversalStringFilterElement.ContainsIgnoreCaseUniversalFilterElement(str);
		return null;
	}

}
