package net.teamfruit.emojicord.asm.lib;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import net.teamfruit.emojicord.Log;

public class ASMValidate {
	private ASMValidate() {
	}

	public void test(final String name) {
	}

	public void test(final String name, final boolean condition) {
	}

	public void tests(final String name, final int occurrences) {
	}

	public void tests(final String name, final boolean condition, final int occurrences) {
	}

	public void check(final String name) {
	}

	public void checks(final String name) {
	}

	public void validate() {
	}

	public static ASMValidate create(final String testName) {
		if (ASMValidateImpl.IsEnabled)
			return new ASMValidateImpl(testName);
		return new ASMValidate();
	}

	private static class ASMValidateImpl extends ASMValidate {
		public static final boolean IsEnabled;

		static {
			IsEnabled = !StringUtils.isEmpty(System.getenv("TEAMFRUIT_DEV"));
			if (IsEnabled)
				Log.log.info("ASM Validator Enabled");
			else
				Log.log.debug("ASM Validator Disabled");
		}

		private final String testName;
		private final Multiset<String> cases = HashMultiset.create();

		public ASMValidateImpl(final String testName) {
			this.testName = testName;
			Log.log.info("ASM Validator Start: "+testName);
		}

		@Override
		public void test(final String name) {
			tests(name, 1);
		}

		@Override
		public void test(final String name, final boolean condition) {
			tests(name, condition, 1);
		}

		@Override
		public void tests(final String name, final int occurrences) {
			tests(name, true, occurrences);
		}

		@Override
		public void tests(final String name, final boolean condition, final int occurrences) {
			if (condition)
				this.cases.add(name, occurrences);
		}

		// It is rarely case, so it crashes immediately.
		@Override
		public void check(final String name) {
			Validate.validState(this.cases.remove(name), "ASM Assertion Error in %s: Too many checkpoints: %s", this.testName, name);
		}

		@Override
		public void checks(final String name) {
			this.cases.remove(name);
		}

		// It often happens due to mistakes in arguments or Mcp or Srg names, so it should be displayed collectively.
		@Override
		public void validate() {
			if (this.cases.size()>0)
				throw new IllegalStateException(String.format("ASM Assertion Error in %s: Not enough checkpoints: %s", this.testName,
						this.cases.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting())).entrySet()
								.stream().map(e -> (e.getValue()==1 ? e.getKey() : String.format("%s(%d)", e.getKey(), e.getValue())))
								.collect(Collectors.joining(", "))));
			Log.log.info("ASM Validator Passed: "+this.testName);
		}
	}
}
