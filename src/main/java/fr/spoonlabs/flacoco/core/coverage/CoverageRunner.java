package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.EntryPoint;
import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import eu.stamp_project.testrunner.listener.impl.CoverageDetailed;
import eu.stamp_project.testrunner.runner.ParserOptions;
import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestInformation;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Class for running the coverage runner from test-runner and computing
 * the coverage matrix.
 *
 * @author Matias Martinez
 */
public class CoverageRunner {

	private Logger logger = Logger.getLogger(Flacoco.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	public CoverageMatrix getCoverageMatrix(List<TestInformation> testToRun) {
		// This matrix stores the results: the execution of tests and the coverage of
		// that execution on each line
		CoverageMatrix matrixExecutionResult = new CoverageMatrix();

		// Compute target path
		String pathToClasses = new File(this.config.getProjectPath() + File.separator + "target/classes/")
				.getAbsolutePath();
		String pathToTestClasses = new File(this.config.getProjectPath() + File.separator + "target/test-classes/")
				.getAbsolutePath();

		// For each test class:
		for (TestInformation testTuple : testToRun) {

			this.logger.debug("-----");
			this.logger.debug("Calling class " + testTuple.getTestClassQualifiedName());

			this.logger.debug("Classpath " + this.config.getClasspath());
			this.logger.debug("classesDirectory" + pathToClasses);
			this.logger.debug("testClassesDirectory" + pathToTestClasses);
			this.logger.debug("test class to run : " + testTuple.getTestClassQualifiedName());
			this.logger.debug("test methods to run : " + testTuple.getTestMethodsNames());

			try {
				setupTestRunnerEntryPoint();
				CoveredTestResultPerTestMethod result = EntryPoint.runCoveredTestResultPerTestMethods(
						this.config.getClasspath(),
						pathToClasses + File.pathSeparatorChar + pathToTestClasses,
						testTuple.getTestClassQualifiedName(),
						testTuple.getTestMethodsNames().toArray(new String[0])
				);

				if (result == null) {
					this.logger.error("Got a null result for test class: " + testTuple.getTestClassQualifiedName());
					continue;
				}

				this.logger.debug(result);

				// Process each method individually
				for (String method : testTuple.getTestMethodsNames()) {
					CoverageFromSingleTestUnit coverageFromSingleTestWrapper = new CoverageFromSingleTestUnit(
							testTuple.getTestClassQualifiedName(),
							method,
							(CoverageDetailed) result.getCoverageOf(method)
					);

					boolean isPassing = result.getPassingTests().contains(method) && !result.getFailingTests().contains(method);
					coverageFromSingleTestWrapper.setIsPassing(isPassing);
					boolean isSkip = result.getIgnoredTests().contains(method) || result.getFailingTests().contains(method);
					coverageFromSingleTestWrapper.setIsSkip(isSkip);

					matrixExecutionResult.processSingleTest(coverageFromSingleTestWrapper);
				}

			} catch (TimeoutException e) {
				this.logger.error(e);
			}
		}
		return matrixExecutionResult;
	}

	/**
	 * Auxiliary method to setup test-runners entry point
	 */
	private void setupTestRunnerEntryPoint() {
		EntryPoint.coverageDetail = ParserOptions.CoverageTransformerDetail.DETAIL;
		EntryPoint.verbose = true;
		// test-runner requires a flag when using JUnit5
		if (this.config.getTestFramework().equals(FlacocoConfig.TestFramework.JUNIT5)) {
			EntryPoint.jUnit5Mode = true;
		}
		if (this.config.isCoverTests()) {
			throw new UnsupportedOperationException();
		}
	}

}