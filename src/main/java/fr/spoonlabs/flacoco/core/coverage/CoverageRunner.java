package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Class for running the coverage runner from test-runner and computing
 * the coverage matrix.
 *
 * @author Matias Martinez
 */
public class CoverageRunner {

	private Logger logger = Logger.getLogger(CoverageRunner.class);
	private FlacocoConfig config = FlacocoConfig.getInstance();

	public CoverageMatrix getCoverageMatrix(List<TestContext> testContexts) {
		// This matrix stores the results: the execution of tests and the coverage of
		// that execution on each line
		CoverageMatrix matrixExecutionResult = new CoverageMatrix();

		// For each test context
		int executedTests = 0;
		int testsFound = 0;
		for (TestContext testContext : testContexts) {
			this.logger.debug("Running " + testContext);

			try {
				// We run the test cases according to the specific test framework strategy
				CoveredTestResultPerTestMethod result = testContext.getTestFrameworkStrategy().execute(testContext);

				this.logger.debug(result);

				// Process each method individually
				for (TestMethod testMethod : testContext.getTestMethods()) {
					testsFound++;

					if (result.getCoverageResultsMap().containsKey(testMethod.getFullyQualifiedMethodName())) {
						matrixExecutionResult.processSingleTest(new CoverageFromSingleTestUnit(testMethod, result));
						executedTests++;
					} else {
						this.logger.warn("Test " + testMethod + " result was not reported by test-runner.");
					}
				}
			} catch (TimeoutException e) {
				this.logger.error(e);
			}
		}

		this.logger.info("Tests found: " + testsFound);
		this.logger.info("Tests executed: " + executedTests);
		return matrixExecutionResult;
	}

}