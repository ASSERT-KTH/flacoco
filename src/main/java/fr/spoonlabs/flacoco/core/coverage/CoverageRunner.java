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
						computeClasspath(),
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
		EntryPoint.workingDirectory = new File(this.config.getWorkspace());
		EntryPoint.verbose = this.config.isTestRunnerVerbose();
		EntryPoint.timeoutInMs = this.config.getTestRunnerTimeoutInMs();
		EntryPoint.JVMArgs = this.config.getTestRunnerJVMArgs();

		// test-runner requires a flag when using JUnit5
		EntryPoint.jUnit5Mode = this.config.getTestFramework().equals(FlacocoConfig.TestFramework.JUNIT5);
		if (this.config.isCoverTests()) {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Auxiliary method to compute the classpath according to the test framework and the custom confirguration
	 *
	 * @return Classpath for test-runner execution
	 */
	private String computeClasspath() {
		String classpath = this.config.getClasspath();
		String mavenHome = this.config.getMavenHome();
		String junitClasspath;
		String jacocoClassPath;

		junitClasspath = mavenHome + "junit/junit/4.12/junit-4.12.jar" + File.pathSeparatorChar
				+ mavenHome + "org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-api/5.3.2/junit-jupiter-api-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar" + File.pathSeparatorChar
				+ mavenHome + "org/opentest4j/opentest4j/1.1.1/opentest4j-1.1.1.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-commons/1.3.2/junit-platform-commons-1.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-engine/5.3.2/junit-jupiter-engine-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-params/5.3.2/junit-jupiter-params-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-engine/1.3.2/junit-platform-engine-1.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-launcher/1.3.2/junit-platform-launcher-1.3.2.jar";

		jacocoClassPath = mavenHome + "org/jacoco/org.jacoco.core/0.8.3/org.jacoco.core-0.7.9.jar";

		// Add JUnit dependencies
		if (this.config.getCustomJUnitClasspath() != null) {
			classpath += File.pathSeparatorChar + this.config.getCustomJUnitClasspath();
		} else {
			classpath += File.pathSeparatorChar + junitClasspath;
		}
		// Add jacoco dependency
		if (this.config.getCustomJacocoClasspath() != null) {
			classpath += File.pathSeparatorChar + this.config.getCustomJacocoClasspath();
		} else {
			classpath += File.pathSeparatorChar + jacocoClassPath;
		}

		return classpath;
	}

}