package fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.filters;

import fr.spoonlabs.flacoco.core.test.method.StringTestMethod;
import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TestMethodFilter {

    private static final Logger logger = Logger.getLogger(TestMethodFilter.class);

    private final EnumSet<TestType> testTypes;
    private final Set<String> ignoredTests;

    public TestMethodFilter(EnumSet<TestType> testTypes, Set<String> ignoredTests) {
        this.testTypes = testTypes;
        this.ignoredTests = ignoredTests;
    }

    public List<TestMethod> acceptClass(Class<?> clazz) {
        List<TestMethod> testMethods = new ArrayList<>();

        // ignore abstract classes or ignored classes
        if (isAbstractClass(clazz) || isIgnoredClass(clazz)) {
            return testMethods;
        }

        // check all available filters
        if (isInSuiteTypes(TestType.JUNIT5_TEST)) {
            testMethods.addAll(acceptJUnit5Test(clazz));
        }
        if (isInSuiteTypes(TestType.JUNIT4_TEST)) {
            testMethods.addAll(acceptJUnit4Test(clazz));
        }
        if (isInSuiteTypes(TestType.JUNIT3_TEST)) {
            testMethods.addAll(acceptJUnit3Test(clazz));
        }

        return testMethods;
    }

    private List<TestMethod> acceptJUnit3Test(Class<?> clazz) {
        List<TestMethod> testMethods = new ArrayList<>();

        try {
            if (isPublicClass(clazz) && TestCase.class.isAssignableFrom(clazz)) {
                for (Method method : clazz.getMethods()) {
                    if (isPublicMethod(method)
                            && !isStaticMethod(method)
                            && method.getReturnType() == void.class
                            // && no arguments
                            && method.getName().startsWith("test")
                            && !isIgnoredMethod(clazz, method)
                    ) {
                        testMethods.add(new StringTestMethod(clazz.getCanonicalName(), method.getName()));
                    }
                }
            }
        } catch (NoClassDefFoundError ignore) {
            logger.warn("NoClassDefFoundError: " + clazz);
        }

        return testMethods;
    }

    private List<TestMethod> acceptJUnit4Test(Class<?> clazz) {
        List<TestMethod> testMethods = new ArrayList<>();

        try {
            for (Method method : clazz.getMethods()) {
                if (method.getAnnotation(org.junit.Test.class) != null && !isIgnoredMethod(clazz, method)) {
                    testMethods.add(new StringTestMethod(clazz.getCanonicalName(), method.getName()));
                }
            }
        } catch (NoClassDefFoundError ignore) {
            logger.warn("NoClassDefFoundError: " + clazz);
        }

        return testMethods;
    }

    private List<TestMethod> acceptJUnit5Test(Class<?> clazz) {
        List<TestMethod> testMethods = new ArrayList<>();

        try {
            // JUnit 5 allows public, protected or package-private methods, so we get all declared methods and filter out
            // the private ones
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getAnnotation(org.junit.jupiter.api.Test.class) != null
                        && !isPrivateMethod(method)
                        && !isIgnoredMethod(clazz, method)
                ) {
                    testMethods.add(new StringTestMethod(clazz.getCanonicalName(), method.getName()));
                }
            }
        } catch (NoClassDefFoundError ignore) {
            logger.warn("NoClassDefFoundError: " + clazz);
        }

        return testMethods;
    }

    private boolean isAbstractClass(Class<?> clazz) {
        return (clazz.getModifiers() & Modifier.ABSTRACT) != 0;
    }

    private boolean isPublicClass(Class<?> clazz) {
        return (clazz.getModifiers() & Modifier.PUBLIC) != 0;
    }

    private boolean isIgnoredClass(Class<?> clazz) {
        return this.ignoredTests.contains(clazz.getCanonicalName());
    }

    private boolean isIgnoredMethod(Class<?> clazz, Method method) {
        return this.ignoredTests.contains(clazz.getCanonicalName() + "#" + method.getName());
    }

    private boolean isPublicMethod(Method method) {
        return (method.getModifiers() & Modifier.PUBLIC) != 0;
    }

    private boolean isPrivateMethod(Method method) {
        return (method.getModifiers() & Modifier.PRIVATE) != 0;
    }

    private boolean isStaticMethod(Method method) {
        return (method.getModifiers() & Modifier.STATIC) != 0;
    }

    private boolean isInSuiteTypes(TestType testType) {
        return testTypes.contains(testType);
    }

}
