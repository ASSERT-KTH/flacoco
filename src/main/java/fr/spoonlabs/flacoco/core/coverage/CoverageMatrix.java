package fr.spoonlabs.flacoco.core.coverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.stamp_project.testrunner.listener.impl.CoverageDetailed;
import eu.stamp_project.testrunner.listener.impl.CoverageFromClass;

/**
 * This class contains the result of the execution of a set of test cases
 * 
 * @author Matias Martinez
 *
 */
public class CoverageMatrix {

	private Logger logger = Logger.getLogger(CoverageMatrix.class);

	public final static String JOIN = "@-@";

	/**
	 * Key is the line, value is a set of the test identifiers that execute that
	 * line (executed)
	 */

	protected Map<String, Set<Integer>> resultExecution = new HashMap<>();

	/**
	 * All test executed
	 */
	protected List<String> tests = new ArrayList<String>();

	/**
	 * Key is the test id, value is the Result
	 */
	protected Map<Integer, Boolean> testResult = new HashMap<Integer, Boolean>();

	/**
	 * Adds
	 * 
	 * @param lineKey
	 * @param testIndex
	 * @param instExecutedAtLineI
	 * @param testResult
	 */
	public void add(String lineKey, int testIndex, int instExecutedAtLineI, Boolean testResult) {

		Set<Integer> currentExecution = null;

		if (instExecutedAtLineI > 0) {

			if (resultExecution.containsKey(lineKey)) {
				currentExecution = resultExecution.get(lineKey);
			} else {
				currentExecution = new HashSet<>();
				resultExecution.put(lineKey, currentExecution);
			}

			currentExecution.add(testIndex);

		}

		this.testResult.put(testIndex, testResult);

	}

	/**
	 * Returns the index of the test given as parameter. That index is used as test
	 * ID
	 * 
	 * @param testKey
	 * @return
	 */
	public int getIndexTest(String testKey) {

		int exists = tests.indexOf(testKey);
		if (exists >= 0)
			return exists;
		else {
			tests.add(testKey);
			// Added at the end
			return tests.size() - 1;
		}
	}

	public void processSingleTest(CoverageFromSingleTestUnit iCovWrapper) {

		CoverageDetailed covLine = (CoverageDetailed) iCovWrapper.getCov();

		// Retrieve the key of the test
		String testKey = getTestKey(iCovWrapper);

		if (iCovWrapper.isSkip()) {
			logger.debug(
					"Ignoring skipped test: " + iCovWrapper.getTestMethod() + " from " + iCovWrapper.getTestClass());
			return;
		}

		int testIndex = this.getIndexTest(testKey);

		boolean isPassing = iCovWrapper.isPassing();

		// Let's navigate the covered class per line.
		for (String iClassNameCovered : covLine.getDetailedCoverage().keySet()) {

			// Lines covered in that class
			CoverageFromClass lines = covLine.getDetailedCoverage().get(iClassNameCovered);

			for (int iLineNumber : lines.getCov().keySet()) {

				int instExecutedAtLineI = lines.getCov().get(iLineNumber);

				String lineKey = getLineKey(iClassNameCovered, iLineNumber);
				this.add(lineKey, testIndex, instExecutedAtLineI, isPassing);

			}

		}
	}

	/**
	 * Creates a key a line
	 * 
	 * @param iClassNameCovered
	 * @param iLineNumber
	 * @return
	 */
	public String getLineKey(String iClassNameCovered, int iLineNumber) {
		return String.format("%s%s%d", iClassNameCovered, JOIN, iLineNumber);
	}

	public static String getTestKey(CoverageFromSingleTestUnit covWrapper) {
		return String.format("%s%s%s", covWrapper.getTestClass(), JOIN, covWrapper.getMethod());
	}

	public Map<String, Set<Integer>> getResultExecution() {
		return resultExecution;
	}

	public void setResultExecution(Map<String, Set<Integer>> resultExecutionNew) {
		this.resultExecution = resultExecutionNew;
	}

	public void setTests(List<String> tests) {
		this.tests = tests;
	}

	public void setTestResult(Map<Integer, Boolean> testResult) {
		this.testResult = testResult;
	}

	public List<String> getFailingTestCases() {
		List<String> failingTest = new ArrayList<>();

		for (int i = 0; i < this.getTests().size(); i++) {
			Boolean testResult = this.getTestResult().get(i);

			if (testResult == null) {
				logger.info("We could not find the results for test  #" + i + ": " + this.getTests().get(i));
			} else

			if (!testResult) {
				String testi = this.getTests().get(i);
				failingTest.add(testi);
			}
		}

		return failingTest;
	}

	public Map<Integer, Boolean> getTestResult() {
		return testResult;
	}

	public List<String> getTests() {
		return tests;
	}

}
