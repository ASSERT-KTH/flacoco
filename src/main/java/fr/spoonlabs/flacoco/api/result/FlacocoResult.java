package fr.spoonlabs.flacoco.api.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import spoon.reflect.code.CtStatement;

/**
 * The result of flacoco.
 * <p>
 * Contains a mapping between suspicious {@link Location} and their
 * {@link Suspiciousness} values.
 * <p>
 * If {@link fr.spoonlabs.flacoco.core.config.FlacocoConfig} has the parameter
 * `computeSpoonResults` enabled, a mapping betweek {@link CtStatement} and
 * {@link Suspiciousness}, as well as the mapping between {@link Location} and
 * {@link CtStatement} can be found under `getSpoonSuspiciousnessMap()` and
 * `getLocationStatementMap()` respectively.
 * <p>
 * Otherwise, both these getter <b>will return null</b>.
 */
public class FlacocoResult {

	private Map<Location, Suspiciousness> defaultSuspiciousnessMap;

	private Map<CtStatement, Suspiciousness> spoonSuspiciousnessMap;

	private Map<Location, CtStatement> locationStatementMap;

	private Set<TestMethod> failingTests;

	private Set<TestMethod> executedTests;

	/**
	 * Returns an ordered mapping from {@link Location} to a {@link Suspiciousness}.
	 * Note that the mapping's iteration follows a descending order, from most to
	 * least suspicious
	 * 
	 * @return The ordered mapping
	 */
	public Map<Location, Suspiciousness> getDefaultSuspiciousnessMap() {
		return defaultSuspiciousnessMap;
	}

	public void setDefaultSuspiciousnessMap(Map<Location, Suspiciousness> defaultSuspiciousnessMap) {
		this.defaultSuspiciousnessMap = defaultSuspiciousnessMap;
	}

	/**
	 * Returns an ordered mapping from {@link CtStatement} to a
	 * {@link Suspiciousness}. Note that the mapping's iteration follows a
	 * descending order, from most to least suspicious
	 * 
	 * @return The ordered mapping
	 */
	public Map<CtStatement, Suspiciousness> getSpoonSuspiciousnessMap() {
		return spoonSuspiciousnessMap;
	}

	public void setSpoonSuspiciousnessMap(Map<CtStatement, Suspiciousness> spoonSuspiciousnessMap) {
		this.spoonSuspiciousnessMap = spoonSuspiciousnessMap;
	}

	/**
	 * Returns a mapping from {@link Location} to {@link CtStatement} computed by
	 * the {@link fr.spoonlabs.flacoco.utils.spoon.SpoonConverter}
	 * 
	 * @return The convertion mapping
	 */
	public Map<Location, CtStatement> getLocationStatementMap() {
		return locationStatementMap;
	}

	/**
	 * Returns a ranked list of suspicious location in descending order, from most
	 * to least suspicious
	 * 
	 * @return The ranked list of locations
	 */
	public List<Location> getSuspiciousLocationList() {
		return new ArrayList<>(getDefaultSuspiciousnessMap().keySet());
	}

	public void setLocationStatementMap(Map<Location, CtStatement> locationStatementMap) {
		this.locationStatementMap = locationStatementMap;
	}

	public Set<TestMethod> getFailingTests() {
		return failingTests;
	}

	public void setFailingTests(Set<TestMethod> failingTests) {
		this.failingTests = failingTests;
	}

	public Set<TestMethod> getExecutedTests() {
		return executedTests;
	}

	public void setExecutedTests(Set<TestMethod> executedTests) {
		this.executedTests = executedTests;
	}
}
