package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.EntryPoint;
import eu.stamp_project.testrunner.listener.CoveredTestResult;
import eu.stamp_project.testrunner.listener.impl.CoverageCollectorDetailed;
import eu.stamp_project.testrunner.runner.coverage.JUnit4JacocoRunner;
import eu.stamp_project.testrunner.runner.coverage.JacocoRunner;
import fr.spoonlabs.flacoco.api.Flacoco;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestInformation;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URLClassLoader;
import java.util.List;

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
		setupTestRunnerEntryPoint();

		// This matrix stores the results: the execution of tests and the coverage of
		// that execution on each line
		CoverageMatrix matrixExecutionResult = new CoverageMatrix();

		// Compute target path
		String pathToClasses = this.config.getProjectPath() + File.separator + "target/classes/";
		String pathToTestClasses = this.config.getProjectPath() + File.separator + "target/test-classes/";

		// Get JacocoRunner
		JacocoRunner runner = getJacocoRunner(pathToClasses, pathToTestClasses);
		URLClassLoader urlloader = runner.getUrlClassloaderFromClassPath(this.config.getClasspath());

		int i = 0;
		// For each test class:
		for (TestInformation testTuple : testToRun) {

			logger.debug("#Test " + i++ + " / " + testToRun.size() + " " + testTuple.getTestClassQualifiedName() + " "
					+ testTuple.getTestMethods().size());

			// For each method test
			for (String method : testTuple.getTestMethodsNames()) {

				logger.debug("-----");
				logger.debug("Calling method " + method);

				logger.debug("Classpath " + this.config.getClasspath());

				logger.debug("classesDirectory" + pathToClasses);
				logger.debug("testClassesDirectory" + pathToTestClasses);
				logger.debug("test class to run : " + testTuple.getTestClassQualifiedName());
				logger.debug("test method to run : " + method);

				// We run the instrumented classes
				if (this.config.isCoverTests()) {
					runner.instrumentAll(pathToTestClasses);
				}
				CoveredTestResult coverageResult = runner.run(new CoverageCollectorDetailed(), urlloader,
						pathToClasses, pathToTestClasses, testTuple.getTestClassQualifiedName(), this.config.isCoverTests(),
						new String[]{method});

				if (coverageResult == null)
					continue;

				CoverageFromSingleTestUnit coverageFromSingleTestWrapper = new CoverageFromSingleTestUnit(
						testTuple.getTestClassQualifiedName(), method, coverageResult.getCoverageInformation());

				CoveredTestResult tr = (CoveredTestResult) coverageResult;

				boolean isPassing = tr.getPassingTests().size() > 0 && tr.getFailingTests().size() == 0;
				coverageFromSingleTestWrapper.setIsPassing(isPassing);

				boolean isSkip = tr.getIgnoredTests().size() > 0 || tr.getAssumptionFailingTests().size() > 0;
				if (isSkip) {
					coverageFromSingleTestWrapper.setIsSkip(isSkip);
				} else {
					coverageFromSingleTestWrapper.setIsSkip(false);
				}

				matrixExecutionResult.processSingleTest(coverageFromSingleTestWrapper);

			}

		}
		return matrixExecutionResult;
	}

	/**
	 * Auxiliary method to setup test-runners entry point
	 */
	private void setupTestRunnerEntryPoint() {
		// test-runner requires a flag when using JUnit5
		if (this.config.getTestFramework().equals(FlacocoConfig.TestFramework.JUNIT5)) {
			EntryPoint.jUnit5Mode = true;
		}
	}

	private JacocoRunner getJacocoRunner(String sourceClasses, String testClasses) {
		return new JUnit4JacocoRunner(sourceClasses, testClasses, new CoverageCollectorDetailed());
	}
}