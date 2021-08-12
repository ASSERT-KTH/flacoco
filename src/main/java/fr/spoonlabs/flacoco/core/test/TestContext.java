package fr.spoonlabs.flacoco.core.test;

import fr.spoonlabs.flacoco.core.coverage.framework.TestFrameworkStrategy;
import fr.spoonlabs.flacoco.core.test.method.TestMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contains a list of TestMethod's to be ran using the specified TestFrameworkStrategy
 *
 * @author andre15silva
 */
public class TestContext {

	private List<TestMethod> testMethods;

	private TestFrameworkStrategy testFrameworkStrategy;

	public TestContext(TestFrameworkStrategy testFrameworkStrategy) {
		this.testMethods = new ArrayList<>();
		this.testFrameworkStrategy = testFrameworkStrategy;
	}

	public void addTestMethods(Collection<TestMethod> testMethodCollection) {
		this.testMethods.addAll(testMethodCollection);
	}

	public List<TestMethod> getTestMethods() {
		return testMethods;
	}

	public TestFrameworkStrategy getTestFrameworkStrategy() {
		return testFrameworkStrategy;
	}

	@Override
	public String toString() {
		return "TestContext{" +
				"testMethods=" + testMethods +
				", testFrameworkStrategy=" + testFrameworkStrategy +
				'}';
	}
}
