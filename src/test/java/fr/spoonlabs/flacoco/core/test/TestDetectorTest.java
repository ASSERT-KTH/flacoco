package fr.spoonlabs.flacoco.core.test;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the TestDetector against 3 example projects
 */
public class TestDetectorTest {

	@Rule
	public TemporaryFolder workspaceDir = new TemporaryFolder();

	@Before
	public void setUp() throws IOException {
		LogManager.getRootLogger().setLevel(Level.DEBUG);

		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
		String dep1 = new File("./examples/libs/junit-4.12.jar").getAbsolutePath();
		String dep2 = new File("./examples/libs/hamcrest-core-1.3.jar").getAbsolutePath();
		String dep3 = new File("./examples/libs/junit-jupiter-api-5.7.2.jar").getAbsolutePath();
		config.setClasspath(dep1 + File.pathSeparatorChar + dep2 + File.pathSeparatorChar + dep3);
	}

	@After
	public void tearDown() {
		FlacocoConfig.deleteInstance();
	}

	@Test
	public void testExampleFL1() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());

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
		config.setProjectPath(new File("./examples/exampleFL2/FLtest1").getAbsolutePath());

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
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());

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
	public void testExampleFL4JUnit5() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4/FLtest1").getAbsolutePath());
		config.setTestFramework(FlacocoConfig.TestFramework.JUNIT5);

		// Find the tests
		TestDetector testDetector = new TestDetector();
		List<TestInformation> tests = testDetector.findTests();

		// Check that there is one test class
		assertEquals(1, tests.size());
		// Check that there are 4 test methods in the test class
		TestInformation testInformation = tests.get(0);
		assertEquals(4, testInformation.getTestMethods().size());
	}

}