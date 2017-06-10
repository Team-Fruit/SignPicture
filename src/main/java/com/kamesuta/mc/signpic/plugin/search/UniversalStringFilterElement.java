package com.kamesuta.mc.signpic.plugin.search;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.ContentId;
import com.kamesuta.mc.signpic.plugin.SignData;

public abstract class UniversalStringFilterElement implements DataFilterElement {
	public final @Nullable String str;

	public UniversalStringFilterElement(final String src) {
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

	public static class EqualsUniversalFilterElement extends UniversalStringFilterElement {

		public EqualsUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		protected boolean propFilter(final SignData data, final EntryId entry, final ContentId content, final StringFilterProperty prop) {
			return new StringFilterElement.EqualsStringFilterElement(prop, this.str).filter(data, entry, content);
		}

	}

	public static class EqualsIgnoreCaseUniversalFilterElement extends UniversalStringFilterElement {

		public EqualsIgnoreCaseUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		protected boolean propFilter(final SignData data, final EntryId entry, final ContentId content, final StringFilterProperty prop) {
			return new StringFilterElement.EqualsIgnoreCaseStringFilterElement(prop, this.str).filter(data, entry, content);
		}
	}

	public static class ContainsUniversalFilterElement extends UniversalStringFilterElement {

		public ContainsUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		protected boolean propFilter(final SignData data, final EntryId entry, final ContentId content, final StringFilterProperty prop) {
			return new StringFilterElement.ContainsStringFilterElement(prop, this.str).filter(data, entry, content);
		}

	}

	public static class ContainsIgnoreCaseUniversalFilterElement extends UniversalStringFilterElement {

		public ContainsIgnoreCaseUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		protected boolean propFilter(final SignData data, final EntryId entry, final ContentId content, final StringFilterProperty prop) {
			return new StringFilterElement.ContainsIgnoreCaseStringFilterElement(prop, this.str).filter(data, entry, content);
		}

	}
}
