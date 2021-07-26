package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.api.Suspiciousness;
import fr.spoonlabs.flacoco.core.coverage.CoverageMatrix;
import fr.spoonlabs.flacoco.core.test.TestMethod;
import fr.spoonlabs.flacoco.localization.spectrum.formulas.OchiaiFormula;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpectrumSuspiciousComputationTest {

	private static CoverageMatrix exampleCoverageMatrix = new CoverageMatrix();

	@BeforeClass
	public static void beforeAll() {
		// test@-@[1,4] are ran in a passing test case
		TestMethod testMethod = mock(TestMethod.class);
		when(testMethod.getFullyQualifiedMethodName()).thenReturn("test1");
		when(testMethod.toString()).thenReturn("test1");
		for (int i = 0; i <= 4; i++) {
			exampleCoverageMatrix.add("test@-@" + i, testMethod, i, true);
		}
		// test@-@[1,6] are ran in a failing test case
		testMethod = mock(TestMethod.class);
		when(testMethod.getFullyQualifiedMethodName()).thenReturn("test2");
		when(testMethod.toString()).thenReturn("test2");
		for (int i = 0; i <= 6; i++) {
			exampleCoverageMatrix.add("test@-@" + i, testMethod, i, false);
		}
		// test@-@[1,9] are ran in a failing test case
		testMethod = mock(TestMethod.class);
		when(testMethod.getFullyQualifiedMethodName()).thenReturn("test4");
		when(testMethod.toString()).thenReturn("test4");
		for (int i = 0; i <= 9; i++) {
			exampleCoverageMatrix.add("test@-@" + i, testMethod, i, false);
		}
		// test@-@[1,5] are ran in a passing test case
		testMethod = mock(TestMethod.class);
		when(testMethod.getFullyQualifiedMethodName()).thenReturn("test3");
		when(testMethod.toString()).thenReturn("test3");
		for (int i = 1; i <= 5; i++) {
			exampleCoverageMatrix.add("test@-@" + i, testMethod, i, true);
		}
	}

	@Test
	public void testOchiaiComputation() {
		SpectrumSuspiciousComputation comp = new SpectrumSuspiciousComputation();

		Map<String, Suspiciousness> susp = comp.calculateSuspicious(exampleCoverageMatrix, new OchiaiFormula());

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
		}

		assertEquals(9, susp.size());

		// Line executed by all the failing test cases
		assertEquals(1.0, susp.get("test@-@6").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.81, susp.get("test@-@5").getScore(), 0.01);

		// Lines executed by just one failing test
		assertEquals(0.70, susp.get("test@-@9").getScore(), 0.01);
		assertEquals(0.70, susp.get("test@-@8").getScore(), 0.01);
		assertEquals(0.70, susp.get("test@-@7").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.70, susp.get("test@-@4").getScore(), 0.01);
		assertEquals(0.70, susp.get("test@-@3").getScore(), 0.01);
		assertEquals(0.70, susp.get("test@-@2").getScore(), 0.01);
		assertEquals(0.70, susp.get("test@-@1").getScore(), 0.01);
	}

}
