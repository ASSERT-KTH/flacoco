package fr.spoonlabs.flacoco.api;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spoon.reflect.code.CtStatement;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class tests the execution of Flacoco as a whole
 */
public class FlacocoTest {

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);

		FlacocoConfig config = FlacocoConfig.getInstance();
		String dep1 = new File("./examples/libs/junit-4.12.jar").getAbsolutePath();
		String dep2 = new File("./examples/libs/hamcrest-core-1.3.jar").getAbsolutePath();
		config.setClasspath(dep1 + File.separator + dep2);
	}

	@After
	public void tearDown() {
		FlacocoConfig.deleteInstance();
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL1/FLtest1");
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Double> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15"), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14"), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12"), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10"), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@5"), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@6"), 0);
	}

	/**
	 * This test captures the functionality of computing the coverage even when an exception is
	 * thrown during execution
	 */
	@Test
	public void testExampleFL2SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL2/FLtest1");
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Double> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
		}

		assertEquals(7, susp.keySet().size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@21"), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@18"), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@16"), 0.01);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14"), 0);

		// Lines executed by all test
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12"), 0.01);
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@5"), 0.01);
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@6"), 0.01);
	}

	@Test
	public void testExampleFL3SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL3/FLtest1");
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Double> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
		}

		assertEquals(7, susp.keySet().size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@21"), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@18"), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@16"), 0.01);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14"), 0);

		// Lines executed by all test
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12"), 0.01);
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@5"), 0.01);
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@6"), 0.01);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL1/FLtest1");
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Double> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(8, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15"), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14"), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12"), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@9"), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@7"), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10"), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@5"), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@6"), 0);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiSpoonMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL1/FLtest1");
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<CtStatement, Double> susp = flacoco.runSpoon();

		// Fails because two original keys get mapped to the same CtStatement
		//assertEquals(6, susp.size());
		assertEquals(5, susp.size());

		for (CtStatement ctStatement : susp.keySet()) {
			System.out.println("" + ctStatement + " " + susp.get(ctStatement));
			assertTrue(ctStatement.getPosition().getFile().getAbsolutePath().endsWith("fr/spoonlabs/FLtest1/Calculator.java"));
			switch (ctStatement.getPosition().getLine()) {
				// Line executed only by the failing
				case 15:
					assertEquals(1.0, susp.get(ctStatement), 0);
					break;
				// Line executed by failing and passing
				case 14:
					assertEquals(0.70, susp.get(ctStatement), 0.01);
					break;
				case 12:
					assertEquals(0.57, susp.get(ctStatement), 0.01);
					break;
				// Lines executed by all test
				case 10:
				case 5:
					assertEquals(0.5, susp.get(ctStatement), 0);
					break;
			}
		}

	}

}