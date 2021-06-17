package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.listener.Coverage;

/**
 * Contains the results of the execution of a single test case (i.e., a method).
 * 
 * @author Matias Martinez
 *
 */
public class CoverageFromSingleTestUnit {
	/**
	 * Class name of class that contains the test
	 */
	protected String testClass;
	/**
	 * Name of the method that corresponds to the test
	 */
	protected String testMethod;
	/**
	 * Coverage info
	 */
	protected Coverage cov;
	/**
	 * Result of the execution: true if it's passing
	 */
	protected boolean isPassing;
	/**
	 * Indicates if the test is skipped
	 */
	protected boolean isSkip = false;

	public CoverageFromSingleTestUnit(String testClass, String method, Coverage cov) {
		super();
		this.testClass = testClass;
		this.testMethod = method;
		this.cov = cov;
	}

	public String getMethod() {
		return testMethod;
	}

	public void setMethod(String method) {
		this.testMethod = method;
	}

	public Coverage getCov() {
		return cov;
	}

	public void setCov(Coverage cov) {
		this.cov = cov;
	}

	public String getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	@Override
	public String toString() {
		return "CoverageFromSingleTestUnit [testClass=" + testClass + ", testMethod=" + testMethod + ", cov="
				+ cov.getInstructionsCovered() + "]";
	}

	public boolean isPassing() {
		return isPassing && !isSkip();
	}

	public void setIsPassing(Boolean isPassing) {
		this.isPassing = isPassing;
	}

	public boolean isSkip() {
		return isSkip;
	}

	public void setIsSkip(Boolean isSkip) {
		this.isSkip = isSkip;
	}

	public String getTestClass() {
		return testClass;
	}

	public void setTestClass(String testClass) {
		this.testClass = testClass;
	}

}
