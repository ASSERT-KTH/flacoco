package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public class TarantulaFormula implements Formula {

	public TarantulaFormula() {
	}

	public double compute(double nPassingNotExecuting, double nFailingNotExecuting, double nPassingExecuting,
			double nFailingExecuting) {
		double passingTerm = nPassingNotExecuting + nPassingExecuting == 0 ? 0 : nPassingExecuting / (nPassingNotExecuting + nPassingExecuting);
		double failingTerm = nFailingNotExecuting + nFailingExecuting == 0 ? 0 : nFailingExecuting / (nFailingNotExecuting + nFailingExecuting);

		if (passingTerm + failingTerm == 0) {
			return 0;
		}

		return failingTerm / (passingTerm + failingTerm);
	}

}
