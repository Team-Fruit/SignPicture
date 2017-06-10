package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public abstract class UniversalFilterElement implements DataFilterElement {
	public final @Nullable String str;

	public UniversalFilterElement(final String src) {
		this.str = src;
	}

	@Override
	public boolean filter(final SignData data, final EntryId entry, final ContentId content) {
		for (final StringFilterProperty line : StringFilterProperty.values())
			if (propFilter(data, entry, content, line))
				return true;
		return false;
	}

	protected abstract boolean propFilter(SignData data, final EntryId entry, final ContentId content, StringFilterProperty prop);

	public static class EqualsUniversalFilterElement extends UniversalFilterElement {

		public EqualsUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		protected boolean propFilter(final SignData data, final EntryId entry, final ContentId content, final StringFilterProperty prop) {
			return new StringFilterElement.EqualsStringFilterElement(prop, this.str).filter(data, entry, content);
		}

	}

	public static class EqualsIgnoreCaseUniversalFilterElement extends UniversalFilterElement {

		public EqualsIgnoreCaseUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		protected boolean propFilter(final SignData data, final EntryId entry, final ContentId content, final StringFilterProperty prop) {
			return new StringFilterElement.EqualsIgnoreCaseStringFilterElement(prop, this.str).filter(data, entry, content);
		}
	}

	public static class ContainsUniversalFilterElement extends UniversalFilterElement {

		public ContainsUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		protected boolean propFilter(final SignData data, final EntryId entry, final ContentId content, final StringFilterProperty prop) {
			return new StringFilterElement.ContainsStringFilterElement(prop, this.str).filter(data, entry, content);
		}

	}

	public static class ContainsIgnoreCaseUniversalFilterElement extends UniversalFilterElement {

		public ContainsIgnoreCaseUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		protected boolean propFilter(final SignData data, final EntryId entry, final ContentId content, final StringFilterProperty prop) {
			return new StringFilterElement.ContainsIgnoreCaseStringFilterElement(prop, this.str).filter(data, entry, content);
		}

	}
}
