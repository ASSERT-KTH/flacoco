package fr.spoonlabs.flacoco.api;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import spoon.reflect.code.CtStatement;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class tests the execution of Flacoco as a whole
 */
public class FlacocoTest {

	@Rule
	public TemporaryFolder workspaceDir = new TemporaryFolder();

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);

		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
	}

	@After
	public void tearDown() {
		FlacocoConfig.deleteInstance();
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultModeThreshold() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setThreshold(0.5);
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		// no lines below or equal to 0.5 in suspiciousness are returned
		assertEquals(3, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultModeManualTestConfig() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setjUnit4Tests(Arrays.asList(
				"fr.spoonlabs.FLtest1.CalculatorTest#testSum",
				"fr.spoonlabs.FLtest1.CalculatorTest#testSubs",
				"fr.spoonlabs.FLtest1.CalculatorTest#testMul",
				"fr.spoonlabs.FLtest1.CalculatorTest#testDiv"
				)
		);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	/**
	 * This test captures the functionality of computing the coverage even when an exception is
	 * thrown during execution
	 */
	@Test
	public void testExampleFL2SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL2/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
		}

		assertEquals(6, susp.keySet().size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@21").getScore(), 0);
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@22").getScore(), 0); // exception thrown here

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@18").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@16").getScore(), 0.01);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0);

		// Lines executed by all test
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);
	}

	@Test
	public void testExampleFL3SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
		}

		assertEquals(5, susp.keySet().size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@21").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@18").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@16").getScore(), 0.01);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0);

		// Lines executed by all test
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);
	}

	@Test
	@Ignore
	public void testExampleFL1SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@9").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@7").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiSpoonMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<CtStatement, Suspiciousness> susp = flacoco.runSpoon();

		assertEquals(4, susp.size());

		for (CtStatement ctStatement : susp.keySet()) {
			System.out.println("" + ctStatement + " " + susp.get(ctStatement));
			// Assert location is Calculator.java, regex for matching both unix and dos paths
			assertTrue(ctStatement.getPosition().getFile().getAbsolutePath()
					.matches(".*(fr)[\\\\/](spoonlabs)[\\\\/](FLtest1)[\\\\/](Calculator)\\.(java)$"));
			switch (ctStatement.getPosition().getLine()) {
				// Line executed only by the failing
				case 15:
					assertEquals(1.0, susp.get(ctStatement).getScore(), 0);
					break;
				// Line executed by failing and passing
				case 14:
					assertEquals(0.70, susp.get(ctStatement).getScore(), 0.01);
					break;
				case 12:
					assertEquals(0.57, susp.get(ctStatement).getScore(), 0.01);
					break;
				// Lines executed by all test
				case 10:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL4JUnit5SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL4JUnit5SpectrumBasedOchiaiDefaultModeManualTestConfig() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setjUnit5Tests(Arrays.asList(
				"fr.spoonlabs.FLtest1.CalculatorTest#testSum",
				"fr.spoonlabs.FLtest1.CalculatorTest#testSubs",
				"fr.spoonlabs.FLtest1.CalculatorTest#testMul",
				"fr.spoonlabs.FLtest1.CalculatorTest#testDiv"
				)
		);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	@Ignore
	public void testExampleFL4JUnit5SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@9").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@7").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL4JUnit5SpectrumBasedOchiaiSpoonMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<CtStatement, Suspiciousness> susp = flacoco.runSpoon();

		assertEquals(4, susp.size());

		for (CtStatement ctStatement : susp.keySet()) {
			System.out.println("" + ctStatement + " " + susp.get(ctStatement));
			// Assert location is Calculator.java, regex for matching both unix and dos paths
			assertTrue(ctStatement.getPosition().getFile().getAbsolutePath()
					.matches(".*(fr)[\\\\/](spoonlabs)[\\\\/](FLtest1)[\\\\/](Calculator)\\.(java)$"));
			switch (ctStatement.getPosition().getLine()) {
				// Line executed only by the failing
				case 15:
					assertEquals(1.0, susp.get(ctStatement).getScore(), 0);
					break;
				// Line executed by failing and passing
				case 14:
					assertEquals(0.70, susp.get(ctStatement).getScore(), 0.01);
					break;
				case 12:
					assertEquals(0.57, susp.get(ctStatement).getScore(), 0.01);
					break;
				// Lines executed by all test
				case 10:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL5JUnit3SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	@Ignore
	public void testExampleFL5JUnit3SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@9").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@7").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL5JUnit3SpectrumBasedOchiaiSpoonMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<CtStatement, Suspiciousness> susp = flacoco.runSpoon();

		assertEquals(4, susp.size());

		for (CtStatement ctStatement : susp.keySet()) {
			System.out.println("" + ctStatement + " " + susp.get(ctStatement));
			// Assert location is Calculator.java, regex for matching both unix and dos paths
			assertTrue(ctStatement.getPosition().getFile().getAbsolutePath()
					.matches(".*(fr)[\\\\/](spoonlabs)[\\\\/](FLtest1)[\\\\/](Calculator)\\.(java)$"));
			switch (ctStatement.getPosition().getLine()) {
				// Line executed only by the failing
				case 15:
					assertEquals(1.0, susp.get(ctStatement).getScore(), 0);
					break;
				// Line executed by failing and passing
				case 14:
					assertEquals(0.70, susp.get(ctStatement).getScore(), 0.01);
					break;
				case 12:
					assertEquals(0.57, susp.get(ctStatement).getScore(), 0.01);
					break;
				// Lines executed by all test
				case 10:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL6MixedSpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL6MixedSpectrumBasedOchiaiDefaultModeManualTestConfig() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setjUnit4Tests(Arrays.asList(
				"fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum",
				"fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs"
				)
		);
		config.setjUnit5Tests(Arrays.asList(
				"fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul",
				"fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv"
				)
		);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	@Ignore
	public void testExampleFL6MixedSpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@9").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@7").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL6MixedSpectrumBasedOchiaiSpoonMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<CtStatement, Suspiciousness> susp = flacoco.runSpoon();

		assertEquals(4, susp.size());

		for (CtStatement ctStatement : susp.keySet()) {
			System.out.println("" + ctStatement + " " + susp.get(ctStatement));
			// Assert location is Calculator.java, regex for matching both unix and dos paths
			assertTrue(ctStatement.getPosition().getFile().getAbsolutePath()
					.matches(".*(fr)[\\\\/](spoonlabs)[\\\\/](FLtest1)[\\\\/](Calculator)\\.(java)$"));
			switch (ctStatement.getPosition().getLine()) {
				// Line executed only by the failing
				case 15:
					assertEquals(1.0, susp.get(ctStatement).getScore(), 0);
					break;
				// Line executed by failing and passing
				case 14:
					assertEquals(0.70, susp.get(ctStatement).getScore(), 0.01);
					break;
				case 12:
					assertEquals(0.57, susp.get(ctStatement).getScore(), 0.01);
					break;
				// Lines executed by all test
				case 10:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL7SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL7SameNamedMethods/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL8SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL8NotMaven/");
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));
		config.setTestRunnerVerbose(true);
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	@Ignore
	public void testExampleFL8SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL8NotMaven/");
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@9").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/CalculatorTest@-@7").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL8SpectrumBasedOchiaiSpoonMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL8NotMaven/");
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<CtStatement, Suspiciousness> susp = flacoco.runSpoon();

		assertEquals(4, susp.size());

		for (CtStatement ctStatement : susp.keySet()) {
			System.out.println("" + ctStatement + " " + susp.get(ctStatement));
			// Assert location is Calculator.java, regex for matching both unix and dos paths
			assertTrue(ctStatement.getPosition().getFile().getAbsolutePath()
					.matches(".*(fr)[\\\\/](spoonlabs)[\\\\/](FLtest1)[\\\\/](Calculator)\\.(java)$"));
			switch (ctStatement.getPosition().getLine()) {
				// Line executed only by the failing
				case 15:
					assertEquals(1.0, susp.get(ctStatement).getScore(), 0);
					break;
				// Line executed by failing and passing
				case 14:
					assertEquals(0.70, susp.get(ctStatement).getScore(), 0.01);
					break;
				case 12:
					assertEquals(0.57, susp.get(ctStatement).getScore(), 0.01);
					break;
				// Lines executed by all test
				case 10:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL9SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL9NotMavenMultiple/");
		config.setSrcJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/java"));
		config.setSrcTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/test2", "./examples/exampleFL9NotMavenMultiple/test1"));
		config.setBinJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/classes"));
		config.setBinTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/test-classes2", "./examples/exampleFL9NotMavenMultiple/bin/test-classes1"));
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
	}

	@Test
	public void testExampleFL9SpectrumBasedOchiaiSpoonMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL9NotMavenMultiple/");
		config.setSrcJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/java"));
		config.setSrcTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/test2", "./examples/exampleFL9NotMavenMultiple/test1"));
		config.setBinJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/classes"));
		config.setBinTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/test-classes2", "./examples/exampleFL9NotMavenMultiple/bin/test-classes1"));
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<CtStatement, Suspiciousness> susp = flacoco.runSpoon();

		assertEquals(4, susp.size());

		for (CtStatement ctStatement : susp.keySet()) {
			System.out.println("" + ctStatement + " " + susp.get(ctStatement));
			// Assert location is Calculator.java, regex for matching both unix and dos paths
			assertTrue(ctStatement.getPosition().getFile().getAbsolutePath()
					.matches(".*(fr)[\\\\/](spoonlabs)[\\\\/](FLtest1)[\\\\/](Calculator)\\.(java)$"));
			switch (ctStatement.getPosition().getLine()) {
				// Line executed only by the failing
				case 15:
					assertEquals(1.0, susp.get(ctStatement).getScore(), 0);
					break;
				// Line executed by failing and passing
				case 14:
					assertEquals(0.70, susp.get(ctStatement).getScore(), 0.01);
					break;
				case 12:
					assertEquals(0.57, susp.get(ctStatement).getScore(), 0.01);
					break;
				// Lines executed by all test
				case 10:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL10SpectrumBasedOchiaiDefaultMode() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL10/FLtest1").getAbsolutePath());
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		// Run Flacoco
		Flacoco flacoco = new Flacoco();

		// Run default mode
		Map<String, Suspiciousness> susp = flacoco.runDefault();

		for (String line : susp.keySet()) {
			System.out.println("" + line + " " + susp.get(line));
		}

		assertEquals(5, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@16").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@15").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@13").getScore(), 0.01);

		// Lines executed by all test
		// the first one is the while(true)
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@10").getScore(), 0);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@11").getScore(), 0);
	}

}