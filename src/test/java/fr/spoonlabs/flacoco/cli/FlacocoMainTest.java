package fr.spoonlabs.flacoco.cli;

import fr.spoonlabs.flacoco.cli.export.CSVExporter;
import fr.spoonlabs.flacoco.cli.export.JSONExporter;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static fr.spoonlabs.flacoco.TestUtils.getCompilerVersion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FlacocoMainTest {

	@Rule
	public final CheckOutput output = new CheckOutput();

	@Before
	public void setUp() {
		LogManager.getRootLogger().setLevel(Level.DEBUG);
	}

	@Test
	public void testMainExplicitArguments() throws Exception {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// It's a smoke test
		String mavenHome = System.getProperty("user.home") + "/.m2/repository/";
		String junitClasspath = mavenHome + "junit/junit/4.13.2/junit-4.13.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-api/5.3.2/junit-jupiter-api-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar" + File.pathSeparatorChar
				+ mavenHome + "org/opentest4j/opentest4j/1.1.1/opentest4j-1.1.1.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-commons/1.3.2/junit-platform-commons-1.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-engine/5.3.2/junit-jupiter-engine-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-params/5.3.2/junit-jupiter-params-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-engine/1.3.2/junit-platform-engine-1.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-launcher/1.3.2/junit-platform-launcher-1.3.2.jar";
		String jacocoClassPath = mavenHome + "org/jacoco/org.jacoco.core/0.8.3/org.jacoco.core-0.8.3.jar";

		int statusCode = catchSystemExit(() -> FlacocoMain.main(new String[]{
				"--projectpath", "examples/exampleFL1/FLtest1",
				"--formula", SpectrumFormula.OCHIAI.name(),
				"--mavenHome", mavenHome,
				"--junitClasspath", junitClasspath,
				"--jacocoClasspath", jacocoClassPath,
				"--testRunnerVerbose",
				"--testRunnerTimeoutInMs", "10000",
				"--testRunnerJVMArgs", "-Xms16M",
				"--ignoredTests", "fr.spoonlabs.FLtest1.CalculatorTest#testSum" + " " +
				                  "fr.spoonlabs.FLtest1.CalculatorTest#testSubs",
				"--junit4tests", "fr.spoonlabs.FLtest1.CalculatorTest#testSum" + " " +
								 "fr.spoonlabs.FLtest1.CalculatorTest#testSubs" + " " +
								 "fr.spoonlabs.FLtest1.CalculatorTest#testMul" + " " +
							     "fr.spoonlabs.FLtest1.CalculatorTest#testDiv",
				"--threshold", "0.0",
				"--jacocoIncludes", "fr.spoonlabs.FLtest1.*",
				"--jacocoExcludes", "org.junit.*",
				"--complianceLevel", "8"
		}));
		assertEquals(0, statusCode);
	}

	@Test
	public void testMainExplicitArgumentsNotMaven() throws Exception {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// It's a smoke test
		String mavenHome = System.getProperty("user.home") + "/.m2/repository/";
		String junitClasspath = mavenHome + "junit/junit/4.13.2/junit-4.13.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-api/5.3.2/junit-jupiter-api-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar" + File.pathSeparatorChar
				+ mavenHome + "org/opentest4j/opentest4j/1.1.1/opentest4j-1.1.1.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-commons/1.3.2/junit-platform-commons-1.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-engine/5.3.2/junit-jupiter-engine-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-params/5.3.2/junit-jupiter-params-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-engine/1.3.2/junit-platform-engine-1.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-launcher/1.3.2/junit-platform-launcher-1.3.2.jar";
		String jacocoClassPath = mavenHome + "org/jacoco/org.jacoco.core/0.8.3/org.jacoco.core-0.8.3.jar";

		int statusCode = catchSystemExit(() -> FlacocoMain.main(new String[]{
				// we don't set --projectpath because it is not needed when we explicit the other 4 dirs
				"--srcJavaDir", "examples/exampleFL8NotMaven/java",
				"--srcTestDir", "examples/exampleFL8NotMaven/test",
				"--binJavaDir", "examples/exampleFL8NotMaven/bin/classes",
				"--binTestDir", "examples/exampleFL8NotMaven/bin/test-classes",
				"--testDetectionStrategy", FlacocoConfig.TestDetectionStrategy.CLASSLOADER.name(),
				"--formula", SpectrumFormula.OCHIAI.name(),
				"--mavenHome", mavenHome,
				"--junitClasspath", junitClasspath,
				"--jacocoClasspath", jacocoClassPath,
				"--testRunnerVerbose",
				"--testRunnerTimeoutInMs", "10000",
				"--testRunnerJVMArgs", "-Xms16M",
				"-v",
				"--includeZeros"
		}));
		assertEquals(0, statusCode);
	}

	@Test
	public void testMainIncorrectInputs() throws Exception {

		int statusCode = catchSystemExit(() -> FlacocoMain.main(new String[]{
				"--projhjhjhectpathddd", // Incorrect argument
				"examples/exampleFL1/FLtest1",
				"--formula", SpectrumFormula.OCHIAI.name(),
				"--coverTest"
		}));
		assertEquals(CommandLine.ExitCode.USAGE, statusCode);
	}

	@Test
	public void testMainDefaultValues() throws Exception {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// It's a smoke test
		int statusCode = catchSystemExit(() -> FlacocoMain.main(new String[]{
				"--projectpath", "examples/exampleFL1/FLtest1"
		}));
		assertEquals(0, statusCode);
	}

	@Test
	public void testMainCSVExport() throws Exception {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// setup check output rule
		output.extension = new CSVExporter().extension();

		int statusCode = catchSystemExit(() -> FlacocoMain.main(new String[]{
				"--projectpath", "examples/exampleFL1/FLtest1",
				"--format", "CSV",
				"-o", "results.csv"
		}));
		assertEquals(0, statusCode);
	}

	@Test
	public void testMainJSONExport() throws Exception {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// setup check output rule
		output.extension = new JSONExporter().extension();

		int statusCode = catchSystemExit(() -> FlacocoMain.main(new String[]{
				"--projectpath", "examples/exampleFL1/FLtest1",
				"--format", "JSON",
				"-o", "results.json"
		}));
		assertEquals(0, statusCode);
	}

	@Test
	public void testMainCustomExport() throws Exception {
		// Run only on target release >= 5
		Assume.assumeTrue(getCompilerVersion() >= 5);

		// setup check output rule
		output.extension = "custom";

		int statusCode = catchSystemExit(() -> FlacocoMain.main(new String[]{
				"--projectpath", "examples/exampleFL1/FLtest1",
				"--formatter", "src/test/resources/OneLineExporter.java",
				"-o", "results.custom"
		}));
		assertEquals(0, statusCode);
	}

	private static final class CheckOutput extends TestWatcher {
		String extension;

		@Override
		protected void starting(Description description) {
			this.extension = null;
		}

		@Override
		protected void finished(Description description) {
			if (this.extension != null) {
				try {
					File expected = new File("src/test/resources/expected." + this.extension);
					assertTrue("The files differ!", FileUtils.contentEquals(new File("results." + this.extension), expected));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
