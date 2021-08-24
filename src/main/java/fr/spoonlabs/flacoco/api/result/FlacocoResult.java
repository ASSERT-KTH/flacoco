package fr.spoonlabs.flacoco.api.result;

import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import spoon.reflect.code.CtStatement;

import java.util.Map;
import java.util.Set;

public class FlacocoResult {

    private Map<Location, Suspiciousness> defaultSuspiciousnessMap;

    private Map<CtStatement, Suspiciousness> spoonSuspiciousnessMap;

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

    public Set<TestMethod> getFailingTests() {
        return failingTests;
    }

    public void setFailingTests(Set<TestMethod> failingTests) {
        this.failingTests = failingTests;
    }
}
