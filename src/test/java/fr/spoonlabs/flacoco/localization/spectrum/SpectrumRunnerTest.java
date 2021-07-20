package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.api.Suspiciousness;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SpectrumRunnerTest {

	@Rule
	public TemporaryFolder workspaceDir = new TemporaryFolder();

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);

		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setWorkspace(workspaceDir.getRoot().getAbsolutePath());
	}

	@After
	public void tearDown() {
		FlacocoConfig.deleteInstance();
	}

	@Test
	public void testExampleFL1Ochiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL2Ochiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL2/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
		}

		assertEquals(6, susp.size());

		// Line executed only by the failing
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@21").getScore(), 0);
		assertEquals(1.0, susp.get("fr/spoonlabs/FLtest1/Calculator@-@22").getScore(), 0);

		// Line executed by a mix of failing and passing
		assertEquals(0.70, susp.get("fr/spoonlabs/FLtest1/Calculator@-@18").getScore(), 0.01);
		assertEquals(0.57, susp.get("fr/spoonlabs/FLtest1/Calculator@-@16").getScore(), 0.01);
		assertEquals(0.5, susp.get("fr/spoonlabs/FLtest1/Calculator@-@14").getScore(), 0);

		// Lines executed by all test
		assertEquals(0.44, susp.get("fr/spoonlabs/FLtest1/Calculator@-@12").getScore(), 0.01);
	}

	@Test
	public void testExampleFL3Ochiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL3/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
		}

		assertEquals(5, susp.size());

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
	public void testExampleFL1OchiaiCoverTests() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL1/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL4JUnit5Ochiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL4JUnit5OchiaiCoverTests() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL4JUnit5/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL5JUnit3Ochiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL5JUnit3OchiaiCoverTests() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL5JUnit3/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL6MixedOchiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL6MixedOchiaiCoverTests() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL6Mixed/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);
		config.setCoverTests(true);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL7Ochiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath(new File("./examples/exampleFL7SameNamedMethods/FLtest1").getAbsolutePath());
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL8Ochiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL8NotMaven/");
		config.setSrcJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/java"));
		config.setSrcTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/test"));
		config.setBinJavaDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/classes"));
		config.setBinTestDir(Collections.singletonList("./examples/exampleFL8NotMaven/bin/test-classes"));
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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
	public void testExampleFL9Ochiai() {
		// Setup config
		FlacocoConfig config = FlacocoConfig.getInstance();
		config.setProjectPath("./examples/exampleFL9NotMavenMultiple/");
		config.setSrcJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/java"));
		config.setSrcTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/test2", "./examples/exampleFL9NotMavenMultiple/test1"));
		config.setBinJavaDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/classes"));
		config.setBinTestDir(Arrays.asList("./examples/exampleFL9NotMavenMultiple/bin/test-classes2", "./examples/exampleFL9NotMavenMultiple/bin/test-classes1"));
		config.setSpectrumFormula(SpectrumFormula.OCHIAI);

		SpectrumRunner runner = new SpectrumRunner();

		Map<String, Suspiciousness> susp = runner.run();

		for (String line : susp.keySet()) {
			System.out.println("susp " + line + " " + susp.get(line));
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

}
