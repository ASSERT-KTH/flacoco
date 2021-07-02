package fr.spoonlabs.flacoco.core.coverage.framework;

import eu.stamp_project.testrunner.EntryPoint;
import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import eu.stamp_project.testrunner.runner.ParserOptions;
import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestInformation;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.TimeoutException;

public class JUnit5Strategy extends TestFrameworkStrategy {

	private Logger logger = Logger.getLogger(JUnit5Strategy.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	@Override
	public CoveredTestResultPerTestMethod execute(TestInformation testInformation) throws TimeoutException {
		this.logger.info("Running: " + testInformation);
		this.setupTestRunnerEntryPoint();

		// test-runner needs a flag for JUnit5 tests
		EntryPoint.jUnit5Mode = true;

		return EntryPoint.runCoveredTestResultPerTestMethods(
				this.computeClasspath(),
				this.getPathToClasses() + File.pathSeparatorChar + this.getPathToTestClasses(),
				testInformation.getTestClassQualifiedName(),
				testInformation.getTestMethodsNames().toArray(new String[0])
		);
	}

}
