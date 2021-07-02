package fr.spoonlabs.flacoco.core.test;

import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

/**
 * Contains all the information about a single test unit, such as the test class and the method models
 *
 * @author andre15silva
 */
public class TestMethod {

	private CtType<?> testClassModel;

	private CtMethod<?> testMethodModel;

	public TestMethod(CtType<?> testClassModel, CtMethod<?> testMethodModel) {
		this.testClassModel = testClassModel;
		this.testMethodModel = testMethodModel;
	}

	public CtType<?> getTestClassModel() {
		return testClassModel;
	}

	public CtMethod<?> getTestMethodModel() {
		return testMethodModel;
	}

	public String getFullyQualifiedClassName() {
		return testClassModel.getQualifiedName();
	}

	public String getFullyQualifiedMethodName() {
		return testClassModel.getQualifiedName() + "#" + testMethodModel.getSimpleName();
	}

	public String getSimpleMethodName() {
		return testMethodModel.getSimpleName();
	}

	@Override
	public String toString() {
		return "TestMethod=" + getFullyQualifiedMethodName();
	}
}
