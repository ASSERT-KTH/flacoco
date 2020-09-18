package fr.spoonlabs.spfl.core;

import java.net.URLClassLoader;
import java.util.List;

import org.apache.log4j.Logger;

import eu.stamp_project.testrunner.listener.TestCoveredResult;
import eu.stamp_project.testrunner.listener.impl.CoverageCollectorDetailed;
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

		URLClassLoader urlloader = runner.getUrlClassloaderFromClassPath(classpath);

		int i = 0;
		for (TestTuple testTuple : testToRun) {

			logger.debug("#Test " + i++ + " / " + testToRun.size() + " " + testTuple.testClassToBeAmplified + " "
					+ testTuple.testMethodsToBeAmplified.size());

			for (String method : testTuple.testMethodsToBeAmplified) {

				logger.debug("-----");
				logger.debug("Calling method " + method);

				logger.debug("Classpath " + classpath);

				logger.debug("classesDirectory" + classesDirectory);
				logger.debug("testClassesDirectory" + testClassesDirectory);
				logger.debug("test class to run : " + testTuple.testClassToBeAmplified);
				logger.debug("test method to run : " + method);

				// We run the instrumented classes
				TestCoveredResult coverageResult = runner.run(new CoverageCollectorDetailed(), urlloader,
						classesDirectory, testClassesDirectory, testTuple.testClassToBeAmplified,
						new String[] { method });

				if (coverageResult == null)
					continue;

				CoverageFromSingleTestUnit coverageFromSingleTestWrapper = new CoverageFromSingleTestUnit(
						testTuple.testClassToBeAmplified, method, coverageResult.getCoverageInformation());

				TestCoveredResult tr = (TestCoveredResult) coverageResult;

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
}