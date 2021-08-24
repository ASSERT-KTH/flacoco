package fr.spoonlabs.flacoco.api.result;

import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import spoon.reflect.code.CtStatement;

import java.util.Map;
import java.util.Set;

/**
 * The result of flacoco.
 * <p>
 * Contains a mapping between suspicious {@link Location} and their {@link Suspiciousness} values.
 * <p>
 * If {@link fr.spoonlabs.flacoco.core.config.FlacocoConfig} has the parameter `computeSpoonResults` enabled,
 * a mapping betweek {@link CtStatement} and {@link Suspiciousness}, as well as the mapping between
 * {@link Location} and {@link CtStatement} can be found under `getSpoonSuspiciousnessMap()` and `getLocationStatementMap()` respectively.
 * <p>
 * Otherwise, both these getter <b>will return null</b>.
 */
public class FlacocoResult {

    private Map<Location, Suspiciousness> defaultSuspiciousnessMap;

    private Map<CtStatement, Suspiciousness> spoonSuspiciousnessMap;

    private Map<Location, CtStatement> locationStatementMap;

    private Set<TestMethod> failingTests;

    public Map<Location, Suspiciousness> getDefaultSuspiciousnessMap() {
        return defaultSuspiciousnessMap;
    }

    public void setDefaultSuspiciousnessMap(Map<Location, Suspiciousness> defaultSuspiciousnessMap) {
        this.defaultSuspiciousnessMap = defaultSuspiciousnessMap;
    }

    public Map<CtStatement, Suspiciousness> getSpoonSuspiciousnessMap() {
        return spoonSuspiciousnessMap;
    }

    public void setSpoonSuspiciousnessMap(Map<CtStatement, Suspiciousness> spoonSuspiciousnessMap) {
        this.spoonSuspiciousnessMap = spoonSuspiciousnessMap;
    }

    public Map<Location, CtStatement> getLocationStatementMap() {
        return locationStatementMap;
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
}
