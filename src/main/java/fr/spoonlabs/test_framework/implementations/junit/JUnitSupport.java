package fr.spoonlabs.test_framework.implementations.junit;

import fr.spoonlabs.test_framework.AbstractTestFramework;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;

/**
 * created by Benjamin DANGLOT benjamin.danglot@inria.fr on 07/11/18
 * <p>
 * This abstract class is used for JUnit4 and JUnit5 support
 */
public abstract class JUnitSupport extends AbstractTestFramework {

	protected abstract String getFullQualifiedNameOfAnnotationTest();

	protected abstract String getFullQualifiedNameOfAnnotationIgnore();

	protected abstract String getFullQualifiedNameOfAnnotationAfterClass();

	public boolean isIgnored(CtElement candidate) {
		return this.hasAnnotation(getFullQualifiedNameOfAnnotationIgnore(), candidate);
	}

	protected boolean isATest(CtMethod<?> candidate) {
		return hasAnnotation(getFullQualifiedNameOfAnnotationTest(), candidate);
	}

	public JUnitSupport(String... qualifiedNameOfAssertClass) {
		super(qualifiedNameOfAssertClass);
	}

	@Override
	public boolean isInAssert(CtElement candidate) {
		if (candidate.getParent(CtInvocation.class) != null) {
			return this.isAssert(candidate.getParent(CtInvocation.class));
		} else {
			return false;
		}
	}

	@Override
	public boolean isTest(CtMethod<?> candidate) {
		if (candidate == null) {
			return false;
		}
		// if the test method has @Ignore, is not a test
		if (isIgnored(candidate)) {
			return false;
		}
		// if the test method has no body, or has parameters, is not a test
		if (candidate.isImplicit() || candidate.getBody() == null || candidate.getBody().getStatements().size() == 0
				|| !candidate.getParameters().isEmpty()) {
			return false;
		}
		// is a test according to the JUnit version
		return isATest(candidate);
	}

	private boolean hasAnnotation(String fullQualifiedNameOfAnnotation, CtElement candidate) {
		return candidate.getAnnotations().stream().anyMatch(ctAnnotation -> ctAnnotation.getAnnotationType()
				.getQualifiedName().equals(fullQualifiedNameOfAnnotation));
	}

	public static final String ASSERT_NULL = "assertNull";
	public static final String ASSERT_NOT_NULL = "assertNotNull";
	public static final String ASSERT_TRUE = "assertTrue";
	public static final String ASSERT_FALSE = "assertFalse";
	public static final String ASSERT_EQUALS = "assertEquals";
	public static final String ASSERT_NOT_EQUALS = "assertNotEquals";
	public static final String ASSERT_ARRAY_EQUALS = "assertArrayEquals";

}
