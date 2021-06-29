package fr.spoonlabs.flacoco;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.fail;

public class FlacocoMainTest {

	@Test
	public void testMainExplicitArguments() throws Exception {

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

		FlacocoMain.main(new String[]{"--projectpath", "examples/exampleFL1/FLtest1", "--formula",
				SpectrumFormula.OCHIAI.name(), "--framework", FlacocoConfig.TestFramework.JUNIT4.name(),
				"--mavenHome", mavenHome, "--junitClasspath", junitClasspath, "--jacocoClasspath", jacocoClassPath

		});
	}

	@Test
	public void testMainIncorrectInputs() throws Exception {

		try {

			FlacocoMain.main(new String[]{"--projhjhjhectpathddd", // Incorrect argument
					"examples/exampleFL1/FLtest1", "--formula", SpectrumFormula.OCHIAI.name(), "--coverTest",
					"--framework", FlacocoConfig.TestFramework.JUNIT4.name()

			});

			fail("The main must fail because one argument is incorrect");
		} catch (Throwable t) {

		}
	}

	@Test
	public void testMainDefaultValues() throws Exception {

		// It's a smoke test

		FlacocoMain.main(new String[]{"--projectpath", "examples/exampleFL1/FLtest1"});
	}

}
