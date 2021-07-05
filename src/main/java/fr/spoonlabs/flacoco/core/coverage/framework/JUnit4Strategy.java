package fr.spoonlabs.flacoco.core.coverage.framework;

import eu.stamp_project.testrunner.EntryPoint;
import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestMethod;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.TimeoutException;

public class JUnit4Strategy extends TestFrameworkStrategy {

	private Logger logger = Logger.getLogger(JUnit4Strategy.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	@Override
	public CoveredTestResultPerTestMethod execute(TestContext testContext) throws TimeoutException {
		this.logger.info("Running : " + testContext);
		this.setupTestRunnerEntryPoint();

		return EntryPoint.runCoveredTestResultPerTestMethods(
				this.computeClasspath(),
				this.getPathToClasses() + File.pathSeparatorChar + this.getPathToTestClasses(),
				testContext.getTestMethods().stream().map(TestMethod::getFullyQualifiedClassName).toArray(String[]::new),
				testContext.getTestMethods().stream().map(TestMethod::getFullyQualifiedMethodName).toArray(String[]::new)
		);
	}

}
