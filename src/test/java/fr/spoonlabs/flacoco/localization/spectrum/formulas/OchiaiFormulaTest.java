package fr.spoonlabs.flacoco.localization.spectrum.formulas;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OchiaiFormulaTest {

    private OchiaiFormula ochiaiFormula = new OchiaiFormula();

    @Test
    public void computeResultingInZero() {
        assertEquals(0, Double.compare(0.0, ochiaiFormula.compute(1, 1, 9, -9)));
        assertEquals(0, Double.compare(0.0, ochiaiFormula.compute(1, 9, 1, -9)));
    }

    @Test
    public void compute() {
        assertEquals(-1, Double.compare(0.0, ochiaiFormula.compute(1, 2, 3, 4)));
        assertEquals(-1, Double.compare(0.0, ochiaiFormula.compute(10, 10, 10, 10)));
    }
}