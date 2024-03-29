package fr.spoonlabs.flacoco.core.test.strategies.testrunner;

import eu.stamp_project.testrunner.test_framework.TestFramework;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit4Strategy;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit5Strategy;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestDetector;
import fr.spoonlabs.flacoco.core.test.method.SpoonTestMethod;
import fr.spoonlabs.flacoco.core.test.strategies.TestDetectionStrategy;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.declaration.CtType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builds a Spoon model and uses test-runner's test framework detection API
 * to detect tests and their corresponding frameworks
 * <p>
 * Note: this strategy is considerably slower than ClasspathStrategy, and has
 * some identified bugs (see https://github.com/SpoonLabs/flacoco/issues/80).
 * Use at own risk.
 */
public class TestRunnerStrategy implements TestDetectionStrategy {

    private Logger logger = Logger.getLogger(TestRunnerStrategy.class);
    private FlacocoConfig config;

    public TestRunnerStrategy(FlacocoConfig config) {
        this.config = config;
    }

    @Override
    public List<TestContext> findTests() {
        // Create Spoon model to retrieve information about the tests
        Launcher launcher = new Launcher();
        for (String dir : config.getSrcTestDir())
            launcher.addInputResource(dir);
        launcher.getEnvironment().setComplianceLevel(config.getComplianceLevel());
        launcher.buildModel();

        // Init test framework
        TestFramework.init(launcher.getFactory());

        TestContext jUnit4Context = new TestContext(new JUnit4Strategy(config));
        TestContext jUnit5Context = new TestContext(new JUnit5Strategy(config));

        for (CtType<?> ctType : TestFramework.getAllTestClasses()) {

            if (ctType.isAbstract()) {
                continue;
            }
            // avoid passing non-qualified class names to test-runner
            if (ctType.getPackage().isUnnamedPackage()) {
                logger.warn("TestDetector was not able to retrieve the fully qualified class name of : " + ctType.getQualifiedName());
                continue;
            }

            // Add JUnit4 methods to jUnit4Context
            jUnit4Context.addTestMethods(
                    TestFramework.getAllTest(ctType).stream().filter(TestFramework::isJUnit4)
                            .map(ctMethod -> new SpoonTestMethod(ctType, ctMethod))
                            .filter(x -> !TestDetector.isIgnored(x, config.getIgnoredTests()))
                            .collect(Collectors.toList())
            );

            // Add JUnit5 methods to jUnit5Context
            jUnit5Context.addTestMethods(
                    TestFramework.getAllTest(ctType).stream().filter(TestFramework::isJUnit5)
                            .map(ctMethod -> new SpoonTestMethod(ctType, ctMethod))
                            .filter(x -> !TestDetector.isIgnored(x, config.getIgnoredTests()))
                            .collect(Collectors.toList())
            );
        }

        // We only want to return those that have test units
        return Stream.of(jUnit4Context, jUnit5Context)
                .filter(x -> !x.getTestMethods().isEmpty())
                .collect(Collectors.toList());
    }
}
