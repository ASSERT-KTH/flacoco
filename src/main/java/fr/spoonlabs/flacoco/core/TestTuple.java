package fr.spoonlabs.flacoco.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Matias Martinez
 *
 */
public class TestTuple {
	public String testClassToBeAmplified;

	public List<String> testMethodsToBeAmplified;

	public Map<String, Object> metadata = new HashMap<String, Object>();

	public TestTuple(String testClassToBeAmplified, List<String> testMethodsToBeAmplified) {
		super();
		this.testClassToBeAmplified = testClassToBeAmplified;
		this.testMethodsToBeAmplified = testMethodsToBeAmplified;
	}

	public String getTestClassToBeAmplified() {
		return testClassToBeAmplified;
	}

	public void setTestClassToBeAmplified(String testClassToBeAmplified) {
		this.testClassToBeAmplified = testClassToBeAmplified;
	}

	public List<String> getTestMethodsToBeAmplified() {
		return testMethodsToBeAmplified;
	}

	public void setTestMethodsToBeAmplified(List<String> testMethodsToBeAmplified) {
		this.testMethodsToBeAmplified = testMethodsToBeAmplified;
	}

}
