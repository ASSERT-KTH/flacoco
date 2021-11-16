package fr.spoonlabs.flacoco.core.coverage;

import ch.scheitlin.alex.java.StackTrace;
import ch.scheitlin.alex.java.StackTraceParser;
import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import eu.stamp_project.testrunner.listener.impl.CoverageDetailed;
import eu.stamp_project.testrunner.listener.impl.CoverageFromClass;
import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import fr.spoonlabs.flacoco.utils.spoon.SpoonBlockInspector;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.surefire.util.DirectoryScanner;
import org.apache.maven.surefire.api.testset.TestListResolver;
import org.jacoco.core.runtime.WildcardMatcher;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains the result of the execution of a set of test units
 *
 * @author Matias Martinez
 */
public class CoverageMatrix {

	private Logger logger = Logger.getLogger(CoverageMatrix.class);

	private FlacocoConfig config;

	public CoverageMatrix(FlacocoConfig config) {
		this.config = config;
	}

	/**
	 * Key is the line, value is a set of test methods that execute that line
	 */
	protected Map<Location, Set<TestMethod>> resultExecution = new HashMap<>();

	/**
	 * Map between executed test methods and their result. True if passing, false is failing.
	 */
	protected Map<TestMethod, Boolean> tests = new HashMap<>();

	/**
	 * Processes a wrapper for the coverage from a single test unit
	 *
	 * @param iCovWrapper The coverage information related to the single unit test
	 * @param testClasses Classes which contain tests
	 */
	public void processSingleTest(CoverageFromSingleTestUnit iCovWrapper, Set<String> testClasses) {
		CoverageDetailed covLine = iCovWrapper.getCov();

		if (iCovWrapper.isSkip()) {
			logger.debug("Ignoring skipped test: " + iCovWrapper.getTestMethod().getFullyQualifiedMethodName());
			return;
		}

		boolean isPassing = iCovWrapper.isPassing();

		// Let's navigate the covered class per line.
		for (String iClassNameCovered : covLine.getDetailedCoverage().keySet()) {

			String className = iClassNameCovered.replace("/", ".");
			if (!config.isCoverTests() && testClasses.contains(className)) {
				continue;
			}

			// Lines covered in that class
			CoverageFromClass lines = covLine.getDetailedCoverage().get(iClassNameCovered);

			for (int iLineNumber : lines.getCov().keySet()) {

				int instExecutedAtLineI = lines.getCov().get(iLineNumber);

				this.add(new Location(className, iLineNumber), iCovWrapper.getTestMethod(), instExecutedAtLineI, isPassing);

			}
		}

		// Now, we check if any exception was thrown and, if so, add the line where it was thrown
		// since JaCoCo does not include them in coverage
		// Handle tests that throw exceptions
		CoveredTestResultPerTestMethod result = iCovWrapper.getCoveredTestResultPerTestMethod();
		TestMethod testMethod = iCovWrapper.getTestMethod();
		if (!isPassing && result.getFailureOf(testMethod.getFullyQualifiedMethodName()) != null) {

			try {
				StackTrace trace = StackTraceParser
						.parse(result.getFailureOf(testMethod.getFullyQualifiedMethodName()).stackTrace);

				for (StackTraceElement element : trace.getStackTraceLines()) {
					// Search for first non-native element
					if (!element.isNativeMethod()) {
						// We want to keep it if and only if it the class was included in the coverage
						// computation, which will ignore classes like org.junit.Assert
						if (classToInclude(element.getClassName())) {

							// We also want to ignore test classes if they coverTests is not set
							if (!config.isCoverTests() && testClasses.contains(element.getClassName())) {
								continue;
							}

							Location location = new Location(
									element.getClassName(),
									element.getLineNumber()
							);

							logger.debug("Adding a line where an exception was thrown: " + location);
							this.add(location, testMethod, 1, false);

							// Compute the executed lines from the block where the exception was thrown
							// See: https://github.com/SpoonLabs/flacoco/issues/109
							SpoonBlockInspector blockMatcher = new SpoonBlockInspector(config);
							List<Location> locations = blockMatcher.getBlockLocations(element);

							for (Location blockLocation : locations) {
								logger.debug("Adding a line from the block where an exception was thrown: " + blockLocation);
								this.add(blockLocation, testMethod, 1, false);
							}
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public Map<Location, Set<TestMethod>> getResultExecution() {
		return resultExecution;
	}

	public Map<TestMethod, Boolean> getTests() {
		return tests;
	}

	public Set<TestMethod> getFailingTestCases() {
		return this.tests.entrySet().stream().filter(x -> !x.getValue())
				.map(Map.Entry::getKey).collect(Collectors.toSet());
	}

	/**
	 * Auxiliary method to introduce the gathered information about a test unit run in the coverage matrix
	 * <p>
	 * The modifier is public for testing purposes
	 *
	 * @param location The location to be added
	 * @param testMethod The test method which covered the location
	 * @param instExecutedAtLineI Number of instructions executed at the location
	 * @param testResult The result of the test method
	 */
	public void add(Location location, TestMethod testMethod, int instExecutedAtLineI, Boolean testResult) {
		if (instExecutedAtLineI > 0) {
			Set<TestMethod> currentExecution;

			if (this.resultExecution.containsKey(location)) {
				currentExecution = this.resultExecution.get(location);
			} else {
				currentExecution = new HashSet<>();
				this.resultExecution.put(location, currentExecution);
			}

			currentExecution.add(testMethod);
		}

		this.tests.put(testMethod, testResult);
	}

	/**
	 * Computes if a given class is to be included in the post-coverage computation
	 *
	 * If we have include-exclude patterns for Jacoco:
	 * - We include the class if it matches the include patterns and does not match the excludes patterns
	 * Else:
	 * - We include the class if it is available in the binary directories
	 * @param className
	 * @return true if the class should be included in the coverage result, false otherwise
	 */
    private boolean classToInclude(String className) {
        // False if it matches an excludes pattern
        for (String pattern : config.getJacocoExcludes()) {
            WildcardMatcher matcher = new WildcardMatcher(pattern);
            if (matcher.matches(className)) {
                return false;
            }
        }
        // True if it matches an includes pattern and doesn't match any excludes pattern
        for (String pattern : config.getJacocoIncludes()) {
            WildcardMatcher matcher = new WildcardMatcher(pattern);
            if (matcher.matches(className)) {
                return true;
            }
        }

        // True if it is present in the available binaries
        for (String dir : config.getBinJavaDir()) {
            DirectoryScanner directoryScanner = new DirectoryScanner(new File(dir), TestListResolver.getWildcard());
            if (directoryScanner.scan().getClasses().contains(className)) {
                return true;
            }
        }
        for (String dir : config.getBinTestDir()) {
            DirectoryScanner directoryScanner = new DirectoryScanner(new File(dir), TestListResolver.getWildcard());
            if (directoryScanner.scan().getClasses().contains(className)) {
                return true;
            }
        }

        return false;
    }

}
