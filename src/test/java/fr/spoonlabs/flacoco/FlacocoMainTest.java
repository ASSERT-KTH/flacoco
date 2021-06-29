package fr.spoonlabs.flacoco;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.localization.spectrum.SpectrumFormula;
import org.junit.Test;

import static org.junit.Assert.fail;

public class FlacocoMainTest {

	@Test
	public void testMainExplicitArguments() throws Exception {

		// It's a smoke test

		FlacocoMain.main(new String[]{"--projectpath", "examples/exampleFL1/FLtest1", "--formula",
				SpectrumFormula.OCHIAI.name(), "--coverTest", "--framework", FlacocoConfig.TestFramework.JUNIT4.name()

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
