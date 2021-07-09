package fr.spoonlabs.flacoco.core.test;

import eu.stamp_project.testrunner.test_framework.TestFramework;
import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit4Strategy;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit5Strategy;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.declaration.CtType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Matias Martinez
 */
public class TestDetector {

	private Logger logger = Logger.getLogger(TestDetector.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	public List<TestContext> findTests() {
		// Create Spoon model to retrieve information about the tests
		Launcher laucher = new Launcher();
		laucher.addInputResource(config.getSrcJavaDir());
		laucher.addInputResource(config.getSrcTestDir());
		laucher.buildModel();

		// Init test framework
		TestFramework.init(laucher.getFactory());

		TestContext jUnit4Context = new TestContext(new JUnit4Strategy());
		TestContext jUnit5Context = new TestContext(new JUnit5Strategy());

		for (CtType<?> ctType : TestFramework.getAllTestClasses()) {

			if (ctType.isAbstract()) {
				continue;
			}

			// Add JUnit4 methods to jUnit4Context
			jUnit4Context.addTestMethods(
					TestFramework.getAllTest(ctType).stream().filter(TestFramework::isJUnit4)
							.map(ctMethod -> new TestMethod(ctType, ctMethod))
							.collect(Collectors.toList())
			);

			// Add JUnit5 methods to jUnit5Context
			jUnit5Context.addTestMethods(
					TestFramework.getAllTest(ctType).stream().filter(TestFramework::isJUnit5)
							.map(ctMethod -> new TestMethod(ctType, ctMethod))
							.collect(Collectors.toList())
			);
		}

		// We only want to return those that have test units
		return Stream.of(jUnit4Context, jUnit5Context)
				.filter(x -> !x.getTestMethods().isEmpty()).collect(Collectors.toList());
	}

}