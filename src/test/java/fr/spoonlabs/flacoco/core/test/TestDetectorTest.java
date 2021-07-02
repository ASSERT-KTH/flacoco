package fr.spoonlabs.flacoco.core.test;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit4Strategy;
import fr.spoonlabs.flacoco.core.coverage.framework.JUnit5Strategy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

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

		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		String dep1 = new File("./examples/libs/junit-4.12.jar").getAbsolutePath();
		String dep2 = new File("./examples/libs/hamcrest-core-1.3.jar").getAbsolutePath();
		String dep3 = new File("./examples/libs/junit-jupiter-api-5.7.2.jar").getAbsolutePath();
		config.setClasspath(dep1 + File.pathSeparatorChar + dep2 + File.pathSeparatorChar + dep3);
	}

	@After
	public void tearDown() {
		FlacocoConfig.deleteInstance();
	}

	@Test
	public void testExampleFL1() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> testContexts = testDetector.findTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 4 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(4, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}

	@Test
	public void testExampleFL2() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL2/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> testContexts = testDetector.findTests();

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
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> testContexts = testDetector.findTests();

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
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> testContexts = testDetector.findTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 4 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(4, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit5Strategy);
	}

	@Test
	public void testExampleFL5JUnit3() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> testContexts = testDetector.findTests();

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
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> testContexts = testDetector.findTests();

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
	public void testExampleFL7() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL7SameNamedMethods/FLtest1").getAbsolutePath());

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> testContexts = testDetector.findTests();

		// Check that there is only one test context
		assertEquals(1, testContexts.size());
		// Check that there are 8 test methods in the test context
		TestContext testContext = testContexts.get(0);
		assertEquals(8, testContext.getTestMethods().size());
		// Check that the correct test framework is set
		assertTrue(testContext.getTestFrameworkStrategy() instanceof JUnit4Strategy);
	}


}