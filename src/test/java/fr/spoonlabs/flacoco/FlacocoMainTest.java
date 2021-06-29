package fr.spoonlabs.flacoco;

import static org.junit.Assert.fail;

import org.junit.Test;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;

@SuppressWarnings("unused")
class FlacocoMainTest {

	@Test
	void testMainExplicitArguments() throws Exception {

		// It's a smoke test

		FlacocoMain.main(new String[] { "--projectpath", "examples/exampleFL1/FLtest1", "--formula",
				SpectrumFormula.OCHIAI.name(), "--coverTest", "--framework", FlacocoConfig.TestFramework.JUNIT4.name()

		});
	}

	@Test
	void testMainIncorrectInputs() throws Exception {

		try {

			FlacocoMain.main(new String[] { "--projhjhjhectpathddd", // Incorrect argument
					"examples/exampleFL1/FLtest1", "--formula", SpectrumFormula.OCHIAI.name(), "--coverTest",
					"--framework", FlacocoConfig.TestFramework.JUNIT4.name()

			});

			fail("The main must fail because one argument is incorrect");
		} catch (Throwable t) {

		}
	}

	@Test
	void testMainDefaultValues() throws Exception {

		// It's a smoke test

		FlacocoMain.main(new String[] { "--projectpath", "examples/exampleFL1/FLtest1" });
	}

}
