package fr.spoonlabs.FLtest1;

import junit.framework.Assert;
import junit.framework.TestCase;

public class CalculatorJUnit3Test extends TestCase {

	Calculator c = new Calculator();

	public void testSum() {

		Assert.assertEquals(4, c.calculate("+", 3, 1));

	}

}
