package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.api.Suspiciousness;
import fr.spoonlabs.flacoco.core.coverage.CoverageMatrix;
import fr.spoonlabs.flacoco.core.test.TestMethod;
import fr.spoonlabs.flacoco.localization.spectrum.formulas.Formula;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Computes the suspiciousness given a code coverage matrix and a formula.
 *
 * @author Matias Martinez
 */
public class SpectrumSuspiciousComputation {

	/**
	 * @param matrix  matrix with the coverage
	 * @param formula the formula to compute the suspiciousness
	 * @return a map where the keys are the lines and the values are the suspicious
	 * values
	 */
	public Map<String, Suspiciousness> calculateSuspicious(CoverageMatrix matrix, Formula formula) {
		return calculateSuspicious(matrix, formula, false);
	}

	/**
	 * @param matrix                        matrix with the coverage
	 * @param formula                       the formula to compute the
	 *                                      suspiciousness
	 * @param includeSuspiciousEqualsToZero indicates if the suspicious list
	 *                                      contains lines with suspicious equals to
	 *                                      zero
	 * @return a map where the keys are the lines and the values are the suspicious
	 * values
	 */
	public Map<String, Suspiciousness> calculateSuspicious(CoverageMatrix matrix, Formula formula,
	                                                       boolean includeSuspiciousEqualsToZero) {

		Map<String, Suspiciousness> result = new HashMap<>();
		// For each line of code to analyze
		for (String keyline : matrix.getResultExecution().keySet()) {
			List<TestMethod> testsPassingExecuting = new ArrayList<>();
			List<TestMethod> testsFailingExecuting = new ArrayList<>();

			int nrTestPassingNotExecuting = 0;
			int nrTestFailingNotExecuting = 0;

			Set<TestMethod> currentExecution = matrix.getResultExecution().get(keyline);

			// For each test
			for (TestMethod testMethod : matrix.getTests().keySet()) {
				Boolean iTestPassing = matrix.getTests().get(testMethod);
				Boolean nrExecuted = currentExecution.contains(testMethod);

				if (iTestPassing && nrExecuted) {
					testsPassingExecuting.add(testMethod);
				} else if (!iTestPassing && nrExecuted) {
					testsFailingExecuting.add(testMethod);
				} else if (iTestPassing && !nrExecuted) {
					nrTestPassingNotExecuting++;
				} else if (!iTestPassing && !nrExecuted) {
					nrTestPassingNotExecuting++;
				}
			}

			Double score = formula.compute(nrTestPassingNotExecuting, nrTestFailingNotExecuting,
					testsPassingExecuting.size(), testsFailingExecuting.size());

			result.put(keyline, new Suspiciousness(score, testsPassingExecuting, testsFailingExecuting));
		}
		// Removes the entries which suspicious is zero
		if (!includeSuspiciousEqualsToZero)
			result.values().removeIf(x -> x.getScore() == 0);

		// Sort by suspicious and return
		return result.entrySet().stream()
				.sorted(Map.Entry.<String, Suspiciousness>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

	}

}
