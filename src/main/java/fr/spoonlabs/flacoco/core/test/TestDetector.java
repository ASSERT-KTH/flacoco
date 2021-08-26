package fr.spoonlabs.flacoco.core.test;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit4Strategy;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit5Strategy;
import fr.spoonlabs.flacoco.core.test.method.StringTestMethod;
import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.ClassloaderStrategy;
import fr.spoonlabs.flacoco.core.test.strategies.testrunner.TestRunnerStrategy;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Matias Martinez
 */
public class TestDetector {

	private Logger logger = Logger.getLogger(TestDetector.class);
	private FlacocoConfig config;

	public TestDetector(FlacocoConfig config) {
		this.config = config;
	}

	private List<TestContext> tests;

	public List<TestContext> getTests() {
		if (this.tests != null) {
			// If they have already been computed, return them
			logger.debug("Returning tests previously computed.");
			return this.tests;
		} else if (!this.config.getjUnit4Tests().isEmpty() || !this.config.getjUnit5Tests().isEmpty()) {
			// If not, but the config contains them, compute the List<TestContext>
			logger.debug("Computing tests from config.");
			this.tests = this.computeTests();
			return this.tests;
		} else {
			// If neither, compute them
			logger.debug("Running chosen test detection strategy: " + this.config.getTestDetectionStrategy());
			this.tests = this.findTests();
			return this.tests;
		}
	}

	private List<TestContext> computeTests() {
		List<TestContext> result = new ArrayList<>();

		if (!this.config.getjUnit4Tests().isEmpty()) {
			TestContext jUnit4Context = new TestContext(new JUnit4Strategy(config));
			jUnit4Context.addTestMethods(
					config.getjUnit4Tests().stream()
							.map(x -> new StringTestMethod(x.split("#")[0], x.split("#")[1]))
							.filter(x -> !isIgnored(x, config.getIgnoredTests()))
							.collect(Collectors.toList())
			);
			result.add(jUnit4Context);
		}
		if (!this.config.getjUnit5Tests().isEmpty()) {
			TestContext jUnit5Context = new TestContext(new JUnit5Strategy(config));
			jUnit5Context.addTestMethods(
					config.getjUnit5Tests().stream()
							.map(x -> new StringTestMethod(x.split("#")[0], x.split("#")[1]))
							.filter(x -> !isIgnored(x, config.getIgnoredTests()))
							.collect(Collectors.toList())
			);
			result.add(jUnit5Context);
		}

		return result;
	}

	private List<TestContext> findTests() {
		switch (config.getTestDetectionStrategy()) {
			case TEST_RUNNER:
				return new TestRunnerStrategy(config).findTests();
			case CLASSLOADER:
			default:
				return new ClassloaderStrategy(config).findTests();
		}
	}

	/**
	 * Checks if testMethod is included in the configured ignored tests
	 * @param testMethod The test method to be checked
	 * @return true if the test should be ignored, false otherwise
	 */
	public static boolean isIgnored(TestMethod testMethod, Set<String> ignoredTests) {
		return ignoredTests.contains(testMethod.getFullyQualifiedClassName()) ||
				ignoredTests.contains(testMethod.getFullyQualifiedMethodName());
	}

}