package fr.spoonlabs.test_framework.implementations.junit;

/**
 * created by Benjamin DANGLOT benjamin.danglot@inria.fr on 07/11/18
 */
public class JUnit4Support extends JUnitSupport {

	public JUnit4Support() {
		super("org.junit.Assert");
	}

	@Override
	protected String getFullQualifiedNameOfAnnotationAfterClass() {
		return "org.junit.AfterClass";
	}

	@Override
	protected String getFullQualifiedNameOfAnnotationTest() {
		return "org.junit.Test";
	}

	@Override
	protected String getFullQualifiedNameOfAnnotationIgnore() {
		return "org.junit.Ignore";
	}

}
