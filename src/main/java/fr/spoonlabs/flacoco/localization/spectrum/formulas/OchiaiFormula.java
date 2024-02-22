package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public class OchiaiFormula implements Formula {

	public OchiaiFormula() {
	}

	public double compute(double nPassingNotExecuting, double nFailingNotExecuting, double nPassingExecuting,
			double nFailingExecuting) {

		if ((nFailingExecuting + nPassingExecuting == 0) || (nFailingExecuting + nFailingNotExecuting == 0)) {
			return 0;
		}
		return nFailingExecuting
				/ (Math.sqrt((nFailingExecuting + nFailingNotExecuting) * (nFailingExecuting + nPassingExecuting)));
	}

}
