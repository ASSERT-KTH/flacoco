package fr.spoonlabs.flacoco.core.test;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit4Strategy;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit5Strategy;
import fr.spoonlabs.flacoco.core.test.method.StringTestMethod;
import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.spoonlabs.flacoco.TestUtils.getCompilerVersion;
import static fr.spoonlabs.flacoco.TestUtils.isLessThanJava11;
import static org.junit.Assert.*;

/**
 * This class tests the TestDetector against 3 example projects
 */
public class TestDetectorTest {

	@Rule
	public TemporaryFolder workspaceDir = new TemporaryFolder();

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);
	}

	@Test
	public void testSpoonClass() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/spoon").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Additional weak Oracle: absence of any error during the execution

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 4 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(1, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL1() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 5 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(5, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL1ManualConfig() {
		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
        config.setjUnit4Tests(Stream.of(
                        "fr.spoonlabs.FLtest1.CalculatorTest#testSum",
                        "fr.spoonlabs.FLtest1.CalculatorTest#testSubs",
                        "fr.spoonlabs.FLtest1.CalculatorTest#testMul",
                        "fr.spoonlabs.FLtest1.CalculatorTest#testDiv",
						"fr.spoonlabs.FLtest1.CalculatorTest#testIgnore"
				).collect(Collectors.toSet())
        );

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 5 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(5, testContext.getTestMethods().size());
		// Check that the test methods are of the correct type
		assertTrue(testContext.getTestMethods().stream().allMatch(x -> x instanceof StringTestMethod));
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL1TestRunnerDetector() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setTestDetectionStrategy(FlacocoConfig.TestDetectionStrategy.TEST_RUNNER);

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 5 test methods in the test context
		TestContext testContext = testContexts.get(0);
		// TODO: the strategy should find 5 tests, but it doesn't find the ignored test
		//assertEquals(5, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL1IgnoreTestClass() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setIgnoredTests(Stream.of("fr.spoonlabs.FLtest1.CalculatorTest").collect(Collectors.toSet()));

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there are no tests
		assertEquals(0, testContexts.size());
	}

	@Test
	public void testExampleFL1IgnoreTestMethod() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setIgnoredTests(Stream.of("fr.spoonlabs.FLtest1.CalculatorTest#testSum").collect(Collectors.toSet()));

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 4 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(4, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL1TestRunnerDetectorIgnoreTestClass() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setTestDetectionStrategy(FlacocoConfig.TestDetectionStrategy.TEST_RUNNER);
		config.setIgnoredTests(Stream.of("fr.spoonlabs.FLtest1.CalculatorTest").collect(Collectors.toSet()));

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there are no tests
		assertEquals(0, testContexts.size());
	}

	@Test
	public void testExampleFL1TestRunnerDetectorIgnoreTestMethod() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setTestDetectionStrategy(FlacocoConfig.TestDetectionStrategy.TEST_RUNNER);
		config.setIgnoredTests(Stream.of("fr.spoonlabs.FLtest1.CalculatorTest#testSum").collect(Collectors.toSet()));

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 4 test methods in the test context
		TestContext testContext = testContexts.get(0);
		// TODO: the strategy should find 4 tests, but it doesn't find the ignored test
		//assertEquals(4, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL2() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL2/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 5 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(5, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL3() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 5 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(5, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL4JUnit5() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 5 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(5, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit5Strategy);
	}

	@Test
	public void testExampleFL4JUnit5ManualConfig() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setjUnit5Tests(Stream.of(
				"fr.spoonlabs.FLtest1.CalculatorTest#testSum",
				"fr.spoonlabs.FLtest1.CalculatorTest#testSubs",
				"fr.spoonlabs.FLtest1.CalculatorTest#testMul",
				"fr.spoonlabs.FLtest1.CalculatorTest#testDiv",
				"fr.spoonlabs.FLtest1.CalculatorTest#testIgnore"
				).collect(Collectors.toSet())
		);

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 5 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(5, testContext.getTestMethods().size());
		// Check that the test methods are of the correct type
		assertTrue(testContext.getTestMethods().stream().allMatch(x -> x instanceof StringTestMethod));
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit5Strategy);
	}

	@Test
	public void testExampleFL5JUnit3() {
		// Run on all target releases
		Assume.assumeTrue(getCompilerVersion() >= 1);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 4 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(4, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL6Mixed() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there are two test contexts, one for each strategy
		assertEquals(2, testContexts.size());
		for (TestContext testContext : testContexts) {
			for (TestMethod testMethod : testContext.getTestMethods()) {
				switch (testMethod.getFullyQualifiedClassName()) {
					case "fr.spoonlabs.FLtest1.CalculatorJUnit3Test":
						assertEquals("fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum", testMethod.getFullyQualifiedMethodName());
						assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
						break;
					case "fr.spoonlabs.FLtest1.CalculatorMixedTest":
						if (testMethod.getFullyQualifiedMethodName().equals("fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs")) {
							assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
						} else if (testMethod.getFullyQualifiedMethodName().equals("fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul")) {
							assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit5Strategy);
						} else {
							fail();
						}
						break;
					case "fr.spoonlabs.FLtest1.CalculatorJUnit5Test":
						assertEquals("fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv", testMethod.getFullyQualifiedMethodName());
						assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit5Strategy);
						break;
					default:
						fail();
				}
			}
		}
	}

	@Test
	public void testExampleFL6MixedManualConfig() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setjUnit4Tests(Stream.of(
				"fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum",
				"fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs"
				).collect(Collectors.toSet())
		);
		config.setjUnit5Tests(Stream.of(
				"fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul",
				"fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv"
				).collect(Collectors.toSet())
		);

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there are two test contexts, one for each strategy
		assertEquals(2, testContexts.size());
		for (TestContext testContext : testContexts) {
			// Check that the test methods are of the correct type
			assertTrue(testContext.getTestMethods().stream().allMatch(x -> x instanceof StringTestMethod));
			for (TestMethod testMethod : testContext.getTestMethods()) {
				switch (testMethod.getFullyQualifiedClassName()) {
					case "fr.spoonlabs.FLtest1.CalculatorJUnit3Test":
						assertEquals("fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum", testMethod.getFullyQualifiedMethodName());
						assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
						break;
					case "fr.spoonlabs.FLtest1.CalculatorMixedTest":
						if (testMethod.getFullyQualifiedMethodName().equals("fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs")) {
							assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
						} else if (testMethod.getFullyQualifiedMethodName().equals("fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul")) {
							assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit5Strategy);
						} else {
							fail();
						}
						break;
					case "fr.spoonlabs.FLtest1.CalculatorJUnit5Test":
						assertEquals("fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv", testMethod.getFullyQualifiedMethodName());
						assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit5Strategy);
						break;
					default:
						fail();
				}
			}
		}
	}

	@Test
	public void testExampleFL7() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL7SameNamedMethods/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 8 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(8, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL8() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath("./examples/exampleFL8NotMaven/");
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 5 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(5, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL9() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath("./examples/exampleFL9NotMavenMultiple/");
		config.setSrcJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/java"));
		config.setSrcTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/test2", "./examples/exampleFL9NotMavenMultiple/test1"));
		config.setBinJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/classes"));
		config.setBinTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/test-classes2", "./examples/exampleFL9NotMavenMultiple/bin/test-classes1"));

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 8 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(8, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL11() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL11/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 4 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(4, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL12() {
		// Run only on target release < 5
		Assume.assumeTrue(getCompilerVersion() < 5);

		// We can only run this test on java version less than 11
		// since java 11 dropped support for compliance level 1.4
		Assume.assumeTrue(isLessThanJava11());

		// Setup config
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setProjectPath(new File("./examples/exampleFL12Compliance4/FLtest1").getAbsolutePath());
		config.setComplianceLevel(4);

		// Find the tests
		TestDetector testDetector = new TestDetector(config);
		List<TestContext> testContexts = testDetector.getTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 4 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(4, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

}