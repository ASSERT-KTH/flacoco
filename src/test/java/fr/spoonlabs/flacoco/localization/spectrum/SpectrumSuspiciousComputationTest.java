package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.core.coverage.CoverageMatrix;
import fr.spoonlabs.flacoco.localization.spectrum.formulas.OchiaiFormula;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SpectrumSuspiciousComputationTest {

	private static CoverageMatrix exampleCoverageMatrix = new CoverageMatrix();

	@BeforeClass
	public static void beforeAll() {
		// test@-@[1,4] are ran in a passing test case
		int idx = exampleCoverageMatrix.getIndexTest("test1");
		for (int i = 0; i <= 4; i++) {
			exampleCoverageMatrix.add("test@-@" + i, idx, i, true);
		}
		// test@-@[1,6] are ran in a passing test case
		idx = exampleCoverageMatrix.getIndexTest("test2");
		for (int i = 0; i <= 6; i++) {
			exampleCoverageMatrix.add("test@-@" + i, idx, i, false);
		}
		// test@-@[1,5] are ran in a passing test case
		idx = exampleCoverageMatrix.getIndexTest("test3");
		for (int i = 1; i <= 5; i++) {
			exampleCoverageMatrix.add("test@-@" + i, idx, i, true);
		}
	}

	@Test
	public void testOchiaiComputation() {
		SpectrumSuspiciousComputation comp = new SpectrumSuspiciousComputation();

		Map<String, Double> susp = comp.calculateSuspicious(exampleCoverageMatrix, new OchiaiFormula());

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("test@-@6"), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("test@-@5"), 0.01);

		// Lines executed by all test
		assertEquals(0.57, susp.get("test@-@4"), 0.01);
		assertEquals(0.57, susp.get("test@-@3"), 0.01);
		assertEquals(0.57, susp.get("test@-@2"), 0.01);
		assertEquals(0.57, susp.get("test@-@1"), 0.01);
	}

}
