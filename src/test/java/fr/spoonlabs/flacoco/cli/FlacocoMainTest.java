package fr.spoonlabs.flacoco.cli;

import fr.spoonlabs.flacoco.cli.FlacocoMain;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class FlacocoMainTest {

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Test
	public void testMainExplicitArguments() {

		// It's a smoke test
		String mavenHome = System.getProperty("user.home") + "/.m2/repository/";
		String junitClasspath = mavenHome + "junit/junit/4.12/junit-4.12.jar" + File.pathSeparatorChar
				+ mavenHome + "org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-api/5.3.2/junit-jupiter-api-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar" + File.pathSeparatorChar
				+ mavenHome + "org/opentest4j/opentest4j/1.1.1/opentest4j-1.1.1.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-commons/1.3.2/junit-platform-commons-1.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-engine/5.3.2/junit-jupiter-engine-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/jupiter/junit-jupiter-params/5.3.2/junit-jupiter-params-5.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-engine/1.3.2/junit-platform-engine-1.3.2.jar" + File.pathSeparatorChar
				+ mavenHome + "org/junit/platform/junit-platform-launcher/1.3.2/junit-platform-launcher-1.3.2.jar";
		String jacocoClassPath = mavenHome + "org/jacoco/org.jacoco.core/0.8.3/org.jacoco.core-0.7.9.jar";

		exit.expectSystemExitWithStatus(0);
		FlacocoMain.main(new String[]{"--projectpath", "examples/exampleFL1/FLtest1", "--formula",
				SpectrumFormula.OCHIAI.name(), "--mavenHome", mavenHome,
				"--junitClasspath", junitClasspath, "--jacocoClasspath", jacocoClassPath,
				"--testRunnerVerbose", "--testRunnerTimeoutInMs", "10000", "--testRunnerJVMArgs", "-Xms16M"
		});
	}

	@Test
	public void testMainIncorrectInputs() {

		exit.expectSystemExitWithStatus(CommandLine.ExitCode.USAGE);
		FlacocoMain.main(new String[]{"--projhjhjhectpathddd", // Incorrect argument
				"examples/exampleFL1/FLtest1", "--formula", SpectrumFormula.OCHIAI.name(), "--coverTest",
		});
	}

	@Test
	public void testMainDefaultValues() {

		// It's a smoke test

		exit.expectSystemExitWithStatus(0);
		FlacocoMain.main(new String[]{"--projectpath", "examples/exampleFL1/FLtest1"});
	}

	@Test
	public void testMainCSVExport() throws IOException {

		exit.expectSystemExitWithStatus(0);
		FlacocoMain.main(new String[]{"--projectpath", "examples/exampleFL1/FLtest1",
				"--format", "CSV", "-o", "results.csv"});

		File expected = new File("src/test/resources/expected.csv");
		assertTrue("The files differ!", FileUtils.contentEquals(new File("results.csv"), expected));
	}

	@Test
	public void testMainJSONExport() throws IOException {

		exit.expectSystemExitWithStatus(0);
		FlacocoMain.main(new String[]{"--projectpath", "examples/exampleFL1/FLtest1",
				"--format", "CSV", "-o", "results.json"});

		File expected = new File("src/test/resources/expected.json");
		assertTrue("The files differ!", FileUtils.contentEquals(new File("results.json"), expected));
	}

	@Test
	public void testMainCustomExport() throws IOException {

		exit.expectSystemExitWithStatus(0);
		FlacocoMain.main(new String[]{"--projectpath", "examples/exampleFL1/FLtest1",
				"--formatter", "src/test/resources/OneLineExporter.java", "-o", "results.custom"});

		File expected = new File("src/test/resources/expected.custom");
		assertTrue("The files differ!", FileUtils.contentEquals(new File("results.custom"), expected));
	}
}
