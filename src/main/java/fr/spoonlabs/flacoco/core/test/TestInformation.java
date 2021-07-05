package fr.spoonlabs.flacoco.core.test;

import java.util.List;
import java.util.stream.Collectors;

import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

/**
 * 
 * @author Matias Martinez
 *
 */
public class TestInformation {

	protected CtType<?> testClassModel = null;

	protected List<CtMethod<?>> testMethods;

	public TestInformation(CtType<?> testClassModel, List<CtMethod<?>> testMethods) {
		super();
		this.testClassModel = testClassModel;
		this.testMethods = testMethods;
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

	@Override
	public String toString() {
		return "TestInformation{" +
				"testClassModel=" + testClassModel +
				", testMethods=" + testMethods +
				'}';
	}
}
