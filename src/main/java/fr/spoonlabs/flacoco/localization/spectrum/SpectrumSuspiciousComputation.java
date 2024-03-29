package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.coverage.CoverageMatrix;
import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import fr.spoonlabs.flacoco.localization.spectrum.formulas.Formula;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Computes the suspiciousness given a code coverage matrix and a formula.
 *
 * @author Matias Martinez
 */
public class SpectrumSuspiciousComputation {

	private Logger logger = Logger.getLogger(SpectrumSuspiciousComputation.class);

	private FlacocoConfig config;

	public SpectrumSuspiciousComputation(FlacocoConfig config) {
		this.config = config;
	}

	/**
	 * @param matrix                        matrix with the coverage
	 * @param formula                       the formula to compute the
	 *                                      suspiciousness
	 * @return a map where the keys are the lines and the values are the suspicious
	 * values
	 */
	public Map<Location, Suspiciousness> calculateSuspicious(CoverageMatrix matrix, Formula formula) {

		Map<Location, Suspiciousness> result = new HashMap<>();
		// For each line of code to analyze
		for (Location location : matrix.getResultExecution().keySet()) {
			List<TestMethod> testsPassingExecuting = new ArrayList<>();
			List<TestMethod> testsFailingExecuting = new ArrayList<>();

			int nrTestPassingNotExecuting = 0;
			int nrTestFailingNotExecuting = 0;

			Set<TestMethod> currentExecution = matrix.getResultExecution().get(location);

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
					nrTestFailingNotExecuting++;
				}
			}

			Double score = formula.compute(nrTestPassingNotExecuting, nrTestFailingNotExecuting,
					testsPassingExecuting.size(), testsFailingExecuting.size());

			result.put(location, new Suspiciousness(score, testsPassingExecuting, testsFailingExecuting));
		}

		// Filter according to threshold, sort by suspicious and return
		return result.entrySet().stream()
				.filter(x -> x.getValue().getScore() >= config.getThreshold() && (x.getValue().getScore() > 0.0 || config.isIncludeZeros()))
				.sorted(Comparator.comparing(x -> x.getKey().getClassName()))
				.sorted(Comparator.comparingInt(x -> x.getKey().getLineNumber()))
				.sorted(Map.Entry.<Location, Suspiciousness>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

	}

}
