package fr.spoonlabs.flacoco.core.coverage;

import eu.stamp_project.testrunner.EntryPoint;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.TestDetector;
import fr.spoonlabs.flacoco.core.test.TestMethod;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.spoonlabs.flacoco.TestUtils.isLessThanJava11;
import static org.junit.Assert.*;

public class CoverageRunnerTest {

	@Rule
	public TemporaryFolder workspaceDir = new TemporaryFolder();

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);

		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
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

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(1, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	/**
	 * This test captures the functionality of computing the coverage even when an exception is
	 * thrown during execution
	 */
	@Test
	public void testExampleFL2() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL2/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		/// let's inspect the matrix
		assertEquals(1, matrix.getFailingTestCases().size());
		assertEquals(5, matrix.getTests().size());

		// 12 executed lines
		assertEquals(12, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(5, firstLineExecuted.size());

		Set<TestMethod> secondLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertEquals(2, secondLineExecuted.size());


		// This line is the one that throws an exception
		System.out.println(matrix.getResultExecution());
		Set<TestMethod> exceptionThrower = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@22");
		assertEquals(1, exceptionThrower.size());
	}

	@Test
	public void testExampleFL3() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		/// let's inspect the matrix
		assertEquals(1, matrix.getFailingTestCases().size());
		assertEquals(5, matrix.getTests().size());

		// 11 executed lines
		assertEquals(11, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		// The assertion fails because it cannot count the erroring test
		assertEquals(5, firstLineExecuted.size());

		Set<TestMethod> secondLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@21");
		assertEquals(1, secondLineExecuted.size());

		Set<TestMethod> failingLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@22");
		assertNull(failingLineExecuted);
		// assertEquals(0, failingLineExecuted.size());
	}

	@Test
	@Ignore
	public void testExampleFL1CoverTests() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		config.setCoverTests(true);
		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 16 executed lines
		// We have 8 from class under test + 8 from test
		assertEquals(16, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		// Now, we check that, if we don't want to cover the test, the numbers of cover
		// lines is fewer.

		config.setCoverTests(false);
		matrix = detector.getCoverageMatrix(tests);

		assertEquals(8, matrix.getResultExecution().keySet().size());
	}

	@Test
	public void testExampleFL4JUnit5() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 8 executed lines
		assertEquals(8, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(1, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	@Test
	public void testExampleFL5JUnit3() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 8 executed lines
		assertEquals(8, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(1, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	@Test
	public void testExampleFL6Mixed() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(1, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	@Test
	public void testTimeout() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setTestRunnerTimeoutInMs(1);

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		assertThrows(RuntimeException.class, () -> detector.getCoverageMatrix(tests));
	}

	@Test
	public void testJVMArgs() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setTestRunnerJVMArgs("-Xms16M");

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);
		assertEquals("-Xms16M", EntryPoint.JVMArgs);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(1, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	@Test
	public void testExampleFL7() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL7SameNamedMethods/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(8, matrix.getTests().size());
		assertEquals(2, matrix.getFailingTestCases().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(8, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(2, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(6, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(4, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(2, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(2, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	@Test
	public void testExampleFL8() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL8NotMaven/");
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));
		config.setTestRunnerVerbose(true);

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(1, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	@Test
	public void testExampleFL9() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL9NotMavenMultiple/");
		config.setSrcJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/java"));
		config.setSrcTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/test2", "./examples/exampleFL9NotMavenMultiple/test1"));
		config.setBinJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/classes"));
		config.setBinTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/test-classes2", "./examples/exampleFL9NotMavenMultiple/bin/test-classes1"));

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(8, matrix.getTests().size());
		assertEquals(2, matrix.getFailingTestCases().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(8, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(2, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(6, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(4, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(2, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(2, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	@Test
	public void testExampleFL11() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL11/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(2, matrix.getFailingTestCases().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(1, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}

	@Test
	public void testExampleFL12() {
		// We can only run this test on java version less than 11
		// since java 11 dropped support for compliance level 1.4
		Assume.assumeTrue(isLessThanJava11());

		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL12Compliance4/FLtest1").getAbsolutePath());
		config.setComplianceLevel(4);

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestContext> tests = testDetector.getTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 8 executed lines
		assertEquals(8, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<TestMethod> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/enum/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		Set<String> executedTestKeys = firstLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testMul"));

		// This one is only executed by the sum
		Set<TestMethod> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/enum/Calculator@-@11");

		executedTestKeys = returnSum.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertEquals(1, returnSum.size());

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSum"));

		// This line is the second if, so it's covered by all tests, except the first one
		Set<TestMethod> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/enum/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTestKeys = secondIfExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		// The first one returns before
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSum"));

		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testMul"));

		Set<TestMethod> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/enum/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTestKeys = oneMultCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testDiv"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testMul"));

		// This line is inside one if, so it's executed only one
		Set<TestMethod> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/enum/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTestKeys = oneReturnLineExecuted.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSum"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSubs"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testDiv"));

		Set<TestMethod> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/enum/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTestKeys = divisionCond.stream()
				.map(TestMethod::getFullyQualifiedMethodName).collect(Collectors.toSet());

		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSum"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testMul"));
		assertFalse(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testSubs"));
		assertTrue(executedTestKeys.contains("fr.spoonlabs.FLtest1.enum.CalculatorTest#testDiv"));

		// Any test executes that
		Set<TestMethod> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/enum/Calculator@-@18");
		assertNull(modCond);
	}

}