package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import eu.stamp_project.testrunner.listener.impl.CoverageDetailed;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestMethod;
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
		for (TestContext testContext : testContexts) {

			try {
				// We run the test cases according to the specific test framework strategy
				CoveredTestResultPerTestMethod result = testContext.getTestFrameworkStrategy().execute(testContext);

				this.logger.debug(result);

				// Process each method individually
				for (TestMethod testMethod : testContext.getTestMethods()) {
					CoverageFromSingleTestUnit coverageFromSingleTestWrapper =
							new CoverageFromSingleTestUnit(
									testMethod,
									(CoverageDetailed) result.getCoverageOf(testMethod.getFullyQualifiedMethodName())
							);

					boolean isPassing = result.getPassingTests().contains(testMethod.getFullyQualifiedMethodName())
							&& result.getFailingTests().stream().map(x -> x.testClassName + "#" + x.testCaseName)
							.noneMatch(x -> x.equals(testMethod.getFullyQualifiedMethodName()))
							&& result.getAssumptionFailingTests().stream().map(x -> x.testClassName + "#" + x.testCaseName)
							.noneMatch(x -> x.equals(testMethod.getFullyQualifiedMethodName()));
					coverageFromSingleTestWrapper.setPassing(isPassing);

					boolean isSkip = result.getIgnoredTests().contains(testMethod.getFullyQualifiedMethodName());
					coverageFromSingleTestWrapper.setSkip(isSkip);

					matrixExecutionResult.processSingleTest(coverageFromSingleTestWrapper);
				}
			} catch (TimeoutException e) {
				this.logger.error(e);
			}
		}

		return matrixExecutionResult;
	}

}