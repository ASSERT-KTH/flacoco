package fr.spoonlabs.spfl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.junit.Test;

import eu.stamp_project.testrunner.runner.coverage.JUnit4JacocoRunner;
import eu.stamp_project.testrunner.runner.coverage.JacocoRunner;
import fr.spoonlabs.spfl.core.CoverageRunner;
import fr.spoonlabs.spfl.core.SuspiciousComputation;
import fr.spoonlabs.spfl.entities.MatrixCoverage;
import fr.spoonlabs.spfl.formulas.OchiaiFormula;
import fr.spoonlabs.test_framework.TestDetector;
import fr.spoonlabs.test_framework.TestTuple;
import spoon.Launcher;

/**
 * 
 * @author Matias Martinez
 *
 */
public class CoverageTest {

	TestDetector testDetector = new TestDetector();

	@Test
	public void test1FailingTest() throws Exception {

		String dep1 = new File("./examples/libs/junit-4.12.jar").getAbsolutePath();
		String dep2 = new File("./examples/libs/hamcrest-core-1.3.jar").getAbsolutePath();

		String projectLocation = new File("./examples/exampleFL1/FLtest1").getAbsolutePath();
		org.apache.log4j.LogManager.getRootLogger().setLevel(Level.DEBUG);
		CoverageRunner detector = new CoverageRunner();

		String dependencies = dep1 + File.separator + dep2;

		// We create the model
		Launcher laucher = new Launcher();
		laucher.addInputResource(projectLocation + File.separator + "src/main");
		laucher.addInputResource(projectLocation + File.separator + "src/test");
		laucher.buildModel();

		// We find for test
		List<TestTuple> tests = testDetector.findTest(laucher.getFactory());

		assertTrue(tests.size() > 0);

		String pathToClasses = projectLocation + File.separator + "target/classes/";
		String pathToTestClasses = projectLocation + File.separator + "target/test-classes/";

		JacocoRunner runner = new JUnit4JacocoRunner(pathToClasses, pathToTestClasses);

		MatrixCoverage matrix = detector.getCoverageMatrix(runner, dependencies, pathToClasses, pathToTestClasses,
				tests);

		// verify nr of test
		assertEquals(4, matrix.getTests().size());
		assertEquals(1, matrix.getFailingTestCases().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<Integer> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@10");
		assertEquals(4, firstLineExecuted.size());

		// This line is the second if, so it's covered by all tests, except the first
		// one
		Set<Integer> secondLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		assertEquals(3, secondLineExecuted.size());

		// This line is inside one if, so it's executed only one
		Set<Integer> oneReturnLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@15");
		assertEquals(1, oneReturnLineExecuted.size());

		// Now let's compute the suspicious

		SuspiciousComputation flcalc = new SuspiciousComputation();

		Map<String, Double> susp = flcalc.calculateSuspicious(matrix, new OchiaiFormula());

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failling
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15"), 0);

		// Line executed by failing and passing
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12"), 0.1);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10"), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@5"), 0);
	}

	/**
	 * This test exposes one limitation, the results from a method that throws an
	 * Exception is empty (any line is marked as executed)
	 * 
	 * @throws Exception
	 */
	@Test
	public void test2NPE() throws Exception {

		String dep1 = new File("./examples/libs/junit-4.12.jar").getAbsolutePath();
		String dep2 = new File("./examples/libs/hamcrest-core-1.3.jar").getAbsolutePath();

		String projectLocation = new File("./examples/exampleFL2/FLtest1").getAbsolutePath();

		CoverageRunner detector = new CoverageRunner();
		org.apache.log4j.LogManager.getRootLogger().setLevel(Level.DEBUG);
		String dependencies = dep1 + File.separator + dep2;

		// We create the model
		Launcher laucher = new Launcher();
		laucher.addInputResource(projectLocation + File.separator + "src/main");
		laucher.addInputResource(projectLocation + File.separator + "src/test");
		laucher.buildModel();

		// We find for test
		List<TestTuple> tests = testDetector.findTest(laucher.getFactory());

		assertTrue(tests.size() > 0);

		assertEquals(1, tests.size());

		assertEquals(5, tests.get(0).getTestMethodsToBeAmplified().size());

		//

		String pathToClasses = projectLocation + File.separator + "target/classes/";
		String pathToTestClasses = projectLocation + File.separator + "target/test-classes/";

		JacocoRunner runner = new JUnit4JacocoRunner(pathToClasses, pathToTestClasses);

		MatrixCoverage matrix = detector.getCoverageMatrix(runner, dependencies, pathToClasses, pathToTestClasses,
				tests);

		/// let's inspect the matrix
		assertEquals(1, matrix.getFailingTestCases().size());
		assertEquals(5, matrix.getTests().size());

		// 10 executed lines
		assertEquals(10, matrix.getResultExecution().keySet().size());

		// This line is the first if, so it's covered by all tests
		Set<Integer> firstLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@12");
		// The assertion fails because it cannot count the erroring test
		assertEquals(5, firstLineExecuted.size());

		Set<Integer> secondLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@21");
		assertEquals(1, secondLineExecuted.size());

		Set<Integer> failingLineExecuted = matrix.getResultExecution().get("fr/spoonlabs/FLtest1/Calculator@-@22");
		assertEquals(0, failingLineExecuted.size());

		/// now suspicious

		SuspiciousComputation flcalc = new SuspiciousComputation();

		Map<String, Double> susp = flcalc.calculateSuspicious(matrix, new OchiaiFormula());

		for (String line : susp.keySet()) {
			System.out.println(" " + line + " " + susp.get(line));
		}
		// When there is NPE, the trace is not recorded. the assertion fails because it
		// only captures the coverage on the class's constructor
		assertTrue(susp.keySet().size() > 2);

	}

}
