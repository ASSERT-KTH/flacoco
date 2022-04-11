package fr.spoonlabs.FLtest1;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class CalculatorTest {

	Calculator c = new Calculator();

	@Test
	public void testSum() {

		Assertions.assertEquals(4, c.calculate("+", 3, 1));

	}

	@Test
	void testSubs() {

		Assertions.assertEquals(2, c.calculate("-", 3, 1));

	}

	@Test
	public void testMul() {

		Assertions.assertEquals(8, c.calculate("*", 4, 2));

	}

	@Test
	public void testDiv() {

		Assertions.assertEquals(2, c.calculate("/", 12, 6));

	}

	@Disabled
	@Test
	public void testIgnore() {

		Assertions.assertEquals(2, c.calculate("/", 12, 6));

	}

}
