package fr.spoonlabs.FLtest1;

import org.junit.jupiter.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

public class CalculatorMixedTest {

	Calculator c = new Calculator();

	@Test
	public void testSubs() {

		Assert.assertEquals(2, c.calculate("-", 3, 1));

	}

	@org.junit.jupiter.api.Test
	public void testMul() {

		Assertions.assertEquals(8, c.calculate("*", 4, 2));

	}

}
