package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.listener.CoveredTestResultPerTestMethod;
import eu.stamp_project.testrunner.listener.impl.CoverageDetailed;
import fr.spoonlabs.flacoco.core.test.TestMethod;

/**
 * Contains the results of the execution of a single test case (i.e., a method).
 *
 * @author Matias Martinez
 */
public class CoverageFromSingleTestUnit {
	/**
	 * TestMethod information
	 */
	protected TestMethod testMethod;

	/**
	 * Coverage info
	 */
	protected CoveredTestResultPerTestMethod coveredTestResultPerTestMethod;

	/**
	 * Result of the execution: true if it's passing
	 */
	protected boolean isPassing;

	/**
	 * Indicates if the test is skipped
	 */
	protected boolean isSkip;

	public CoverageFromSingleTestUnit(TestMethod testMethod, CoveredTestResultPerTestMethod result) {
		this.testMethod = testMethod;
		this.coveredTestResultPerTestMethod = result;

		this.isPassing = result.getPassingTests().contains(testMethod.getFullyQualifiedMethodName())
				&& result.getFailingTests().stream().map(x -> x.testClassName + "#" + x.testCaseName)
				.noneMatch(x -> x.equals(testMethod.getFullyQualifiedMethodName()))
				&& result.getAssumptionFailingTests().stream().map(x -> x.testClassName + "#" + x.testCaseName)
				.noneMatch(x -> x.equals(testMethod.getFullyQualifiedMethodName()));

		this.isSkip = result.getIgnoredTests().contains(testMethod.getFullyQualifiedMethodName());
	}

	public TestMethod getTestMethod() {
		return testMethod;
	}

	public CoverageDetailed getCov() {
		return (CoverageDetailed) coveredTestResultPerTestMethod.getCoverageOf(testMethod.getFullyQualifiedMethodName());
	}

	public CoveredTestResultPerTestMethod getCoveredTestResultPerTestMethod() {
		return coveredTestResultPerTestMethod;
	}

	public boolean isPassing() {
		return isPassing;
	}

	public boolean isSkip() {
		return isSkip;
	}

	@Override
	public String toString() {
		return "CoverageFromSingleTestUnit{" +
				"testMethod=" + testMethod +
				", cov=" + getCov() +
				", passing=" + isPassing() +
				", skip=" + isSkip() +
				'}';
	}

}