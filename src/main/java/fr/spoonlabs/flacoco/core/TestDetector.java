package fr.spoonlabs.flacoco.core;

import java.util.ArrayList;
import java.util.List;

import eu.stamp_project.testrunner.test_framework.TestFramework;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;

/**
 * 
 * @author Matias Martinez
 *
 */
public class TestDetector {
	TestFramework testFramework;

	public TestDetector() {

	}

	public List<TestInformation> findTest(Factory factory) {
		TestFramework.init(factory);
		this.testFramework = TestFramework.get();

		List<TestInformation> tuples = new ArrayList();

		List<CtType<?>> modelclassesOfTest = this.testFramework.getAllTestClasses();
		for (CtType<?> type : modelclassesOfTest) {

			if (type.isAbstract()) {
				continue;
			}

			List<CtMethod<?>> methodsTest = this.testFramework.getAllTest(type);

			TestInformation tuple = new TestInformation(type, methodsTest);

			tuples.add(tuple);
		}
		return tuples;
	}

}