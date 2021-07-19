package fr.spoonlabs.flacoco.core.coverage.framework;

import eu.stamp_project.testrunner.EntryPoint;
import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestMethod;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeoutException;

public class JUnit5Strategy extends TestFrameworkStrategy {

	private Logger logger = Logger.getLogger(JUnit5Strategy.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	@Override
	public CoveredTestResultPerTestMethod execute(TestContext testContext) throws TimeoutException {
		this.logger.debug("Running " + testContext);
		this.setupTestRunnerEntryPoint();

		// test-runner needs a flag for JUnit5 tests
		EntryPoint.jUnit5Mode = true;

		return EntryPoint.runCoveredTestResultPerTestMethods(
				this.computeClasspath(),
				config.getBinJavaDir(),
				config.getBinTestDir(),
				testContext.getTestMethods().stream().map(TestMethod::getFullyQualifiedClassName).distinct().toArray(String[]::new),
				new String[0]
		);
	}

}
