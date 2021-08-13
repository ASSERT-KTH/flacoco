package fr.spoonlabs.flacoco.core.test.strategies;

import fr.spoonlabs.flacoco.core.test.TestContext;

import java.util.List;

public interface TestDetectionStrategy {

    public List<TestContext> findTests();

}