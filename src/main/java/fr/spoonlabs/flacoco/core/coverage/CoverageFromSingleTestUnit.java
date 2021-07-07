package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.listener.Coverage;
import eu.stamp_project.testrunner.listener.impl.CoverageDetailed;
import fr.spoonlabs.flacoco.core.test.TestMethod;

/**
 * Contains the results of the execution of a single test case (i.e., a method).
 * 
 * @author Matias Martinez
 *
 */
public class CoverageFromSingleTestUnit {
	/**
	 * TestMethod information
	 */
	protected TestMethod testMethod;
	/**
	 * Coverage info
	 */
	protected CoverageDetailed cov;
	/**
	 * Result of the execution: true if it's passing
	 */
	protected boolean isPassing;
	/**
	 * Indicates if the test is skipped
	 */
	protected boolean isSkip = false;

	public CoverageFromSingleTestUnit(TestMethod testMethod, CoverageDetailed cov) {
		this.testMethod = testMethod;
		this.cov = cov;
	}

	public TestMethod getTestMethod() {
		return testMethod;
	}

	public CoverageDetailed getCov() {
		return cov;
	}

	public boolean isPassing() {
		return isPassing;
	}

	public void setPassing(boolean passing) {
		isPassing = passing;
	}

	public boolean isSkip() {
		return isSkip;
	}

	public void setSkip(boolean skip) {
		isSkip = skip;
	}

	@Override
	public String toString() {
		return "CoverageFromSingleTestUnit{" +
				"testMethod=" + testMethod +
				", cov=" + cov +
				", isPassing=" + isPassing +
				", isSkip=" + isSkip +
				'}';
	}
}
