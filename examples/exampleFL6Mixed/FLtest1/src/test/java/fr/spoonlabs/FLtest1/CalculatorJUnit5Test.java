package fr.spoonlabs.FLtest1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorJUnit5Test {

	Calculator c = new Calculator();

	@Test
	public void testDiv() {

		Assertions.assertEquals(2, c.calculate("/", 12, 6));

	}
}
