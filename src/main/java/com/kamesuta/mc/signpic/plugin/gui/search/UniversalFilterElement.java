package com.kamesuta.mc.signpic.plugin.gui.search;

import javax.annotation.Nullable;

import com.kamesuta.mc.signpic.plugin.SignData;

public abstract class UniversalFilterElement implements IFilterElement {
	public final @Nullable String str;

	public UniversalFilterElement(final String src) {
		this.str = src;
	}

	public static class EqualsUniversalFilterElement extends UniversalFilterElement {

		public EqualsUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		public boolean filter(final SignData data) {
			for (final StringFilterProperty line : StringFilterProperty.values())
				if (new StringFilterElement.EqualsStringFilterElement(line, this.str).filter(data))
					return true;
			return false;
		}

	}

	public static class EqualsIgnoreCaseUniversalFilterElement extends UniversalFilterElement {

		public EqualsIgnoreCaseUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		public boolean filter(final SignData data) {
			for (final StringFilterProperty line : StringFilterProperty.values())
				if (new StringFilterElement.EqualsIgnoreCaseStringFilterElement(line, this.str).filter(data))
					return true;
			return false;
		}
	}

	public static class ContainsUniversalFilterElement extends UniversalFilterElement {

		public ContainsUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		public boolean filter(final SignData data) {
			for (final StringFilterProperty line : StringFilterProperty.values())
				if (new StringFilterElement.ContainsStringFilterElement(line, this.str).filter(data))
					return true;
			return false;
		}

	}

	public static class ContainsIgnoreCaseUniversalFilterElement extends UniversalFilterElement {

		public ContainsIgnoreCaseUniversalFilterElement(final String src) {
			super(src);
		}

		@Override
		public boolean filter(final SignData data) {
			for (final StringFilterProperty line : StringFilterProperty.values())
				if (new StringFilterElement.ContainsIgnoreCaseStringFilterElement(line, this.str).filter(data))
					return true;
			return false;
		}

	}
}
