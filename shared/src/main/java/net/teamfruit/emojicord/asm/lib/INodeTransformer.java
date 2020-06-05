package net.teamfruit.emojicord.asm.lib;

import org.apache.commons.lang3.StringUtils;

public interface INodeTransformer {
	ClassName getClassName();

	default String getSimpleName() {
		return StringUtils.substringAfterLast(getClassName().getName(), ".");
	}

	default ClassMatcher getMatcher() {
		return new ClassMatcher(getClassName());
	}
}