package fr.spoonlabs.flacoco.localization.spectrum;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.spoonlabs.flacoco.core.coverage.CoverageMatrix;
import fr.spoonlabs.flacoco.localization.spectrum.formulas.Formula;

/**
 * Computes the suspiciousness given a code coverage matrix and a formula.
 * 
 * @author Matias Martinez
 */
public class SpectrumSuspiciousComputation {

	/**
	 * 
	 * @param matrix  matrix with the coverage
	 * @param formula the formula to compute the suspiciousness
	 * @return a map where the keys are the lines and the values are the suspicious
	 *         values
	 */
	public Map<String, Double> calculateSuspicious(CoverageMatrix matrix, Formula formula) {
		return calculateSuspicious(matrix, formula, false);
	}

	/**
	 * 
	 * @param matrix                        matrix with the coverage
	 * @param formula                       the formula to compute the
	 *                                      suspiciousness
	 * @param includeSuspiciousEqualsToZero indicates if the suspicious list
	 *                                      contains lines with suspicious equals to
	 *                                      zero
	 * @return a map where the keys are the lines and the values are the suspicious
	 *         values
	 */
	public Map<String, Double> calculateSuspicious(CoverageMatrix matrix, Formula formula,
	                                               boolean includeSuspiciousEqualsToZero) {

		Map<String, Double> result = new HashMap<>();
		// For each line of code to analyze
		for (String keyline : matrix.getResultExecution().keySet()) {

			int nrTestPassingNotExecuting = 0;
			int nrTestFailingNotExecuting = 0;
			int nrTestPassingExecuting = 0;
			int nrTestFailingExecuting = 0;

			Set<Integer> currentExecution = matrix.getResultExecution().get(keyline);

			// For each test
			for (int iTest = 0; iTest < matrix.getTests().size(); iTest++) {

				Boolean iTestPassing = matrix.getTestResult().get(iTest);
				Boolean nrExecuted = (currentExecution.contains(iTest));

				if (iTestPassing && nrExecuted) {
					nrTestPassingExecuting++;
				} else if (!iTestPassing && nrExecuted) {
					nrTestFailingExecuting++;
				} else if (iTestPassing && !nrExecuted) {
					nrTestPassingNotExecuting++;
				} else if (!iTestPassing && !nrExecuted) {
					nrTestPassingNotExecuting++;
				}
			}

			double valueFL = formula.compute(nrTestPassingNotExecuting, nrTestFailingNotExecuting,
					nrTestPassingExecuting, nrTestFailingExecuting);

			result.put(keyline, valueFL);
		}
		// Removes the entries which suspicious is zero
		if (!includeSuspiciousEqualsToZero)
			result.values().removeIf(value -> value == 0);

		// Sort by suspicious
		Map<String, Double> sortedSuspicious = result.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		return sortedSuspicious;

	}

}
