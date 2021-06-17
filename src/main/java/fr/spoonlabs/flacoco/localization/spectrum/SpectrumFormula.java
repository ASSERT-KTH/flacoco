package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.localization.spectrum.formulas.Formula;
import fr.spoonlabs.flacoco.localization.spectrum.formulas.OchiaiFormula;

public enum SpectrumFormula {

	OCHIAI(new OchiaiFormula());

	private final Formula formula;

	private SpectrumFormula(Formula formula) {
		this.formula = formula;
	}

	public Formula getFormula() {
		return this.formula;
	}

}
