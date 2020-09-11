package fr.spoonlabs.test_framework.implementations;

import fr.spoonlabs.test_framework.AbstractTestFrameworkDecorator;
import spoon.reflect.declaration.CtElement;

/**
 * created by Benjamin DANGLOT benjamin.danglot@inria.fr on 09/11/18
 */
public class GoogleTruthTestFramework extends AbstractTestFrameworkDecorator {

	public GoogleTruthTestFramework() {
		super("com.google.common.truth.Truth");
	}

	private static final String ASSERT_THAT = "assertThat";

	public static final String ASSERT_NULL = "isNull";
	public static final String ASSERT_NOT_NULL = "isNotNull";
	public static final String ASSERT_TRUE = "isTrue";
	public static final String ASSERT_FALSE = "isFalse";
	public static final String ASSERT_EQUALS = "isEqualTo";
	public static final String ASSERT_NOT_EQUALS = "isNotEqualTo";

	@Override
	public boolean isIgnored(CtElement candidate) {
		return false;
	}
}
