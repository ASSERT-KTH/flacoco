package fr.spoonlabs.flacoco.core;

import java.net.URLClassLoader;
import java.util.List;

import org.apache.log4j.Logger;

import eu.stamp_project.testrunner.listener.CoveredTestResult;
import eu.stamp_project.testrunner.listener.impl.CoverageCollectorDetailed;
import eu.stamp_project.testrunner.runner.coverage.JacocoRunner;
import fr.spoonlabs.flacoco.entities.CoverageFromSingleTestUnit;
import fr.spoonlabs.flacoco.entities.MatrixCoverage;

/**
 * 
 * @author Matias Martinez
 *
 */
public class CoverageRunner {

	private Logger logger = Logger.getLogger(CoverageRunner.class.getName());

	public MatrixCoverage getCoverageMatrix(JacocoRunner runner, String classpath, String classesDirectory,
			String testClassesDirectory, List<TestInformation> testToRun) {
		boolean coverTest = false;
		return getCoverageMatrix(runner, classpath, classesDirectory, testClassesDirectory, testToRun, coverTest);
	}

	public MatrixCoverage getCoverageMatrix(JacocoRunner runner, String classpath, String classesDirectory,
			String testClassesDirectory, List<TestInformation> testToRun, boolean coverTest) {

		// This matrix stores the results: the execution of tests and the coverage of
		// that execution on each line
		MatrixCoverage matrixExecutionResult = new MatrixCoverage();

		URLClassLoader urlloader = runner.getUrlClassloaderFromClassPath(classpath);

		int i = 0;
		// For each test class:
		for (TestInformation testTuple : testToRun) {

			logger.debug("#Test " + i++ + " / " + testToRun.size() + " " + testTuple.getTestClassQualifiedName() + " "
					+ testTuple.getTestMethods().size());

			// For each method test
			for (String method : testTuple.getTestMethodsNames()) {

				logger.debug("-----");
				logger.debug("Calling method " + method);

				logger.debug("Classpath " + classpath);

				logger.debug("classesDirectory" + classesDirectory);
				logger.debug("testClassesDirectory" + testClassesDirectory);
				logger.debug("test class to run : " + testTuple.getTestClassQualifiedName());
				logger.debug("test method to run : " + method);

				// We run the instrumented classes
				CoveredTestResult coverageResult = runner.run(new CoverageCollectorDetailed(), urlloader,
						classesDirectory, testClassesDirectory, testTuple.getTestClassQualifiedName(), coverTest,
						new String[] { method });

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
}