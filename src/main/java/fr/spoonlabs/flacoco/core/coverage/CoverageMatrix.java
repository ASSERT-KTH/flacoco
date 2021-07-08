package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.listener.impl.CoverageDetailed;
import eu.stamp_project.testrunner.listener.impl.CoverageFromClass;
import fr.spoonlabs.flacoco.core.test.TestMethod;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains the result of the execution of a set of test units
 *
 * @author Matias Martinez
 */
public class CoverageMatrix {

	private Logger logger = Logger.getLogger(CoverageMatrix.class);

	public final static String JOIN = "@-@";

	/**
	 * Key is the line, value is a set of test methods that execute that  line
	 */
	protected Map<String, Set<TestMethod>> resultExecution = new HashMap<>();

	/**
	 * Map between executed test methods and their result. True if passing, false is failing.
	 */
	protected Map<TestMethod, Boolean> tests = new HashMap<>();

	/**
	 * Creates the key for a line of a given class
	 *
	 * @param iClassNameCovered
	 * @param iLineNumber
	 * @return The key for iLineNumber in iClassNameCovered
	 */
	public String getLineKey(String iClassNameCovered, int iLineNumber) {
		return String.format("%s%s%d", iClassNameCovered, JOIN, iLineNumber);
	}

	/**
	 * Processes a wrapper for the coverage from a single test unit
	 *
	 * @param iCovWrapper
	 */
	public void processSingleTest(CoverageFromSingleTestUnit iCovWrapper) {
		CoverageDetailed covLine = iCovWrapper.getCov();

		if (iCovWrapper.isSkip()) {
			logger.debug("Ignoring skipped test: " + iCovWrapper.getTestMethod().getFullyQualifiedMethodName());
			return;
		}

		boolean isPassing = iCovWrapper.isPassing();

		// Let's navigate the covered class per line.
		for (String iClassNameCovered : covLine.getDetailedCoverage().keySet()) {

			// Lines covered in that class
			CoverageFromClass lines = covLine.getDetailedCoverage().get(iClassNameCovered);

			for (int iLineNumber : lines.getCov().keySet()) {

				int instExecutedAtLineI = lines.getCov().get(iLineNumber);

				String lineKey = getLineKey(iClassNameCovered, iLineNumber);
				this.add(lineKey, iCovWrapper.getTestMethod(), instExecutedAtLineI, isPassing);

			}

		}
	}

	public Map<String, Set<TestMethod>> getResultExecution() {
		return resultExecution;
	}

	public Map<TestMethod, Boolean> getTests() {
		return tests;
	}

	public List<TestMethod> getFailingTestCases() {
		return this.tests.entrySet().stream().filter(x -> !x.getValue())
				.map(Map.Entry::getKey).collect(Collectors.toList());
	}

	/**
	 * Auxiliary method to introduce the gathered information about a test unit run in the coverage matrix
	 *
	 * The modifier is public for testing purposes
	 * @param lineKey
	 * @param testMethod
	 * @param instExecutedAtLineI
	 * @param testResult
	 */
	public void add(String lineKey, TestMethod testMethod, int instExecutedAtLineI, Boolean testResult) {
		if (instExecutedAtLineI > 0) {
			Set<TestMethod> currentExecution;

			if (this.resultExecution.containsKey(lineKey)) {
				currentExecution = this.resultExecution.get(lineKey);
			} else {
				currentExecution = new HashSet<>();
				this.resultExecution.put(lineKey, currentExecution);
			}

			currentExecution.add(testMethod);
		}

		this.tests.put(testMethod, testResult);
	}

}
