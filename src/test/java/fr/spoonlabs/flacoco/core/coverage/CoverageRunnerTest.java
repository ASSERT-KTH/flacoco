package fr.spoonlabs.flacoco.core.coverage;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestDetector;
import fr.spoonlabs.flacoco.core.test.TestInformation;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CoverageRunnerTest {

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

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestInformation> tests = testDetector.findTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 8 executed lines
		assertEquals(8, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<Integer> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		List<String> executedTest = firstLineExecuted.stream().map(e -> matrix.getTests().get(e))
				.collect(Collectors.toList());

		assertEquals(4, executedTest.size());

		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));

		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));

		// This one is only executed by the sum
		Set<Integer> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTest = returnSum.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());

		assertEquals(1, executedTest.size());

		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));

		System.out.println(executedTest);

		// This line is the second if, so it's covered by all tests, except the first
		// one
		Set<Integer> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTest = secondIfExecuted.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());

		// The first one returns before
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));

		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));

		Set<Integer> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTest = oneMultCond.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));

		// This line is inside one if, so it's executed only one
		Set<Integer> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTest = oneReturnLineExecuted.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());

		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));

		Set<Integer> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTest = divisionCond.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());

		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));

		// Any test executes that
		Set<Integer> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
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
		List<TestInformation> tests = testDetector.findTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		/// let's inspect the matrix
		assertEquals(1, matrix.getFailingTestCases().size());
		assertEquals(5, matrix.getTests().size());

		// 9 executed lines
		assertEquals(9, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<Integer> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(5, firstLineExecuted.size());

		System.out.println(matrix.getResultExecution());

		Set<Integer> secondLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertEquals(2, secondLineExecuted.size());
	}

	@Test
	public void testExampleFL3() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestInformation> tests = testDetector.findTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		/// let's inspect the matrix
		assertEquals(1, matrix.getFailingTestCases().size());
		assertEquals(5, matrix.getTests().size());

		// 9 executed lines
		assertEquals(9, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<Integer> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		// The assertion fails because it cannot count the erroring test
		assertEquals(5, firstLineExecuted.size());

		Set<Integer> secondLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@21");
		assertEquals(1, secondLineExecuted.size());

		Set<Integer> failingLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@22");
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
		List<TestInformation> tests = testDetector.findTests();

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
		Set<Integer> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

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
		config.setProjectPath(new File("./examples/exampleFL4/FLtest1").getAbsolutePath());
		config.setTestFramework(FlacocoConfig.TestFramework.JUNIT5);

		CoverageRunner detector = new CoverageRunner();

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestInformation> tests = testDetector.findTests();

		assertTrue(tests.size() > 0);

		CoverageMatrix matrix = detector.getCoverageMatrix(tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 8 executed lines
		assertEquals(8, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<Integer> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");

		assertEquals(4, firstLineExecuted.size());

		List<String> executedTest = firstLineExecuted.stream().map(e -> matrix.getTests().get(e))
				.collect(Collectors.toList());

		assertEquals(4, executedTest.size());

		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));

		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));

		// This one is only executed by the sum
		Set<Integer> returnSum = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@11");

		executedTest = returnSum.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());

		assertEquals(1, executedTest.size());

		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));

		System.out.println(executedTest);

		// This line is the second if, so it's covered by all tests, except the first
		// one
		Set<Integer> secondIfExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondIfExecuted.size());

		executedTest = secondIfExecuted.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());

		// The first one returns before
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));

		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));

		Set<Integer> oneMultCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@14");
		assertEquals(2, oneMultCond.size());
		executedTest = oneMultCond.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));

		// This line is inside one if, so it's executed only one
		Set<Integer> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		executedTest = oneReturnLineExecuted.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());

		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));

		Set<Integer> divisionCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@16");
		assertEquals(1, divisionCond.size());
		executedTest = divisionCond.stream().map(e -> matrix.getTests().get(e)).collect(Collectors.toList());

		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSum"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testMul"));
		assertFalse(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testSubs"));
		assertTrue(executedTest.contains("fr.spoonlabs.FLtest1.CalculatorTest@-@testDiv"));

		// Any test executes that
		Set<Integer> modCond = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@18");
		assertNull(modCond);
	}


}