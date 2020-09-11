package fr.spoonlabs.spfl.core;

import java.util.List;

import org.apache.log4j.Logger;

import eu.stamp_project.testrunner.listener.Coverage;
import eu.stamp_project.testrunner.listener.TestResult;
import eu.stamp_project.testrunner.listener.impl.CoverageLineImpl;
import eu.stamp_project.testrunner.runner.coverage.JacocoRunner;
import fr.spoonlabs.spfl.entities.CoverageFromSingleTestUnit;
import fr.spoonlabs.spfl.entities.MatrixCoverage;
import fr.spoonlabs.test_framework.TestTuple;

/**
 * 
 * @author Matias Martinez
 *
 */
public class CoverageRunner {

	private Logger logger = Logger.getLogger(CoverageRunner.class.getName());

	public MatrixCoverage getCoverageMatrix(JacocoRunner runner, String classpath, String classesDirectory,
			String testClassesDirectory, List<TestTuple> testToRun) {

		// This matrix stores the results: the execution of tests and
		MatrixCoverage matrixExecutionResult = new MatrixCoverage();

		int i = 0;
		for (TestTuple testTuple : testToRun) {

			logger.debug("#Test " + i++ + " / " + testToRun.size() + " " + testTuple.testClassToBeAmplified + " "
					+ testTuple.testMethodsToBeAmplified.size());

			for (String method : testTuple.testMethodsToBeAmplified) {

				logger.debug("Classpath " + classpath);

				logger.debug("classesDirectory" + classesDirectory);
				logger.debug("testClassesDirectory" + testClassesDirectory);
				logger.debug("test class to run : " + testTuple.testClassToBeAmplified);

				// We instrument the classes
				runner.recreateInstrumentedClassloaded(classpath, classesDirectory, testClassesDirectory,
						runner.getInstrumentedClassLoader().getDefinitions());

				// We run the instrumented classes
				Coverage coverageResult = runner.runAlternative(new CoverageLineImpl(), classesDirectory,
						testClassesDirectory, testTuple.testClassToBeAmplified, new String[] { method });

				logger.debug(
						method + " " + ((coverageResult != null) ? coverageResult.getDetailedCoverage().keySet().size()
								: " null cover"));

				if (coverageResult == null)
					continue;

				CoverageFromSingleTestUnit coverageFromSingleTestWrapper = new CoverageFromSingleTestUnit(
						testTuple.testClassToBeAmplified, method, coverageResult);

				if (coverageResult instanceof TestResult) {

					TestResult tr = (TestResult) coverageResult;

					boolean isPassing = tr.getPassingTests().size() > 0 && tr.getFailingTests().size() == 0;
					coverageFromSingleTestWrapper.setIsPassing(isPassing);

					boolean isSkip = tr.getIgnoredTests().size() > 0 || tr.getAssumptionFailingTests().size() > 0;
					if (isSkip) {
						coverageFromSingleTestWrapper.setIsSkip(isSkip);
					} else {
						coverageFromSingleTestWrapper.setIsSkip(false);
					}

				} else {
					logger.error("Result is not a Test Result...");
				}

				matrixExecutionResult.processSingleTest(coverageFromSingleTestWrapper);

			}

		}
		return matrixExecutionResult;

	}
}