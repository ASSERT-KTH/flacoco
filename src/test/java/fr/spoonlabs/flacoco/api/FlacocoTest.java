package fr.spoonlabs.flacoco.api;

import fr.spoonlabs.flacoco.api.result.FlacocoResult;
import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.api.result.Suspiciousness;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.method.StringTestMethod;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import spoon.reflect.code.CtStatement;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.spoonlabs.flacoco.TestUtils.*;
import static org.junit.Assert.*;

/**
 * This test class tests the execution of Flacoco as a whole
 */
public class FlacocoTest {

	@Rule
	public TemporaryFolder workspaceDir = new TemporaryFolder();

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(6, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL1SpectrumBasedTarantulaDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.TARANTULA);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(5, susp.size());

		// Line executed by a mix of failing and passing
		assertEquals(0.75, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.6, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(5, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultModeThreshold() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setThreshold(0.51);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		// no lines below to 0.51 in suspiciousness are returned
		assertEquals(3, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(3, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultModeIncludeZero() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setThreshold(0.0);
		config.setIncludeZeros(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		// all lines are returned
		assertEquals(10, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		// Lines with no failing test executing them have a 0.0 score
		assertEquals(0.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 11)).getScore(), 0);
		assertEquals(0.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 13)).getScore(), 0);
		assertEquals(0.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 16)).getScore(), 0);
		assertEquals(0.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 17)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(10, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultModeManualTestConfig() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setjUnit4Tests(Stream.of(
				"fr.spoonlabs.FLtest1.CalculatorTest#testSum",
				"fr.spoonlabs.FLtest1.CalculatorTest#testSubs",
				"fr.spoonlabs.FLtest1.CalculatorTest#testMul",
				"fr.spoonlabs.FLtest1.CalculatorTest#testDiv"
				).collect(Collectors.toSet())
		);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(6, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiDefaultModeIgnoreFailingTest() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setIgnoredTests(Stream.of("fr.spoonlabs.FLtest1.CalculatorTest#testMul").collect(Collectors.toSet()));
		config.setIncludeZeros(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(3, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(0, result.getFailingTests().size());

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		// all executed lines are returned
		assertEquals(9, susp.size());

		// and they all have zero suspiciousness, since we ignored the failing test case
		for (Location location : susp.keySet()) {
			assertEquals(0.0, susp.get(location).getScore(), 0);
		}

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(9, locations.size());
		assertOrdered(susp, locations);
	}

	/**
	 * This test captures the functionality of computing the coverage even when an exception is
	 * thrown during execution
	 */
	@Test
	public void testExampleFL2SpectrumBasedOchiaiDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL2/FLtest1").getAbsolutePath());

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(5, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMod")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMod")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(8, susp.keySet().size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 21)).getScore(), 0);
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 22)).getScore(), 0); // exception thrown here

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 18)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 16)).getScore(), 0.01);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0);

		// Lines executed by all test
		assertEquals(0.44, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);
		assertEquals(0.44, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0.01);
		assertEquals(0.44, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0.01);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(8, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL3SpectrumBasedOchiaiDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(5, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testModCatchException")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testModCatchException")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(7, susp.keySet().size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 21)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 18)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 16)).getScore(), 0.01);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0);

		// Lines executed by all test
		assertEquals(0.44, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);
		assertEquals(0.44, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0.01);
		assertEquals(0.44, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0.01);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(7, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(9, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 29)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 8)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(9, locations.size());
		assertOrdered(susp, locations);
	}

	/**
	 * This test captures the cases where the test classes are included in the option `jacocoIncludes`
	 * but the option `coverTests` is not set
	 */
	@Test
	public void testExampleFL1SpectrumBasedOchiaiNoCoverTestsIncludesDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setJacocoIncludes(Collections.singleton("fr.spoonlabs.FLtest1.*"));

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(6, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL1SpectrumBasedOchiaiSpoonMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setComputeSpoonResults(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, CtStatement> mapping = result.getLocationStatementMap();
		assertEquals(6, mapping.size());

		Map<CtStatement, Suspiciousness> susp = result.getSpoonSuspiciousnessMap();
		// The two lines of the (empty) constructor get mapped to the same CtStatement
		assertEquals(5, susp.size());

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
				case 5:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL4JUnit5SpectrumBasedOchiaiDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(4, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	@Ignore
	public void testExampleFL4JUnit5SpectrumBasedOchiaiDefaultModeManualTestConfig() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setjUnit5Tests(Stream.of(
				"fr.spoonlabs.FLtest1.CalculatorTest#testSum",
				"fr.spoonlabs.FLtest1.CalculatorTest#testSubs",
				"fr.spoonlabs.FLtest1.CalculatorTest#testMul",
				"fr.spoonlabs.FLtest1.CalculatorTest#testDiv"
				).collect(Collectors.toSet())
		);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(4, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	@Ignore
	public void testExampleFL4JUnit5SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(7, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 29)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 8)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(7, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL4JUnit5SpectrumBasedOchiaiSpoonMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setComputeSpoonResults(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, CtStatement> mapping = result.getLocationStatementMap();
		assertEquals(4, mapping.size());

		Map<CtStatement, Suspiciousness> susp = result.getSpoonSuspiciousnessMap();
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
		// Run only on all compiler versions
		Assume.assumeTrue(getCompilerVersion() >= 1);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "ignoretestIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(4, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	@Ignore
	public void testExampleFL5JUnit3SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Run only on all compiler versions
		Assume.assumeTrue(getCompilerVersion() >= 1);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "ignoretestIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(7, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 28)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 9)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 7)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(7, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL5JUnit3SpectrumBasedOchiaiSpoonMode() {
		// Run only on all compiler versions
		Assume.assumeTrue(getCompilerVersion() >= 1);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());
		config.setComputeSpoonResults(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "ignoretestIgnore")));

		Map<Location, CtStatement> mapping = result.getLocationStatementMap();
		assertEquals(4, mapping.size());

		Map<CtStatement, Suspiciousness> susp = result.getSpoonSuspiciousnessMap();
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
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorJUnit5Test", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorJUnit3Test", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testMul")));

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(4, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL6MixedSpectrumBasedOchiaiDefaultModeManualTestConfig() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setjUnit4Tests(Stream.of(
				"fr.spoonlabs.FLtest1.CalculatorJUnit3Test#testSum",
				"fr.spoonlabs.FLtest1.CalculatorMixedTest#testSubs"
				).collect(Collectors.toSet())
		);
		config.setjUnit5Tests(Stream.of(
				"fr.spoonlabs.FLtest1.CalculatorMixedTest#testMul",
				"fr.spoonlabs.FLtest1.CalculatorJUnit5Test#testDiv"
				).collect(Collectors.toSet())
		);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorJUnit5Test", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorJUnit3Test", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testMul")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(4, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	@Ignore
	public void testExampleFL6MixedSpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorJUnit5Test", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorJUnit3Test", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testMul")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(7, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 28)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 9)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 7)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(7, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL6MixedSpectrumBasedOchiaiSpoonMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setComputeSpoonResults(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorJUnit5Test", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorJUnit3Test", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorMixedTest", "testMul")));

		Map<Location, CtStatement> mapping = result.getLocationStatementMap();
		assertEquals(4, mapping.size());

		Map<CtStatement, Suspiciousness> susp = result.getSpoonSuspiciousnessMap();
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
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL7SameNamedMethods/FLtest1").getAbsolutePath());

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(8, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testSum")
		)));

		// Check failing tests
		assertEquals(2, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testMul")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(6, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL8SpectrumBasedOchiaiDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		// we don't set --projectpath because it is not needed when we explicit the other 4 dirs
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));
		config.setTestRunnerVerbose(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(6, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL8SpectrumBasedOchiaiCoverTestsDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath("./examples/exampleFL8NotMaven/");
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));
		config.setCoverTests(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(9, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 29)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.CalculatorTest", 8)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(9, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL8SpectrumBasedOchiaiSpoonMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath("./examples/exampleFL8NotMaven/");
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));
		config.setComputeSpoonResults(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, CtStatement> mapping = result.getLocationStatementMap();
		assertEquals(6, mapping.size());

		Map<CtStatement, Suspiciousness> susp = result.getSpoonSuspiciousnessMap();
		// The two lines of the (empty) constructor get mapped to the same CtStatement
		assertEquals(5, susp.size());

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
				case 5:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL9SpectrumBasedOchiaiDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath("./examples/exampleFL9NotMavenMultiple/");
		config.setSrcJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/java"));
		config.setSrcTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/test2", "./examples/exampleFL9NotMavenMultiple/test1"));
		config.setBinJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/classes"));
		config.setBinTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/test-classes2", "./examples/exampleFL9NotMavenMultiple/bin/test-classes1"));

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(8, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testSum")
		)));

		// Check failing tests
		assertEquals(2, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testMul")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0);
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(6, locations.size());
		assertOrdered(susp, locations);
	}

	@Test
	public void testExampleFL9SpectrumBasedOchiaiSpoonMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath("./examples/exampleFL9NotMavenMultiple/");
		config.setSrcJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/java"));
		config.setSrcTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/test2", "./examples/exampleFL9NotMavenMultiple/test1"));
		config.setBinJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/classes"));
		config.setBinTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/test-classes2", "./examples/exampleFL9NotMavenMultiple/bin/test-classes1"));
		config.setComputeSpoonResults(true);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// Check executed tests
		assertEquals(8, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testSum")
		)));

		// Check failing tests
		assertEquals(2, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorDuplicatedTest", "testMul")));

		Map<Location, CtStatement> mapping = result.getLocationStatementMap();
		assertEquals(6, mapping.size());

		Map<CtStatement, Suspiciousness> susp = result.getSpoonSuspiciousnessMap();
		// The two lines of the (empty) constructor get mapped to the same CtStatement
		assertEquals(5, susp.size());

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
				case 5:
					assertEquals(0.5, susp.get(ctStatement).getScore(), 0);
					break;
			}
		}
	}

	@Test
	public void testExampleFL11SpectrumBasedOchiaiDefaultMode() {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL11/FLtest1").getAbsolutePath());

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(2, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testMul")));
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testDiv")));

		// Check ignored tests weren't executed
		assertFalse(result.getExecutedTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.CalculatorTest", "testIgnore")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(8, susp.size());

		// Line executed by all failing test cases
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 14)).getScore(), 0.0);

		// Line executed by one passing and 2 failing tests
		assertEquals(0.81, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 12)).getScore(), 0.01);

		// Lines executed by one failing test
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 15)).getScore(), 0.01);
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 16)).getScore(), 0.01);
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 17)).getScore(), 0.01);

		// Line executed by all tests (2 passing, 2 failing)
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 10)).getScore(), 0.01);
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 5)).getScore(), 0.01);
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.Calculator", 6)).getScore(), 0.01);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(8, locations.size());
		assertOrdered(susp, locations);
	}

	/**
	 * Captures behaviour of https://github.com/SpoonLabs/flacoco/issues/71
	 */
	@Test
	public void testExampleFL12SpectrumBasedOchiaiDefaultMode() {
		// Run only on target release < 5
		Assume.assumeTrue(getCompilerVersion() < 5);

		// We can only run this test on java version less than 11
		// since java 11 dropped support for compliance level 1.4
		Assume.assumeTrue(isLessThanJava11());

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		config.setProjectPath(new File("./examples/exampleFL12Compliance4/FLtest1").getAbsolutePath());
		config.setComplianceLevel(4);

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		for (Map.Entry<Location, Suspiciousness> entry : result.getDefaultSuspiciousnessMap().entrySet()) {
			System.out.println(entry);
		}

		// Check executed tests
		assertEquals(4, result.getExecutedTests().size());
		assertTrue(result.getExecutedTests().containsAll(Arrays.asList(
				new StringTestMethod("fr.spoonlabs.FLtest1.enum.CalculatorTest", "testMul"),
				new StringTestMethod("fr.spoonlabs.FLtest1.enum.CalculatorTest", "testDiv"),
				new StringTestMethod("fr.spoonlabs.FLtest1.enum.CalculatorTest", "testSubs"),
				new StringTestMethod("fr.spoonlabs.FLtest1.enum.CalculatorTest", "testSum")
		)));

		// Check failing tests
		assertEquals(1, result.getFailingTests().size());
		assertTrue(result.getFailingTests().contains(new StringTestMethod("fr.spoonlabs.FLtest1.enum.CalculatorTest", "testMul")));

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();
		assertEquals(4, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get(new Location("fr.spoonlabs.FLtest1.enum.Calculator", 15)).getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get(new Location("fr.spoonlabs.FLtest1.enum.Calculator", 14)).getScore(), 0.01);
		assertEquals(0.57, susp.get(new Location("fr.spoonlabs.FLtest1.enum.Calculator", 12)).getScore(), 0.01);

		// Lines executed by all test
		assertEquals(0.5, susp.get(new Location("fr.spoonlabs.FLtest1.enum.Calculator", 10)).getScore(), 0);

		List<Location> locations = result.getSuspiciousLocationList();
		assertEquals(4, locations.size());
		assertOrdered(susp, locations);
	}

	/**
	 * This test captures the functionality of computing the coverage even when an exception is
	 * thrown during execution and there are lines ignored by JaCoCo before the line throwing exception
	 *
	 * See:
	 * - https://github.com/jacoco/jacoco/issues/1223#issuecomment-926636606
	 * - https://github.com/SpoonLabs/flacoco/issues/109
	 */
	@Test
	public void testExampleCL2SpectrumBasedOchiaiDefaultMode() {
		// Run only on java 8
		Assume.assumeTrue(getCompilerVersion() >= 5);
		Assume.assumeTrue(getJavaVersion() == 8);

		// Setup config
		FlacocoConfig config = getDefaultFlacocoConfig();
		LogManager.getRootLogger().setLevel(Level.INFO);
		config.setTestRunnerVerbose(false);
		config.setProjectPath(new File("./examples/cl2").getAbsolutePath());
		config.setSrcJavaDir(Collections.singletonList(new File("./examples/cl2/src/java").getAbsolutePath()));

		// Run Flacoco
		Flacoco flacoco = new Flacoco(config);

		// Run default mode
		FlacocoResult result = flacoco.run();

		// TODO: Flacoco is running 743 tests compared to the baseline obtained through running `mvn clean test` on the cl2 project
		// TODO: It also finds 4 failing tests, when it should be 3
		// assertEquals(703, result.getExecutedTests().size());
		//assertEquals(4, result.getFailingTests().size());

		Map<Location, Suspiciousness> susp = result.getDefaultSuspiciousnessMap();

		// Lines not included by JaCoCo
		assertTrue(result.getDefaultSuspiciousnessMap().containsKey(new Location("org.apache.commons.lang.StringUtils", 1050)));
		assertTrue(result.getDefaultSuspiciousnessMap().containsKey(new Location("org.apache.commons.lang.StringUtils", 1051)));
		assertTrue(result.getDefaultSuspiciousnessMap().containsKey(new Location("org.apache.commons.lang.StringUtils", 1054)));
	}

	private FlacocoConfig getDefaultFlacocoConfig() {
		FlacocoConfig config = new FlacocoConfig();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		config.setTestRunnerVerbose(true);
		config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		return config;
	}

	private void assertOrdered(Map<Location, Suspiciousness> susp, List<Location> locations) {
		// Assert the location list is ranked
		Location previous = null;
		for (Location location : locations) {
			if (previous != null) {
				assertTrue(susp.get(previous).getScore() >= susp.get(location).getScore());
			}
			previous = location;
		}
	}

}