package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import eu.stamp_project.testrunner.listener.impl.CoverageDetailed;
import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestInformation;
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

	public CoverageMatrix getCoverageMatrix(List<TestInformation> testToRun) {
		// This matrix stores the results: the execution of tests and the coverage of
		// that execution on each line
		CoverageMatrix matrixExecutionResult = new CoverageMatrix();

		// For each test class
		for (TestInformation testInformation : testToRun) {

			try {
				// We run the test cases according to the specific test framework strategy
				CoveredTestResultPerTestMethod result = testInformation.getTestFrameworkStrategy().execute(testInformation);

				this.logger.debug(result);

				// Process each method individually
				for (String method : testInformation.getTestMethodsNames()) {
					CoverageFromSingleTestUnit coverageFromSingleTestWrapper =
							new CoverageFromSingleTestUnit(
									testInformation.getTestClassQualifiedName(),
									method,
									(CoverageDetailed) result.getCoverageOf(method)
							);

					boolean isPassing = result.getPassingTests().contains(method)
							&& result.getFailingTests().stream().map(x -> x.testCaseName)
							.noneMatch(x -> x.equals(method))
							&& result.getAssumptionFailingTests().stream().map(x -> x.testCaseName)
							.noneMatch(x -> x.equals(method));
					coverageFromSingleTestWrapper.setIsPassing(isPassing);

					boolean isSkip = result.getIgnoredTests().contains(method);
					coverageFromSingleTestWrapper.setIsSkip(isSkip);

					matrixExecutionResult.processSingleTest(coverageFromSingleTestWrapper);
				}
			} catch (TimeoutException e) {
				this.logger.error(e);
			}
		}

		return matrixExecutionResult;
	}

}