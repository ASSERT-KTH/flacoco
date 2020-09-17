package fr.spoonlabs.FLtest1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CalculatorTest {

	Calculator c = new Calculator();

	@Test
	public void testSum() {

		assertEquals(4, c.calculate("+", 3, 1));

	}

	@Test
	public void testSubs() {

		assertEquals(2, c.calculate("-", 3, 1));

	}

	@Test
	public void testMul() {

		assertEquals(8, c.calculate("*", 4, 2));

	}

	@Test
	public void testDiv() {

		assertEquals(2, c.calculate("/", 12, 6));

	}

	@Test
	public void testModCatchException() {
		try {
			assertEquals(0, c.calculate("%", 12, 6));
		} catch (Exception e) {
			fail("the test has failed");
		}
	}

}
