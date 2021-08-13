package fr.spoonlabs.flacoco.core.test.strategies.classloader.finder;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit4Strategy;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit5Strategy;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.classes.impl.ClassloaderFinder;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.filters.TestMethodFilter;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.filters.TestType;

import java.net.URLClassLoader;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFinderRunner implements Callable<List<TestContext>> {

    FlacocoConfig config = FlacocoConfig.getInstance();

    @Override
    public List<TestContext> call() throws Exception {
        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        ClassloaderFinder classloaderFinder = new ClassloaderFinder(classLoader);

        // collect JUnit4 compatible tests
        Processor processor = new Processor(
                classloaderFinder,
                new TestMethodFilter(EnumSet.of(TestType.JUNIT3_TEST, TestType.JUNIT4_TEST), config.getIgnoredTests())
        );
        TestContext jUnit4Context = new TestContext(JUnit4Strategy.getInstance());
        jUnit4Context.addTestMethods(processor.process());

        // collect JUnit5 compatible tests
        processor = new Processor(
                classloaderFinder,
                new TestMethodFilter(EnumSet.of(TestType.JUNIT5_TEST), config.getIgnoredTests())
        );
        TestContext jUnit5Context = new TestContext(JUnit5Strategy.getInstance());
        jUnit5Context.addTestMethods(processor.process());

        // We only want to return those that have test units
        return Stream.of(jUnit4Context, jUnit5Context)
                .filter(x -> !x.getTestMethods().isEmpty())
                .collect(Collectors.toList());
    }
}
