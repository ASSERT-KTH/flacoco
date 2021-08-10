package fr.spoonlabs.FLtest1.enum;

import junit.framework.Assert;
import junit.framework.TestCase;

public class CalculatorTest extends TestCase {

	Calculator c = new Calculator();

	public void testSum() {

		Assert.assertEquals(4, c.calculate("+", 3, 1));

	}

	public void testSubs() {

		Assert.assertEquals(2, c.calculate("-", 3, 1));

	}

	public void testMul() {

		Assert.assertEquals(8, c.calculate("*", 4, 2));

	}

	public void testDiv() {

		Assert.assertEquals(2, c.calculate("/", 12, 6));

	}

}