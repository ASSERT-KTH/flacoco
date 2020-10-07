package fr.spoonlabs.spfl.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	public List<TestTuple> findTest(Factory factory) {
		TestFramework.init(factory);
		this.testFramework = TestFramework.get();

		List<TestTuple> tuples = new ArrayList();

		List<CtType<?>> modelclassesOfTest = this.testFramework.getAllTestClasses();
		for (CtType<?> type : modelclassesOfTest) {

			if (type.isAbstract()) {
				continue;
			}

			List<CtMethod<?>> methodsTest = this.testFramework.getAllTest(type);

			List<String> methodTestNames = methodsTest.stream().map(e -> e.getSimpleName())
					.collect(Collectors.toList());

			TestTuple tuple = new TestTuple(type.getQualifiedName(), methodTestNames);

			tuples.add(tuple);
		}
		return tuples;
	}

}