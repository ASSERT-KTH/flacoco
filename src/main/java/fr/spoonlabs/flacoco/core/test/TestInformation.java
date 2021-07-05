package fr.spoonlabs.flacoco.core.test;

import fr.spoonlabs.flacoco.core.coverage.framework.TestFrameworkStrategy;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains a test class, its methods and a strategy according to its test framework
 *
 * @author Matias Martinez
 */
public class TestInformation {

	protected CtType<?> testClassModel = null;

	protected List<CtMethod<?>> testMethods;

	protected TestFrameworkStrategy testFrameworkStrategy;

	public TestInformation(CtType<?> testClassModel, List<CtMethod<?>> testMethods, TestFrameworkStrategy testFrameworkStrategy) {
		this.testClassModel = testClassModel;
		this.testMethods = testMethods;
		this.testFrameworkStrategy = testFrameworkStrategy;
	}

	public String getTestClassQualifiedName() {
		return testClassModel.getQualifiedName();
	}

	public List<String> getTestMethodsNames() {
		return testMethods.stream().map(e -> e.getSimpleName()).collect(Collectors.toList());
	}

	public CtType<?> getTestClassModel() {
		return testClassModel;
	}

	public void setTestClassModel(CtType<?> testClassModel) {
		this.testClassModel = testClassModel;
	}

	public List<CtMethod<?>> getTestMethods() {
		return testMethods;
	}

	public void setTestMethods(List<CtMethod<?>> testMethods) {
		this.testMethods = testMethods;
	}

	public TestFrameworkStrategy getTestFrameworkStrategy() {
		return testFrameworkStrategy;
	}

	public void setTestFrameworkStrategy(TestFrameworkStrategy testFrameworkStrategy) {
		this.testFrameworkStrategy = testFrameworkStrategy;
	}

	@Override
	public String toString() {
		return "TestInformation{" +
				"testClassModel=" + testClassModel.getQualifiedName() +
				", testMethods=" + testMethods.stream().map(CtMethod::getSimpleName) +
				", testFrameworkStrategy=" + testFrameworkStrategy +
				'}';
	}

}
