package fr.spoonlabs.flacoco.formulas;

public class OchiaiFormula implements Formula {

	public OchiaiFormula() {
	}

	public double compute(int nPassingNotExecuting, int nFailingNotExecuting, int nPassingExecuting,
			int nFailingExecuting) {

		if ((nFailingExecuting + nPassingExecuting == 0) || (nFailingExecuting + nFailingNotExecuting == 0)) {
			return 0;
		}
		return nFailingExecuting
				/ (Math.sqrt((nFailingExecuting + nFailingNotExecuting) * (nFailingExecuting + nPassingExecuting)));
	}

}
