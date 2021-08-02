package fr.spoonlabs.flacoco.core.test;

import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.util.Objects;

/**
 * Contains all the information about a single test unit in a Spoonful way,
 * such as the test class and the method models
 *
 * @author andre15silva
 */
public class SpoonTestMethod implements TestMethod {

	private CtType<?> testClassModel;

	private String fullyQualifiedClassName;

	private CtMethod<?> testMethodModel;

	private String fullyQualifiedMethodName;

	public SpoonTestMethod(CtType<?> testClassModel, CtMethod<?> testMethodModel) {
		this.testClassModel = testClassModel;
		this.testMethodModel = testMethodModel;
		this.fullyQualifiedClassName = testClassModel.getQualifiedName();
		this.fullyQualifiedMethodName = fullyQualifiedClassName + "#" + testMethodModel.getSimpleName();
	}

	public CtType<?> getTestClassModel() {
		return testClassModel;
	}

	public CtMethod<?> getTestMethodModel() {
		return testMethodModel;
	}

	public String getFullyQualifiedClassName() {
		return fullyQualifiedClassName;
	}

	public String getFullyQualifiedMethodName() {
		return fullyQualifiedMethodName;
	}

	@Override
	public String toString() {
		return "[Spoon]TestMethod=" + getFullyQualifiedMethodName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass().getSuperclass() != o.getClass().getSuperclass()) return false;
		TestMethod that = (TestMethod) o;
		return Objects.equals(getFullyQualifiedMethodName(), that.getFullyQualifiedMethodName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getFullyQualifiedMethodName());
	}
}