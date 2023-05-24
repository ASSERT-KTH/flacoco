package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public interface Formula {

	public double compute(double nPassingNotExecuting, double nFailingNotExecuting, double nPassingExecuting,
			double nFailingExecuting);

}
