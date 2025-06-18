package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public class OchiaiFormula implements Formula {

	public OchiaiFormula() {
	}

	public double compute(double nPassingNotExecuting, double nFailingNotExecuting, double nPassingExecuting,
						  double nFailingExecuting) {
		double sumOfFailingExecutingAndPassingExecuting = nFailingExecuting + nPassingExecuting;
		if (Double.compare(sumOfFailingExecutingAndPassingExecuting, 0.0) == 0) {
			return 0.0;
		}
		double sumOfFailingExecutingAndFailingNotExecuting = nFailingExecuting + nFailingNotExecuting;
		if (Double.compare(sumOfFailingExecutingAndFailingNotExecuting, 0.0) == 0) {
			return 0.0;
		}
		return nFailingExecuting
				/ Math.sqrt(sumOfFailingExecutingAndPassingExecuting * sumOfFailingExecutingAndFailingNotExecuting);
	}
}
