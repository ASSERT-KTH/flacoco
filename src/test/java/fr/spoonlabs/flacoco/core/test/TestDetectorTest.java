package fr.spoonlabs.flacoco.core.test;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the TestDetector against 3 example projects
 */
public class TestDetectorTest {

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
	public void testExampleFL1() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL1/FLtest1");

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestInformation> tests = testDetector.findTests();

		// Check that there is one test class
		assertEquals(1, tests.size());
		// Check that there are 4 test methods in the test class
		TestInformation testInformation = tests.get(0);
		assertEquals(4, testInformation.getTestMethods().size());
	}

	@Test
	public void testExampleFL2() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL2/FLtest1");

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestInformation> tests = testDetector.findTests();

		// Check that there is one test class
		assertEquals(1, tests.size());
		// Check that there are 5 test methods in the test class
		TestInformation testInformation = tests.get(0);
		assertEquals(5, testInformation.getTestMethods().size());
	}

	@Test
	public void testExampleFL3() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL3/FLtest1");

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestInformation> tests = testDetector.findTests();

		// Check that there is one test class
		assertEquals(1, tests.size());
		// Check that there are 5 test methods in the test class
		TestInformation testInformation = tests.get(0);
		assertEquals(5, testInformation.getTestMethods().size());
	}

}